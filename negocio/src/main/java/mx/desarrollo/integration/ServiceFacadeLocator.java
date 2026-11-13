package mx.desarrollo.integration;
import mx.desarrollo.facade.*;

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

    private static FacadeVenta facadeVenta;

    public static FacadeVenta getInstanceFacadeVenta() {
        if (facadeVenta == null) {
            facadeVenta = new FacadeVenta();
        }
        return facadeVenta;
    }

    private static FacadeHistorial facadeHistorial;

    public static FacadeHistorial getInstanceFacadeHistorial() {
        if (facadeHistorial == null) {
            facadeHistorial = new FacadeHistorial();
        }
        return facadeHistorial;
    }


    private static FacadeServicio facadeServicio;

    public static FacadeServicio getInstanceFacadeServicio() {
        if (facadeServicio == null) {
            facadeServicio = new FacadeServicio();
        }

        return facadeServicio;
    }


}