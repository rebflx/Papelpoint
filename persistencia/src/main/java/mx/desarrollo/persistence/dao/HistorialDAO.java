package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.HistorialModificacionProducto;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

public class HistorialDAO extends AbstractDAO<HistorialModificacionProducto> {

    public HistorialDAO() {
        super(HistorialModificacionProducto.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public List<HistorialModificacionProducto> findAllOrdenado() {
        return execute(em -> {
            em.clear();
            return em.createQuery(
                    "SELECT h FROM HistorialModificacionProducto h "
                            + "JOIN FETCH h.usuario JOIN FETCH h.producto "
                            + "ORDER BY h.fecha DESC, h.idModificacion DESC",
                    HistorialModificacionProducto.class).getResultList();
        });
    }

    public void registrar(Integer idUsuario, Integer idProducto, String detalles) {
        execute(em -> {
            HistorialModificacionProducto h = new HistorialModificacionProducto();
            h.setUsuario(em.getReference(Usuario.class, idUsuario));
            h.setProducto(em.getReference(Producto.class, idProducto));
            h.setFecha(LocalDateTime.now());
            h.setDetalles(detalles);
            em.persist(h);
            return null;
        });
    }
}
