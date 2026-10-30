package mx.desarrollo.delegate;

import mx.desarrollo.entity.DetalleVentaProducto;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;
import java.time.LocalDateTime;

public class DelegateVenta {
    public static final String METODO_EFECTIVO = "EFECTIVO";
    public static final String METODO_TARJETA = "TARJETA";
    public static final String ESTADO_COMPLETADA = "COMPLETADA";
    public static final double IVA = 0.16;

    public Venta registrarVenta(Usuario usuario, String metodoPago, List<DetalleVentaProducto> detalles){
        if(usuario == null || usuario.getIdUsuario() == null){
            throw new IllegalArgumentException("No hay un usuario en sesion"); //le cambian el mensaje si quieren
        }
        if(detalles == null || detalles.isEmpty()){
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

        return ServiceLocator.getInstanceVentaDAO().registrarVenta(venta, detalles, IVA);
    }
    public List<Venta> listarVentas(){
        return ServiceLocator.getInstanceVentaDAO().findAll();
    }
}
