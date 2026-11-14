
package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Descuento;
import mx.desarrollo.persistence.persistence.AbstractDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DescuentoDAO extends AbstractDAO<Descuento> {

    private final EntityManager entityManager;

    public DescuentoDAO(EntityManager entityManager) {
        super(Descuento.class);
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }


    public Optional<Descuento> buscarVigentePorProducto(
            Integer idProducto) {

        List<Descuento> resultados = execute(em ->
                em.createQuery(
                                "SELECT d FROM Descuento d " +
                                        "WHERE d.producto.idProducto = :idProducto " +
                                        "AND d.vigencia >= :ahora " +
                                        "ORDER BY d.vigencia ASC, d.idDescuento DESC",
                                Descuento.class
                        )
                        .setParameter("idProducto", idProducto)
                        .setParameter("ahora", LocalDateTime.now())
                        .setMaxResults(1)
                        .getResultList()
        );

        return resultados.stream().findFirst();
    }
}


