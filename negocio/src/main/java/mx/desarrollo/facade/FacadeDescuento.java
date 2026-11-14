
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateDescuento;
import mx.desarrollo.entity.Descuento;

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

    public Optional<Descuento> consultarDescuentoVigente(
            Integer idProducto) {

        return delegate.consultarDescuentoVigente(idProducto);
    }
}

