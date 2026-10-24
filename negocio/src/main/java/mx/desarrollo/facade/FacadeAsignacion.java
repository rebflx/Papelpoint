package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateAsignacion;
import mx.desarrollo.delegate.DelegateProfesor;
import mx.desarrollo.entity.Asignacion;

import java.time.LocalTime;
import java.util.List;

public class FacadeAsignacion {
    private final DelegateAsignacion delegateAsignacion;

    public FacadeAsignacion() {
        this.delegateAsignacion = new DelegateAsignacion();
    }

    public boolean asignarMateria(Integer idProfesor, Integer idUnidadAprendizaje, String diaSemana, LocalTime horaInicio, LocalTime horaFin){
        return delegateAsignacion.asignarMateria(idProfesor, idUnidadAprendizaje, diaSemana, horaInicio, horaFin);
    }

    public List<Asignacion> obtenerAsignaciones(){
        return delegateAsignacion.getAsignaciones();
    }

    public void actualizarAsignacion(Asignacion asignacion){
        delegateAsignacion.actualizarAsignacion(asignacion);
    }

    public void eliminarAsignacion(Asignacion asignacion){
        delegateAsignacion.eliminarAsignacion(asignacion);
    }
}
