package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Unidad_aprendizaje;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import java.util.Optional;

public class UnidadDAO extends AbstractDAO<Unidad_aprendizaje> {

    private final EntityManager entityManager;

    public UnidadDAO(EntityManager em) {
        super(Unidad_aprendizaje.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}