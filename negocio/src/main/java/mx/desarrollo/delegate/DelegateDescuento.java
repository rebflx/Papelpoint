
package mx.desarrollo.delegate;

import mx.desarrollo.entity.Descuento;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DelegateDescuento {

    public void guardarDescuento(Descuento descuento) {

        validar(descuento);

        try {
            ServiceLocator.getInstanceDescuentoDAO()
                    .save(descuento);

        } catch (RuntimeException e) {
            throw new IllegalStateException("Error al guardar el descuento.", e);
        }
    }

    public void actualizarDescuento(Descuento descuento) {

        validar(descuento);

        if (descuento.getIdDescuento() == null) {
            throw new IllegalArgumentException("Se necesita un ID para actualizar.");
        }

        if (ServiceLocator.getInstanceDescuentoDAO()
                .find(descuento.getIdDescuento()).isEmpty()) {

            throw new IllegalArgumentException("El descuento no existe.");
        }

        try {
            ServiceLocator.getInstanceDescuentoDAO().update(descuento);

        } catch (RuntimeException e) {
            throw new IllegalStateException("Error al actualizar el descuento.", e);
        }
    }

    public Optional<Descuento> consultarDescuentoVigente(
            Integer idProducto) {

        if (idProducto == null || idProducto <= 0) {
            throw new IllegalArgumentException("El ID del producto no es valido.");
        }

        return ServiceLocator.getInstanceDescuentoDAO().buscarVigentePorProducto(idProducto);
    }

    private void validar(Descuento descuento) {

        if (descuento == null) {
            throw new IllegalArgumentException("El descuento no puede ser nulo.");
        }

        Double porcentaje = descuento.getPorcentajeDescuento();

        if (porcentaje == null || !Double.isFinite(porcentaje) || porcentaje < 1 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 100.");
        }

        if (descuento.getVigencia() == null || !descuento.getVigencia().isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException("La vigencia debe ser una fecha futura.");
        }

        Producto producto = descuento.getProducto();

        if (producto == null || producto.getIdProducto() == null || producto.getIdProducto() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un producto valido.");
        }

        if (ServiceLocator.getInstanceProductoDAO().find(producto.getIdProducto()).isEmpty()) {
            throw new IllegalArgumentException("El producto no existe.");
        }
    }


    public void asignarDescuento(Integer idProducto, double porcentaje, LocalDateTime vigencia) {

        if (idProducto == null || idProducto <= 0) {
            throw new IllegalArgumentException("Selecciona un producto valido.");
        }

        if (!Double.isFinite(porcentaje) || porcentaje < 1 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 100.");
        }

        if (vigencia == null || !vigencia.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La vigencia debe ser una fecha futura.");
        }

        Producto producto = ServiceLocator.getInstanceProductoDAO().find(idProducto).orElseThrow(() -> new IllegalArgumentException("El producto no existe."));

        if (consultarDescuentoVigente(idProducto).isPresent()) {
            throw new IllegalArgumentException("Este producto ya tiene un descuento vigente.");
        }

        Descuento descuento = new Descuento();

        descuento.setProducto(producto);
        descuento.setPorcentajeDescuento(porcentaje);
        descuento.setVigencia(vigencia);

        guardarDescuento(descuento);
    }


    public double calcularPrecioConDescuento(Producto producto) {
        if (producto == null || producto.getIdProducto() == null || producto.getPrecio() == null) {
            throw new IllegalArgumentException("Producto invalido.");
        }

        double precio = producto.getPrecio();

        Optional<Descuento> decuento = consultarDescuentoVigente(producto.getIdProducto());

        if (decuento.isPresent()) {
            double porcentaje = decuento.get().getPorcentajeDescuento();

            precio = precio * (1 - porcentaje / 100.0);
        }
        return Math.round(precio * 100.0) / 100.0;
    }


    public List<Descuento> obtenerDescuentos() {
        return ServiceLocator.getInstanceDescuentoDAO().obtenerTodos();
    }

    public void modificarDescuento(Integer idDescuento, Integer idProducto, double porcentaje, LocalDateTime vigencia) {

        if (idDescuento == null) {
            throw new IllegalArgumentException("Selecciona un descuento.");
        }

        if (porcentaje < 1 || porcentaje > 100 || !Double.isFinite(porcentaje)) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 100.");
        }

        if (vigencia == null || !vigencia.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La vigencia debe ser una fecha futura.");
        }

        Descuento descuento = ServiceLocator.getInstanceDescuentoDAO().find(idDescuento).orElseThrow(() ->
                        new IllegalArgumentException("El descuento no existe."));

        Producto producto = ServiceLocator.getInstanceProductoDAO().find(idProducto).orElseThrow(() ->
                        new IllegalArgumentException("El producto no existe."));

        for (Descuento otro : obtenerDescuentos()) {
            if (!otro.getIdDescuento().equals(idDescuento) && otro.getProducto().getIdProducto().equals(idProducto) && otro.getVigencia().isAfter(LocalDateTime.now())) {

                throw new IllegalArgumentException(
                        "El producto ya tiene un descuento vigente."
                );
            }
        }

        descuento.setProducto(producto);
        descuento.setPorcentajeDescuento(porcentaje);
        descuento.setVigencia(vigencia);

        actualizarDescuento(descuento);
    }

    public void eliminarDescuento(Integer idDescuento) {
        if (idDescuento == null) {
            throw new IllegalArgumentException("Selecciona un descuento para eliminar.");
        }

        ServiceLocator.getInstanceDescuentoDAO().eliminarPorId(idDescuento);
    }


}

