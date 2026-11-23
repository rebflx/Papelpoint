package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Paquete;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.ProductoPaquete;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

import java.util.List;
import java.util.Map;

public class PaqueteDAO extends AbstractDAO<Paquete> {
    private final EntityManager entityManager;

    public PaqueteDAO(EntityManager em) {
        super(Paquete.class);
        this.entityManager = em;
    }

    @Override public EntityManager getEntityManager() { return entityManager; }

    public List<Paquete> listarActualizados() {
        return execute(em -> {
            em.clear();
            return em.createQuery("SELECT p FROM Paquete p ORDER BY p.idPaquete DESC", Paquete.class)
                    .getResultList();
        });
    }

    public List<ProductoPaquete> obtenerProductos(Integer idPaquete) {
        return execute(em -> em.createQuery(
                        "SELECT pp FROM ProductoPaquete pp JOIN FETCH pp.producto " +
                                "WHERE pp.paquete.idPaquete = :id ORDER BY pp.idProductoPaquete", ProductoPaquete.class)
                .setParameter("id", idPaquete).getResultList());
    }

    public void guardarPaquete(Paquete paquete, Map<Integer, Integer> cantidades) {
        execute(em -> {
            em.persist(paquete);
            guardarDetalles(em, paquete, cantidades);
            return null;
        });
    }

    public void actualizarPaquete(Paquete datos, Map<Integer, Integer> cantidades) {
        execute(em -> {
            Paquete actual = em.find(Paquete.class, datos.getIdPaquete());
            if (actual == null || !Boolean.TRUE.equals(actual.getActiva())) {
                throw new IllegalArgumentException("El paquete ya no existe o está desactivado.");
            }
            actual.setNombrePaquete(datos.getNombrePaquete());
            actual.setPrecio(datos.getPrecio());
            actual.setDescripcion(datos.getDescripcion());
            List<ProductoPaquete> anteriores = em.createQuery(
                            "SELECT pp FROM ProductoPaquete pp WHERE pp.paquete.idPaquete = :id", ProductoPaquete.class)
                    .setParameter("id", actual.getIdPaquete()).getResultList();
            for (ProductoPaquete anterior : anteriores) em.remove(anterior);
            em.flush();
            guardarDetalles(em, actual, cantidades);
            return null;
        });
    }

    private void guardarDetalles(EntityManager em, Paquete paquete, Map<Integer, Integer> cantidades) {
        for (Map.Entry<Integer, Integer> entrada : cantidades.entrySet()) {
            Producto producto = em.find(Producto.class, entrada.getKey());
            int cantidad = entrada.getValue();
            if (producto == null || !Boolean.TRUE.equals(producto.getActivo()) ||
                    producto.getExistencias() == null || producto.getExistencias() < cantidad) {
                throw new IllegalArgumentException("No hay suficientes existencias del producto seleccionado.");
            }
            ProductoPaquete detalle = new ProductoPaquete();
            detalle.setPaquete(paquete);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            em.persist(detalle);
        }
    }

    public void desactivar(Integer id) {
        execute(em -> {
            Paquete p = em.find(Paquete.class, id);
            if (p == null) throw new IllegalArgumentException("No se encontró el paquete.");
            p.setActiva(false);
            return null;
        });
    }

    public List<Paquete> listarActivos() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Paquete p WHERE p.activa = true ORDER BY p.nombrePaquete", Paquete.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<ProductoPaquete> componentesActuales(Integer idPaquete) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT pp FROM ProductoPaquete pp JOIN FETCH pp.producto "
                                    + "WHERE pp.paquete.idPaquete = :id ORDER BY pp.idProductoPaquete", ProductoPaquete.class)
                    .setParameter("id", idPaquete).getResultList();
        } finally {
            em.close();
        }
    }
}
