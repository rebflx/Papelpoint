package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateVenta;
import mx.desarrollo.entity.DetalleVentaProducto;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;

import java.util.List;

public class FacadeVenta {

    private final DelegateVenta delegateVenta = new DelegateVenta();

    public Venta registrarVenta(Usuario usuario, String metodoPago, List<DetalleVentaProducto> detalles) {
        return delegateVenta.registrarVenta(usuario, metodoPago, detalles);
    }

    public List<Venta> listarVentas() {
        return delegateVenta.listarVentas();
    }
}