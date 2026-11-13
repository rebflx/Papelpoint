
package mx.desarrollo.persistence.dao;

import mx.desarrollo.entity.Servicio;
import jakarta.persistence.EntityManager;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.util.List;

public class servicioDAO extends AbstractDAO<Servicio> {

    private final EntityManager entityManager;

    public servicioDAO(EntityManager em) {
        super(Servicio.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Servicio> findAllFresh() {
        return execute(em -> {
            em.clear();
            return em.createQuery(
                    "SELECT s FROM Servicio s", Servicio.class
            ).getResultList();
        });
    }
}
