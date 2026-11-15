
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateDescuento;
import mx.desarrollo.entity.Descuento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FacadeDescuento {

    private final DelegateDescuento delegate =
            new DelegateDescuento();

    public void guardarDescuento(Descuento descuento) {
        delegate.guardarDescuento(descuento);
    }

    public void actualizarDescuento(Descuento descuento) {
        delegate.actualizarDescuento(descuento);
    }

    public Optional<Descuento> consultarDescuentoVigente(Integer idProducto) {

        return delegate.consultarDescuentoVigente(idProducto);
    }

    public void asignarDescuento(Integer idProducto, double porcentaje, java.time.LocalDateTime vigencia){
        delegate.asignarDescuento( idProducto, porcentaje, vigencia);
    }

    public double calcularPrecioConDescuento(mx.desarrollo.entity.Producto producto){
        return delegate.calcularPrecioConDescuento(producto);
    }


    public List<Descuento> obtenerDescuentos() {
        return delegate.obtenerDescuentos();
    }

    public void modificarDescuento(Integer idDescuento, Integer idProducto, double porcentaje, LocalDateTime vigencia) {
        delegate.modificarDescuento(idDescuento, idProducto, porcentaje, vigencia
        );
    }

    public void eliminarDescuento(Integer idDescuento) {
        delegate.eliminarDescuento(idDescuento);
    }

}

