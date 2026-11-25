package mx.desarrollo.delegate;

import mx.desarrollo.entity.*;
import mx.desarrollo.persistence.dao.VentaDAO;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DelegateVenta {
    public static final String METODO_EFECTIVO = "EFECTIVO";
    public static final String METODO_TARJETA = "TARJETA";
    public static final String ESTADO_COMPLETADA = "COMPLETADA";
    public static final String ESTADO_CANCELADA = "CANCELADA";
    public static final int LIMITE_TICKETS = 200;

    public Venta registrarVenta(Usuario usuario, String metodoPago, List<DetalleVentaProducto> detalles){
        return registrarVenta(usuario, metodoPago, detalles, List.of());
    }

    public Venta registrarVenta(Usuario usuario, String metodoPago,
                                List<DetalleVentaProducto> detalles,
                                List<DetalleVentaServicio> servicios){
        return registrarVenta(usuario, metodoPago, detalles, servicios, List.of());
    }

    public Venta registrarVenta(Usuario usuario, String metodoPago,
                                List<DetalleVentaProducto> detalles,
                                List<DetalleVentaServicio> servicios,
                                List<DetalleVentaPaquete> paquetes){
        if(usuario == null || usuario.getIdUsuario() == null){
            throw new IllegalArgumentException("No hay un usuario en sesion");
        }
        if((detalles == null || detalles.isEmpty()) && (servicios == null || servicios.isEmpty())
                && (paquetes == null || paquetes.isEmpty())){
            throw new IllegalArgumentException("El carrito esta vacio");
        }
        if (!METODO_EFECTIVO.equals(metodoPago) && !METODO_TARJETA.equals(metodoPago)){
            throw new IllegalArgumentException("Metodo de pago no valido");
        }

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setMetodoPago(metodoPago);
        venta.setEstado(ESTADO_COMPLETADA);
        venta.setFecha(LocalDateTime.now());

        // 0.0 = sin IVA (si quitas el parametro del DAO, borra tambien este 0.0)
        return ServiceLocator.getInstanceVentaDAO().registrarVenta(
                venta, detalles == null ? List.of() : detalles,
                servicios == null ? List.of() : servicios,
                paquetes == null ? List.of() : paquetes);
    }

    public static String validarExistenciasPaquete(String nombrePaquete, List<ProductoPaquete> componentes,
                                                   int cantidad, Map<Integer, Integer> existencias,
                                                   Map<Integer, Integer> reservado){
        if(componentes == null || componentes.isEmpty()){
            return "El paquete \"" + nombrePaquete + "\" no tiene productos.";
        }
        for(ProductoPaquete pp : componentes){
            Integer id = pp.getProducto().getIdProducto();
            int disponibles = existencias.getOrDefault(id, 0);
            int enCarrito = reservado.getOrDefault(id, 0);
            int requeridas = pp.getCantidad() * cantidad;
            if(disponibles - enCarrito < requeridas){
                return "No hay existencias suficientes de \"" + pp.getProducto().getNombre()
                        + "\" para el paquete \"" + nombrePaquete + "\" (disponibles: "
                        + Math.max(0, disponibles - enCarrito) + ", requeridas: " + requeridas + ").";
            }
        }
        return null;
    }

    // ---------- CANCELAR VENTA ----------

    public List<Venta> ventasCancelablesDelTurno(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new IllegalArgumentException("No hay un usuario en sesion");
        }
        return ServiceLocator.getInstanceVentaDAO().ventasPendientesDeCorte(usuario.getIdUsuario());
    }

    public void cancelarVenta(Usuario usuario, Integer idVenta) {
        if (usuario == null || usuario.getIdUsuario() == null || idVenta == null) {
            throw new IllegalArgumentException("Datos no validos");
        }
        ServiceLocator.getInstanceVentaDAO().cancelarVenta(idVenta, usuario.getIdUsuario());
    }

    // ------------------------------------

    public int numeroClienteDelDia(Venta venta){
        return ServiceLocator.getInstanceVentaDAO().numeroClienteDelDia(venta);
    }

    public static String folio(Integer idVenta){
        return VentaDAO.generarFolio(idVenta);
    }

    public List<Venta> listarVentas(){
        return ServiceLocator.getInstanceVentaDAO().findAll();
    }

    public List<Ticket> buscarTickets(String texto){
        String t = texto == null ? "" : texto.trim();
        LocalDate dia = leerDia(t);
        return ServiceLocator.getInstanceVentaDAO()
                .buscarTickets(dia == null ? t : null, dia, LIMITE_TICKETS);
    }

    public List<DetalleVentaProducto> detallesDeVenta(Integer idVenta){
        if(idVenta == null){
            throw new IllegalArgumentException("Venta no valida");
        }
        return ServiceLocator.getInstanceVentaDAO().detallesDeVenta(idVenta);
    }

    public List<DetalleVentaServicio> detallesServicioDeVenta(Integer idVenta) {
        if (idVenta == null) {
            throw new IllegalArgumentException("Venta no valida");
        }
        return ServiceLocator.getInstanceVentaDAO().detallesServicioDeVenta(idVenta);
    }

    public List<DetalleVentaPaquete> detallesPaqueteDeVenta(Integer idVenta) {
        if (idVenta == null) {
            throw new IllegalArgumentException("Venta no valida");
        }
        return ServiceLocator.getInstanceVentaDAO().detallesPaqueteDeVenta(idVenta);
    }

    public List<DetalleVentaServicio> listarTrabajosServicio() {
        return ServiceLocator.getInstanceVentaDAO().listarTrabajosServicio();
    }

    public void marcarTrabajoRealizado(Integer idDetalle) {
        ServiceLocator.getInstanceVentaDAO().actualizarEstadoTrabajo(idDetalle, "Realizado");
    }

    private static final DateTimeFormatter[] FORMATOS_DIA = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    private static LocalDate leerDia(String texto){
        for(DateTimeFormatter f : FORMATOS_DIA){
            try{
                return LocalDate.parse(texto, f);
            }catch (Exception ignorar){
            }
        }
        return null;
    }
}