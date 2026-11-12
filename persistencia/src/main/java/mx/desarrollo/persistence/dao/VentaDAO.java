package mx.desarrollo.persistence.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import mx.desarrollo.entity.DetalleVentaProducto;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.Ticket;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDAO extends AbstractDAO<Venta> {

    public VentaDAO(){
        super(Venta.class);
    }

    @Override
    protected EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    /** Formato unico del folio: NV-14 */
    public static String generarFolio(Integer idVenta){
        return "NV-" + idVenta;
    }

    /**
     * Registra venta + detalles + ticket y descuenta existencias,
     * todo en UNA sola transaccion (si algo falla, nada se guarda).
     */
    public Venta registrarVenta(Venta venta, List<DetalleVentaProducto> detalles, double tasaIva){
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            double subtotal = 0;
            for (DetalleVentaProducto d: detalles){
                if(d.getCantidad() == null || d.getCantidad() < 1){
                    throw new IllegalArgumentException("La cantidad debe ser de al menos 1");
                }

                Producto p = em.find(Producto.class, d.getProducto().getIdProducto(), LockModeType.PESSIMISTIC_WRITE);

                if(p==null||!Boolean.TRUE.equals(p.getActivo())){
                    throw new IllegalArgumentException("Un producto del carrito ya no esta disponible");
                }
                // CORREGIDO: antes se comparaba cantidad < 1 y nunca se validaba el stock
                if(p.getExistencias() < d.getCantidad()){
                    throw new IllegalArgumentException("No hay existencias suficientes de \"" + p.getNombre() + "\" (disponibles: " + p.getExistencias() + ")");
                }

                p.setExistencias(p.getExistencias() - d.getCantidad());
                d.setVenta(venta);
                d.setProducto(p);
                d.setPrecioUnitario(p.getPrecio());

                subtotal += p.getPrecio() * d.getCantidad();
            }

            venta.setUsuario(em.getReference(Usuario.class, venta.getUsuario().getIdUsuario()));
            venta.setSubtotal(redondear(subtotal));
            venta.setTotal(redondear(subtotal * (1 + tasaIva)));

            em.persist(venta);          // con IDENTITY aqui ya se asigna idVenta
            for(DetalleVentaProducto d: detalles){
                em.persist(d);
            }

            Ticket ticket = new Ticket();
            ticket.setVenta(venta);
            ticket.setFechaExpedicion(LocalDateTime.now());
            ticket.setFolio(generarFolio(venta.getIdVenta()));
            em.persist(ticket);

            tx.commit();
            return venta;
        }catch (RuntimeException ex){
            if(tx.isActive()){
                tx.rollback();
            }
            throw ex;
        }finally {
            em.close();
        }
    }

    /**
     * Numero de cliente del dia: cuantas ventas hay hoy hasta esta (inclusive).
     * Funciona igual al reimprimir un ticket viejo.
     */
    public int numeroClienteDelDia(Venta venta){
        LocalDateTime inicio = venta.getFecha().toLocalDate().atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1);
        return execute(em -> Math.toIntExact(em.createQuery(
                        "SELECT COUNT(v) FROM Venta v WHERE v.fecha >= :ini AND v.fecha < :fin AND v.idVenta <= :id",
                        Long.class)
                .setParameter("ini", inicio)
                .setParameter("fin", fin)
                .setParameter("id", venta.getIdVenta())
                .getSingleResult()));
    }

    private static double redondear(double valor){
        return Math.round(valor * 100) / 100.0;
    }

}