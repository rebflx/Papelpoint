package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateHistorial;
import mx.desarrollo.entity.HistorialModificacionProducto;
import mx.desarrollo.entity.Producto;
import mx.desarrollo.entity.Usuario;

import java.util.List;

public class FacadeHistorial {

    private final DelegateHistorial delegateHistorial;

    public FacadeHistorial() {
        this.delegateHistorial = new DelegateHistorial();
    }

    public List<HistorialModificacionProducto> obtenerHistorial() {
        return delegateHistorial.getHistorial();
    }

    public void registrar(Usuario usuario, Producto producto, String detalles) {
        delegateHistorial.registrar(usuario, producto, detalles);
    }

    public String describirCambios(Producto antes, Producto despues) {
        return delegateHistorial.describirCambios(antes, despues);
    }
}
