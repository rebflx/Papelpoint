package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateProducto;
import mx.desarrollo.entity.Producto;

import java.util.List;

public class FacadeProducto {

    private final DelegateProducto delegateProducto;

    public FacadeProducto() {
        this.delegateProducto = new DelegateProducto();
    }

    public void guardarProducto(Producto prod) {
        delegateProducto.saveProducto(prod);
    }

    public List<Producto> obtenerProductos() {
        return delegateProducto.getProductos();
    }

    public List<Producto> obtenerProductosActualizados() {
        return delegateProducto.getProductosActualizados();
    }

}
