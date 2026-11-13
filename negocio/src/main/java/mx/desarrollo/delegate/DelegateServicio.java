
package mx.desarrollo.delegate;

import mx.desarrollo.entity.Servicio;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateServicio {

    public void saveServicio(Servicio serv) {
        if (!validarServicio(serv)) {
            throw new IllegalArgumentException(
                    "Los datos del servicio no son válidos."
            );
        }

        ServiceLocator.getInstanceServicioDAO().save(serv);
    }

    // Consultar servicios existentes
    public List<Servicio> getServicios() {
        return ServiceLocator.getInstanceServicioDAO().findAll();
    }

    // Modificar un servicio
    public void modificarServicio(Servicio serv) {
        if (serv.getIdServicio() == null ||
                !validarServicio(serv)) {
            System.out.println(
                    "Error de validación: modificarServicio"
            );
            return;
        }

        ServiceLocator.getInstanceServicioDAO().update(serv);
    }

    // Desactivar un servicio
    public void eliminarServicio(Servicio serv) {
        if (serv.getIdServicio() == null) {
            return;
        }

        serv.setDisponible(false);
        ServiceLocator.getInstanceServicioDAO().update(serv);
    }

    // Consultar servicios actualizados
    public List<Servicio> getServiciosActualizados() {
        return ServiceLocator.getInstanceServicioDAO()
                .findAllFresh();
    }

    // Validar los datos del servicio
    public boolean validarServicio(Servicio serv) {

        if (serv == null) {
            return false;
        }

        if (serv.getNombre() == null ||
                serv.getNombre().isBlank()) {
            return false;
        }

        if (serv.getPrecio() == null ||
                !Double.isFinite(serv.getPrecio()) ||
                serv.getPrecio() < 0) {
            return false;
        }

        if (serv.getDisponible() == null) {
            return false;
        }

        return true;
    }
}
