
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateServicio;
import mx.desarrollo.entity.Servicio;

import java.util.List;

public class FacadeServicio {

    private final DelegateServicio delegateServicio;

    public FacadeServicio() {
        this.delegateServicio = new DelegateServicio();
    }

    public void guardarServicio(Servicio serv) {
        delegateServicio.saveServicio(serv);
    }

    public List<Servicio> obtenerServicios() {
        return delegateServicio.getServicios();
    }

    public List<Servicio> obtenerServiciosActualizados() {
        return delegateServicio.getServiciosActualizados();
    }

    public void modificarServicio(Servicio serv) {
        delegateServicio.modificarServicio(serv);
    }

    public void eliminarServicio(Servicio serv) {
        delegateServicio.eliminarServicio(serv);
    }
}
