package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateVenta;
import mx.desarrollo.entity.DetalleVentaProducto;
import mx.desarrollo.entity.DetalleVentaServicio;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;

import java.util.List;

public class FacadeVenta {

    private final DelegateVenta delegateVenta = new DelegateVenta();

    public Venta registrarVenta(Usuario usuario, String metodoPago, List<DetalleVentaProducto> detalles) {
        return delegateVenta.registrarVenta(usuario, metodoPago, detalles);
    }

    public Venta registrarVenta(Usuario usuario, String metodoPago,
                                List<DetalleVentaProducto> detalles,
                                List<DetalleVentaServicio> servicios) {
        return delegateVenta.registrarVenta(usuario, metodoPago, detalles, servicios);
    }

    public List<DetalleVentaServicio> listarTrabajosServicio() {
        return delegateVenta.listarTrabajosServicio();
    }

    public void marcarTrabajoRealizado(Integer idDetalle) {
        delegateVenta.marcarTrabajoRealizado(idDetalle);
    }

    public List<Venta> listarVentas() {
        return delegateVenta.listarVentas();
    }
}