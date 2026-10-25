package mx.desarrollo.integration;

import mx.desarrollo.facade.FacadeProducto;
import mx.desarrollo.facade.FacadeUsuario;

public class ServiceFacadeLocator {

    private static FacadeUsuario facadeUsuario;
    private static FacadeProducto facadeProducto;

    public static FacadeUsuario getInstanceFacadeUsuario() {
        if (facadeUsuario == null) {
            facadeUsuario = new FacadeUsuario();
        }
        return facadeUsuario;
    }

    public static FacadeProducto getInstanceFacadeProducto() {
        if (facadeProducto == null) {
            facadeProducto = new FacadeProducto();
            return facadeProducto;
        } else {
            return facadeProducto;
        }

    }
}