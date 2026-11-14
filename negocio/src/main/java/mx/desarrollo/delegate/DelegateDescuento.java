
package mx.desarrollo.delegate;

import mx.desarrollo.entity.Descuento;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.LocalDateTime;
import java.util.Optional;

public class DelegateDescuento {

    public void guardarDescuento(Descuento descuento) {

        validar(descuento);

        try {
            ServiceLocator.getInstanceDescuentoDAO()
                    .save(descuento);

        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "Error al guardar el descuento.", e
            );
        }
    }

    public void actualizarDescuento(Descuento descuento) {

        validar(descuento);

        if (descuento.getIdDescuento() == null) {
            throw new IllegalArgumentException(
                    "Se necesita un ID para actualizar."
            );
        }

        if (ServiceLocator.getInstanceDescuentoDAO()
                .find(descuento.getIdDescuento()).isEmpty()) {

            throw new IllegalArgumentException(
                    "El descuento no existe."
            );
        }

        try {
            ServiceLocator.getInstanceDescuentoDAO()
                    .update(descuento);

        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "Error al actualizar el descuento.", e
            );
        }
    }

    public Optional<Descuento> consultarDescuentoVigente(
            Integer idProducto) {

        if (idProducto == null || idProducto <= 0) {
            throw new IllegalArgumentException(
                    "El ID del producto no es valido."
            );
        }

        return ServiceLocator.getInstanceDescuentoDAO()
                .buscarVigentePorProducto(idProducto);
    }

    private void validar(Descuento descuento) {

        if (descuento == null) {
            throw new IllegalArgumentException(
                    "El descuento no puede ser nulo."
            );
        }

        Double porcentaje =
                descuento.getPorcentajeDescuento();

        if (porcentaje == null ||
                !Double.isFinite(porcentaje) ||
                porcentaje < 0 || porcentaje > 100) {

            throw new IllegalArgumentException(
                    "El porcentaje debe estar entre 0 y 100."
            );
        }

        if (descuento.getVigencia() == null ||
                !descuento.getVigencia()
                        .isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "La vigencia debe ser una fecha futura."
            );
        }

        Producto producto = descuento.getProducto();

        if (producto == null ||
                producto.getIdProducto() == null ||
                producto.getIdProducto() <= 0) {

            throw new IllegalArgumentException(
                    "Debe seleccionar un producto válido."
            );
        }

        if (ServiceLocator.getInstanceProductoDAO()
                .find(producto.getIdProducto()).isEmpty()) {

            throw new IllegalArgumentException(
                    "El producto no existe."
            );
        }
    }
}
