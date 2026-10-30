package mx.desarrollo.persistence.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import mx.desarrollo.entity.DetalleVentaProducto;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;
import java.util.List;

public class VentaDAO extends AbstractDAO<Venta> {

    public VentaDAO(){
        super(Venta.class);
    }

    @Override
    protected EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    public Venta registrarVenta(Venta venta, List<DetalleVentaProducto> detalles, double tasaIva){
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();

            double subtotal = 0;
            for (DetalleVentaProducto d: detalles){
                Producto p = em.find(Producto.class, d.getProducto().getIdProducto(), LockModeType.PESSIMISTIC_WRITE);

                if(p==null||!Boolean.TRUE.equals(p.getActivo())){
                    throw new IllegalArgumentException("Un producto del carrito ya no esta disponible");
                }
                if(d.getCantidad()<1){
                    throw new IllegalArgumentException("No hay exixstencias suficientes de \"" + p.getNombre() + "\" (disponibles: " + p.getExistencias() + ")");
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

            em.persist(venta);
            for(DetalleVentaProducto d: detalles){
                em.persist(d);
            }

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

    private static double redondear(double valor){
        return Math.round(valor * 100) / 100.0;
    }

}
