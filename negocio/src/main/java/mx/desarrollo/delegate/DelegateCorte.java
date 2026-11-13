package mx.desarrollo.delegate;

import mx.desarrollo.entity.CorteCaja;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.persistence.dao.CorteCajaDAO;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DelegateCorte {

    public static final String METODO_EFECTIVO = DelegateVenta.METODO_EFECTIVO;
    public static final String ESTADO_COMPLETADA = DelegateVenta.ESTADO_COMPLETADA;
    public static final String ESTADO_CANCELADA = "CANCELADA";

    public static final int LIMITE_HISTORIAL = 200;
    public static final double MONTO_MAXIMO = 99_999_999.99;

    public static final double[] DENOMINACIONES = {1000, 500, 200, 100, 50, 20, 10, 5, 2, 1, 0.5};

    private static final int MAX_PIEZAS_POR_DENOMINACION = 1_000_000;
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private CorteCajaDAO dao() {
        return ServiceLocator.getInstanceCorteCajaDAO();
    }

    public ResumenCorte consultarTurno(Usuario sesion, Usuario objetivo) {
        validarSesion(sesion);
        Usuario u = objetivo == null ? sesion : objetivo;
        validarAcceso(sesion, u);
        List<Venta> ventas = dao().ventasPendientes(u.getIdUsuario());
        return construirResumen(ventas, u, null, null, null, null);
    }


    public ResumenCorte cerrarCorte(Usuario sesion, Usuario objetivo, double efectivoContado, double fondoInicial) {
        validarSesion(sesion);
        Usuario u = objetivo == null ? sesion : objetivo;
        validarAcceso(sesion, u);
        validarMonto(efectivoContado, "El efectivo contado");
        validarMonto(fondoInicial, "El fondo inicial");

        CorteCaja corte = dao().registrarCorte(u, redondear(efectivoContado), redondear(fondoInicial),
                METODO_EFECTIVO, ESTADO_COMPLETADA);

        List<Venta> ventas = dao().ventasDeCorte(corte.getIdCorte());
        return construirResumen(ventas, corte.getUsuario(), null, corte.getFechaInicio(), corte.getFechaCierre(), corte);
    }


    public ResumenCorte corteFinal(Usuario sesion, LocalDate dia) {
        validarAdmin(sesion);
        LocalDate d = dia == null ? LocalDate.now() : dia;
        List<Venta> ventas = dao().ventasDelDia(d);
        return construirResumen(ventas, null, d, null, null, null);
    }


    public List<CorteCaja> historial(Usuario sesion, LocalDate desde, LocalDate hasta, Usuario filtroUsuario) {
        validarAdmin(sesion);
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la final");
        }
        Integer idUsuario = filtroUsuario == null ? null : filtroUsuario.getIdUsuario();
        return dao().buscarCortes(desde, hasta, idUsuario, LIMITE_HISTORIAL);
    }

    public ResumenCorte detalleCorte(Usuario sesion, Integer idCorte) {
        validarSesion(sesion);
        if (idCorte == null) {
            throw new IllegalArgumentException("Corte no valido");
        }
        CorteCaja corte = dao().buscarConUsuario(idCorte);
        if (corte == null) {
            throw new IllegalArgumentException("El corte ya no existe");
        }
        validarAcceso(sesion, corte.getUsuario());
        List<Venta> ventas = dao().ventasDeCorte(idCorte);
        return construirResumen(ventas, corte.getUsuario(), null, corte.getFechaInicio(), corte.getFechaCierre(), corte);
    }

    public static double diferencia(CorteCaja corte) {
        double contado = corte.getTotalContado() == null ? 0 : corte.getTotalContado();
        double calculado = corte.getTotalCalculado() == null ? 0 : corte.getTotalCalculado();
        return redondear(contado - calculado);
    }


    public List<Usuario> usuariosParaCorte(Usuario sesion) {
        validarSesion(sesion);
        List<Usuario> lista = new ArrayList<>();
        if (esAdmin(sesion)) {
            for (Usuario u : ServiceLocator.getInstanceUsuarioDAO().findAll()) {
                if (Boolean.TRUE.equals(u.getActivo())) {
                    lista.add(u);
                }
            }
            lista.sort(Comparator.comparing(Usuario::getNombrePersona, String.CASE_INSENSITIVE_ORDER));
        } else {
            lista.add(sesion);
        }
        return lista;
    }


    public double totalDenominaciones(Map<Double, Integer> piezasPorDenominacion) {
        BigDecimal total = BigDecimal.ZERO;
        if (piezasPorDenominacion == null) {
            return 0;
        }
        for (Map.Entry<Double, Integer> e : piezasPorDenominacion.entrySet()) {
            double denom = e.getKey() == null ? -1 : e.getKey();
            int piezas = e.getValue() == null ? 0 : e.getValue();
            if (!denominacionValida(denom)) {
                throw new IllegalArgumentException("Denominacion no valida: " + denom);
            }
            if (piezas < 0 || piezas > MAX_PIEZAS_POR_DENOMINACION) {
                throw new IllegalArgumentException("Cantidad no valida para la denominacion $" + denom);
            }
            total = total.add(BigDecimal.valueOf(denom).multiply(BigDecimal.valueOf(piezas)));
        }
        return total.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    public List<String> lineasImpresion(ResumenCorte r, int ancho) {
        if (r == null) {
            throw new IllegalArgumentException("No hay corte para imprimir");
        }
        int w = Math.max(ancho, 32);
        String linea = "-".repeat(w);
        List<String> out = new ArrayList<>();

        out.add(centrar("PAPELPOINT", w));
        if (r.getDia() != null) {
            out.add(centrar("CORTE FINAL DEL DIA", w));
            out.add(centrar(r.getDia().format(FECHA), w));
        } else if (r.isCerrado()) {
            out.add(centrar("CORTE DE CAJA", w));
        } else {
            out.add(centrar("CORTE DE CAJA (TURNO EN CURSO)", w));
        }
        out.add(linea);

        if (r.getIdCorte() != null) {
            out.add(renglon("Corte No.:", String.valueOf(r.getIdCorte()), w));
        }
        if (r.getUsuario() != null) {
            out.add(renglon("Cajero:", sinAcentos(r.getUsuario().getNombrePersona()), w));
        }
        if (r.getFechaInicio() != null && r.getDia() == null) {
            out.add(renglon("Inicio:", r.getFechaInicio().format(FECHA_HORA), w));
        }
        if (r.getFechaCierre() != null) {
            out.add(renglon("Cierre:", r.getFechaCierre().format(FECHA_HORA), w));
        }
        out.add(linea);

        for (Map.Entry<String, ResumenCorte.Linea> e : r.getPorMetodo().entrySet()) {
            out.add(renglon("Ventas " + nombreMetodo(e.getKey()) + " (" + e.getValue().getTransacciones() + ")",
                    dinero(e.getValue().getMonto()), w));
        }
        if (r.getNumCanceladas() > 0) {
            out.add(renglon("Cancelaciones (" + r.getNumCanceladas() + ")", dinero(-r.getMontoCanceladas()), w));
        }
        out.add(linea);
        out.add(renglon("TOTAL VENTAS (" + r.getNumVentas() + ")", dinero(r.getTotalVentas()), w));

        if (r.getDia() != null && !r.getTotalPorUsuario().isEmpty()) {
            out.add(linea);
            out.add("Por usuario:");
            for (Map.Entry<String, Double> e : r.getTotalPorUsuario().entrySet()) {
                out.add(renglon("  " + sinAcentos(e.getKey()), dinero(e.getValue()), w));
            }
        }
        if (r.getDia() != null) {
            out.add(renglon("Ventas sin corte:", String.valueOf(r.getVentasSinCorte()), w));
        }

        if (r.isCerrado()) {
            out.add(linea);
            out.add(renglon("Total contado:", dinero(r.getTotalContado()), w));
            double dif = r.getDiferencia();
            String etiqueta = dif > 0 ? "SOBRANTE:" : dif < 0 ? "FALTANTE:" : "DIFERENCIA:";
            out.add(renglon(etiqueta, dinero(Math.abs(dif)), w));
        }

        out.add(linea);
        out.add(centrar("Impreso: " + LocalDateTime.now().format(FECHA_HORA), w));
        return out;
    }


    ResumenCorte construirResumen(List<Venta> ventas, Usuario usuario, LocalDate dia,
                                  LocalDateTime inicio, LocalDateTime cierre, CorteCaja corte) {
        int n = 0, nEfectivo = 0, nCanceladas = 0, sinCorte = 0;
        double total = 0, efectivo = 0, canceladas = 0;
        LocalDateTime primera = null;

        Map<String, double[]> metodos = new LinkedHashMap<>();   // metodo -> {transacciones, monto}
        Map<String, Double> porUsuario = new LinkedHashMap<>();

        for (Venta v : ventas) {
            if (primera == null || v.getFecha().isBefore(primera)) {
                primera = v.getFecha();
            }
            if (ESTADO_CANCELADA.equals(v.getEstado())) {
                nCanceladas++;
                canceladas += v.getTotal();
                continue;
            }
            if (!ESTADO_COMPLETADA.equals(v.getEstado())) {
                continue;
            }
            n++;
            total += v.getTotal();
            if (v.getCorteCaja() == null) {
                sinCorte++;
            }
            double[] acum = metodos.computeIfAbsent(v.getMetodoPago(), k -> new double[2]);
            acum[0]++;
            acum[1] += v.getTotal();
            if (METODO_EFECTIVO.equals(v.getMetodoPago())) {
                nEfectivo++;
                efectivo += v.getTotal();
            }
            if (usuario == null && v.getUsuario() != null) {
                porUsuario.merge(v.getUsuario().getNombrePersona(), v.getTotal(), Double::sum);
            }
        }

        ResumenCorte r = new ResumenCorte();
        r.setUsuario(usuario);
        r.setDia(dia);
        r.setFechaInicio(inicio != null ? inicio : primera);
        r.setFechaCierre(cierre);
        r.setTotales(n, redondear(total), nEfectivo, redondear(efectivo), nCanceladas, redondear(canceladas));
        r.setVentasSinCorte(sinCorte);
        for (Map.Entry<String, double[]> e : metodos.entrySet()) {
            r.agregarMetodo(e.getKey(), (int) e.getValue()[0], redondear(e.getValue()[1]));
        }
        for (Map.Entry<String, Double> e : porUsuario.entrySet()) {
            r.agregarTotalUsuario(e.getKey(), redondear(e.getValue()));
        }
        r.setVentas(new ArrayList<>(ventas));
        if (corte != null) {
            r.setIdCorte(corte.getIdCorte());
            r.setTotalContado(corte.getTotalContado());
        }
        return r;
    }


    private static boolean esAdmin(Usuario u) {
        return u != null && DelegateUsuario.ROL_ADMIN.equalsIgnoreCase(u.getRol());
    }

    private static void validarSesion(Usuario sesion) {
        if (sesion == null || sesion.getIdUsuario() == null) {
            throw new IllegalArgumentException("No hay un usuario en sesion");
        }
    }

    private static void validarAdmin(Usuario sesion) {
        validarSesion(sesion);
        if (!esAdmin(sesion)) {
            throw new IllegalArgumentException("Esta seccion es solo para los administradores");
        }
    }

    private static void validarAcceso(Usuario sesion, Usuario objetivo) {
        if (objetivo == null || objetivo.getIdUsuario() == null) {
            throw new IllegalArgumentException("Usuario no valido");
        }
        if (!esAdmin(sesion) && !sesion.getIdUsuario().equals(objetivo.getIdUsuario())) {
            throw new IllegalArgumentException("Solo puedes consultar y cerrar tu propio corte");
        }
    }

    private static void validarMonto(double monto, String campo) {
        if (Double.isNaN(monto) || Double.isInfinite(monto)) {
            throw new IllegalArgumentException(campo + " no es un numero valido");
        }
        if (monto < 0) {
            throw new IllegalArgumentException(campo + " no puede ser negativo");
        }
        if (monto > MONTO_MAXIMO) {
            throw new IllegalArgumentException(campo + " es demasiado grande");
        }
    }

    private static boolean denominacionValida(double d) {
        for (double v : DENOMINACIONES) {
            if (Double.compare(v, d) == 0) {
                return true;
            }
        }
        return false;
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }

    private static String dinero(double monto) {
        String s = String.format(Locale.US, "$%,.2f", Math.abs(monto));
        return monto < 0 ? "-" + s : s;
    }

    private static String nombreMetodo(String metodo) {
        if (metodo == null || metodo.isBlank()) {
            return "";
        }
        String m = metodo.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(m.charAt(0)) + m.substring(1);
    }

    private static String sinAcentos(String s) {
        if (s == null) {
            return "";
        }
        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    private static String centrar(String texto, int ancho) {
        if (texto.length() >= ancho) {
            return texto;
        }
        return " ".repeat((ancho - texto.length()) / 2) + texto;
    }

    private static String renglon(String izq, String der, int ancho) {
        int espacio = ancho - izq.length() - der.length();
        if (espacio < 1) {
            int maxIzq = Math.max(1, ancho - der.length() - 1);
            izq = izq.substring(0, Math.min(izq.length(), maxIzq));
            espacio = Math.max(1, ancho - izq.length() - der.length());
        }
        return izq + " ".repeat(espacio) + der;
    }
}
