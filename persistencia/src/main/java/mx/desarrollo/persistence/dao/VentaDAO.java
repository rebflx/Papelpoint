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
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
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

    public static String generarFolio(Integer idVenta){
        return "NV-" + idVenta;
    }


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


    public List<Ticket> buscarTickets(String folio, LocalDate dia, int limite){
        String texto = folio == null ? "" : folio.replaceAll("[%_\\\\]", "").trim().toLowerCase();

        StringBuilder jpql = new StringBuilder(
                "SELECT t FROM Ticket t JOIN FETCH t.venta v JOIN FETCH v.usuario WHERE 1 = 1");
        if(!texto.isEmpty()){
            jpql.append(" AND LOWER(t.folio) LIKE :folio");
        }
        if(dia != null){
            jpql.append(" AND t.fechaExpedicion >= :ini AND t.fechaExpedicion < :fin");
        }
        jpql.append(" ORDER BY t.fechaExpedicion DESC, t.idTicket DESC");

        EntityManager em = getEntityManager();
        try{
            TypedQuery<Ticket> q = em.createQuery(jpql.toString(), Ticket.class);
            if(!texto.isEmpty()){
                q.setParameter("folio", "%" + texto + "%");
            }
            if(dia != null){
                q.setParameter("ini", dia.atStartOfDay());
                q.setParameter("fin", dia.plusDays(1).atStartOfDay());
            }
            q.setMaxResults(limite);
            return q.getResultList();
        }finally {
            em.close();
        }
    }

    public List<DetalleVentaProducto> detallesDeVenta(Integer idVenta){
        EntityManager em = getEntityManager();
        try{
            return em.createQuery(
                            "SELECT d FROM DetalleVentaProducto d JOIN FETCH d.producto "
                                    + "WHERE d.venta.idVenta = :id ORDER BY d.idDetalleProducto",
                            DetalleVentaProducto.class)
                    .setParameter("id", idVenta)
                    .getResultList();
        }finally {
            em.close();
        }
    }

    private static double redondear(double valor){
        return Math.round(valor * 100) / 100.0;
    }

}