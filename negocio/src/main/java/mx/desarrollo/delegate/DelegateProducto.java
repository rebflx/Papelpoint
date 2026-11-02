package mx.desarrollo.delegate;

import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateProducto {

    public void saveProducto(Producto prod) {
        if (!validarUnidad(prod)) {
            throw new IllegalArgumentException("Los datos del producto no son válidos.");
        }
        ServiceLocator.getInstanceProductoDAO().save(prod);
    }

    //consultar unidades existentes
    public List<Producto> getProductos() {
        return ServiceLocator.getInstanceProductoDAO().findAll();
    }

    public void modificarProducto(Producto prod) {
        if (prod.getIdProducto() == null || !validarUnidad(prod)) {
            System.out.println("error validacion: modificarProducto");
            return;
        }
        ServiceLocator.getInstanceProductoDAO().update(prod);
    }

    public void eliminarProducto(Producto prod) {
        if (prod.getIdProducto() == null) {
            return;
        }
        prod.setActivo(false);
        ServiceLocator.getInstanceProductoDAO().update(prod);
    }

    public List<Producto> getProductosActualizados() {
        return ServiceLocator.getInstanceProductoDAO().findAllFresh();
    }


    //validar la unidad
    public boolean validarUnidad(Producto producto) {



        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            return false;
        }

        if (producto.getPrecio() == null || producto.getPrecio() < 0) {
            return false;
        }

        if (producto.getExistencias() == null || producto.getExistencias() < 0) {
            return false;
        }

        if (producto.getStockMinimo() == null || producto.getStockMinimo() < 0) {
            return false;
        }

        return true;
    }
}
