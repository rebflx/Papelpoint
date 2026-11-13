package mx.desarrollo.delegate;

import mx.desarrollo.entity.DetalleVentaProducto;
import mx.desarrollo.entity.DetalleVentaServicio;
import mx.desarrollo.entity.Ticket;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.persistence.dao.VentaDAO;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DelegateVenta {
    public static final String METODO_EFECTIVO = "EFECTIVO";
    public static final String METODO_TARJETA = "TARJETA";
    public static final String ESTADO_COMPLETADA = "COMPLETADA";
    public static final double IVA = 0.16;
    public static final int LIMITE_TICKETS = 200;

    public Venta registrarVenta(Usuario usuario, String metodoPago, List<DetalleVentaProducto> detalles){
        return registrarVenta(usuario, metodoPago, detalles, List.of());
    }

    public Venta registrarVenta(Usuario usuario, String metodoPago,
                                List<DetalleVentaProducto> detalles,
                                List<DetalleVentaServicio> servicios){
        if(usuario == null || usuario.getIdUsuario() == null){
            throw new IllegalArgumentException("No hay un usuario en sesion");
        }
        if((detalles == null || detalles.isEmpty()) && (servicios == null || servicios.isEmpty())){
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

        return ServiceLocator.getInstanceVentaDAO().registrarVenta(
                venta, detalles == null ? List.of() : detalles,
                servicios == null ? List.of() : servicios, IVA);
    }

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

    public List<DetalleVentaServicio> listarTrabajosServicio() {
        return ServiceLocator.getInstanceVentaDAO().listarTrabajosServicio();
    }

    public void marcarTrabajoRealizado(Integer idDetalle) {
        ServiceLocator.getInstanceVentaDAO().actualizarEstadoTrabajo(idDetalle, "Realizado");
    }

    public static double iva(Venta venta){
        return Math.round((venta.getTotal() - venta.getSubtotal()) * 100) / 100.0;
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