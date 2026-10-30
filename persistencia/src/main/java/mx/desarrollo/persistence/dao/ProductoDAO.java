package mx.desarrollo.persistence.dao;

import mx.desarrollo.entity.Producto;
import jakarta.persistence.EntityManager;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

public class ProductoDAO extends AbstractDAO<Producto> {

    private final EntityManager entityManager;

    public ProductoDAO(EntityManager em) {
        super(Producto.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;

    }

    public List<Producto> findAllFresh() {
        return execute(em -> {
            em.clear();
            return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        });
    }

}