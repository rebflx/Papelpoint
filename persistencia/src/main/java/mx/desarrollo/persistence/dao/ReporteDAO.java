package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.persistence.persistence.HibernateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public record VentaFila(
            int id,
            LocalDateTime fecha,
            String metodo,
            BigDecimal total
    ) {}

    public record Detalle(
            int idVenta,
            String categoria,
            long cantidad,
            BigDecimal importe
    ) {}

    public record Datos(
            List<VentaFila> ventas,
            List<Detalle> detalles
    ) {}

    public Datos consultar(
            LocalDate desde,
            LocalDate hasta) {

        EntityManager em =
                HibernateUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            LocalDateTime inicio =
                    desde.atStartOfDay();

            LocalDateTime fin =
                    hasta.plusDays(1).atStartOfDay();

            List<VentaFila> ventas =
                    new ArrayList<>();

            List<Detalle> detalles =
                    new ArrayList<>();

            List<?> resultadosVentas =
                    em.createNativeQuery(
                                    "SELECT idVenta, fecha, metodoPago, total "
                                            + "FROM vw_reporte_ventas "
                                            + "WHERE fecha >= :desde "
                                            + "AND fecha < :hasta "
                                            + "ORDER BY fecha DESC, idVenta DESC"
                            )
                            .setParameter(
                                    "desde",
                                    Timestamp.valueOf(inicio)
                            )
                            .setParameter(
                                    "hasta",
                                    Timestamp.valueOf(fin)
                            )
                            .getResultList();

            for (Object resultado : resultadosVentas) {

                Object[] fila = (Object[]) resultado;

                ventas.add(new VentaFila(
                        ((Number) fila[0]).intValue(),
                        convertirFecha(fila[1]),
                        (String) fila[2],
                        convertirDinero(fila[3])
                ));
            }

            List<?> resultadosDetalles =
                    em.createNativeQuery(
                                    "SELECT idVenta, categoria, cantidad, importe "
                                            + "FROM vw_reporte_detalles "
                                            + "WHERE fecha >= :desde "
                                            + "AND fecha < :hasta"
                            )
                            .setParameter(
                                    "desde",
                                    Timestamp.valueOf(inicio)
                            )
                            .setParameter(
                                    "hasta",
                                    Timestamp.valueOf(fin)
                            )
                            .getResultList();

            for (Object resultado : resultadosDetalles) {

                Object[] fila = (Object[]) resultado;

                detalles.add(new Detalle(
                        ((Number) fila[0]).intValue(),
                        (String) fila[1],
                        ((Number) fila[2]).longValue(),
                        convertirDinero(fila[3])
                ));
            }

            em.getTransaction().commit();

            return new Datos(ventas, detalles);

        } catch (RuntimeException e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {

            em.close();
        }
    }

    private BigDecimal convertirDinero(Object valor) {

        return new BigDecimal(valor.toString())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDateTime convertirFecha(Object valor) {

        if (valor instanceof LocalDateTime) {
            return (LocalDateTime) valor;
        }

        if (valor instanceof Timestamp) {
            return ((Timestamp) valor).toLocalDateTime();
        }

        return LocalDateTime.parse(
                valor.toString().replace(" ", "T")
        );
    }
}



