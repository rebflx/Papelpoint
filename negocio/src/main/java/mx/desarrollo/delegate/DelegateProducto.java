package mx.desarrollo.delegate;

import mx.desarrollo.entity.Producto;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateProducto {

    public void saveProducto(Producto prod) {
        System.out.println("1. Entró al Delegate");

        //usar metodo
        if (validarUnidad(prod) == true) {
            ServiceLocator.getInstanceProductoDAO().save(prod);
            System.out.println("delegate: save");
        } else {
            System.out.println("error validacion: delegateproducto");
        }

    }

    //consultar unidades existentes
    public List<Producto> getProductos() {
        return ServiceLocator.getInstanceProductoDAO().findAll();
    }

    /*
    //eliminar unidades
    public void eliminarUnidad(Unidad_aprendizaje unidad){
        ServiceLocator.getInstanceUnidadAprendizajeDAO().delete(unidad);
    }

    //modificar unidad
    public void modificarUnidad(Unidad_aprendizaje unidad) {
        ServiceLocator.getInstanceUnidadAprendizajeDAO().update(unidad);
    }
    */


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
