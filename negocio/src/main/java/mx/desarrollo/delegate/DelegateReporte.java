package mx.desarrollo.delegate;

import mx.desarrollo.persistence.dao.ReporteDAO;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class DelegateReporte {

    public record Fila(
            int id,
            LocalDateTime fecha,
            String metodo,
            BigDecimal importe
    ) {}

    public record Resumen(
            BigDecimal total,
            int ventas,
            long productos,
            BigDecimal promedio,
            List<Fila> filas,
            Map<LocalDate, BigDecimal> porDia,
            Map<String, BigDecimal> porCategoria
    ) {}

    public Resumen consultar(
            LocalDate desde,
            LocalDate hasta,
            String categoria) {

        validar(desde, hasta, categoria);

        ReporteDAO.Datos datos =
                ServiceLocator.getInstanceReporteDAO().consultar(desde, hasta);
        return procesar(datos, categoria);
    }

    private void validar(
            LocalDate desde,
            LocalDate hasta,
            String categoria) {

        if (desde == null || hasta == null || desde.isAfter(hasta)) {

            throw new IllegalArgumentException("Rango de fechas no válido.");
        }

        if (categoria == null || !List.of("Todas", "Productos", "Servicios", "Paquetes").contains(categoria)) {

            throw new IllegalArgumentException(
                    "Categoría no válida."
            );
        }
    }

    public Resumen procesar(
            ReporteDAO.Datos datos,
            String categoria) {

        boolean todas = "Todas".equals(categoria);

        Map<Integer, List<ReporteDAO.Detalle>>
                agrupados = new HashMap<>();

        for (ReporteDAO.Detalle d : datos.detalles()) {

            agrupados.computeIfAbsent(d.idVenta(), k -> new ArrayList<>()).add(d);
        }

        List<Fila> filas = new ArrayList<>();

        Map<LocalDate, BigDecimal> dias =
                new TreeMap<>();

        Map<String, BigDecimal> categorias =
                new LinkedHashMap<>();

        BigDecimal total = BigDecimal.ZERO;

        long productos = 0;

        for (ReporteDAO.VentaFila v : datos.ventas()) {

            List<ReporteDAO.Detalle> detalles =
                    agrupados.getOrDefault(v.id(), List.of());

            List<ReporteDAO.Detalle> seleccion =
                    detalles.stream().filter(d -> todas || categoria.equals(d.categoria())).toList();

            if (!todas && seleccion.isEmpty()) {
                continue;
            }

            BigDecimal importe;

            if (todas) {
                importe = v.total();

            } else {

                importe = seleccion.stream()
                        .map(ReporteDAO.Detalle::importe)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            total = total.add(importe);
            filas.add(new Fila(v.id(), v.fecha(), v.metodo(), importe));

            dias.merge(v.fecha().toLocalDate(), importe, BigDecimal::add);

            for (ReporteDAO.Detalle d : seleccion) {
                categorias.merge(d.categoria(), d.importe(), BigDecimal::add);

                if ("Productos".equals(d.categoria())) {

                    productos += d.cantidad();
                }
            }
        }
        BigDecimal promedio;

        if (filas.isEmpty()) {

            promedio = BigDecimal.ZERO;

        } else {

            promedio = total.divide(BigDecimal.valueOf(filas.size()), 2, RoundingMode.HALF_UP);
        }

        return new Resumen(total, filas.size(), productos, promedio, List.copyOf(filas), dias, categorias);
    }
}
