package mx.desarrollo.delegate;

import mx.desarrollo.entity.Asignacion;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.LocalTime;
import java.util.List;

public class DelegateAsignacion {

    //CRUD DE ASIGNACIONES

    public boolean asignarMateria(Integer idProfesor, Integer idUnidadAprendizaje, String diaSemana, LocalTime horaInicio, LocalTime horaFin){
          List<Asignacion> asignacionesProfesor = ServiceLocator.getInstanceAsignacionDAO().findByOneParameter(idProfesor,"idProfesor");

        for (Asignacion a : asignacionesProfesor) {
            if (a.getDiaSemana().equalsIgnoreCase(diaSemana)) {
                boolean traslape = horaInicio.isBefore(a.getHoraFin());
                if (traslape) {
                    return false;

                }
            }
        }

          Asignacion nuevaAsignacion = new Asignacion();
          nuevaAsignacion.setIdProfesor(idProfesor);
          nuevaAsignacion.setIdUnidadAprendizaje(idUnidadAprendizaje);
          nuevaAsignacion.setDiaSemana(diaSemana);
          nuevaAsignacion.setHoraInicio(horaInicio);
          nuevaAsignacion.setHoraFin(horaFin);

          ServiceLocator.getInstanceAsignacionDAO().save(nuevaAsignacion);
          return true;
    }


    //consultar asignaciones existentes
    public List<Asignacion> getAsignaciones() {
        return ServiceLocator.getInstanceAsignacionDAO().findAll();
    }

    //actualizar
    public void actualizarAsignacion(Asignacion asignacion){
        ServiceLocator.getInstanceAsignacionDAO().update(asignacion);
    }

    //eliminar
    public void eliminarAsignacion(Asignacion asignacion){
        ServiceLocator.getInstanceAsignacionDAO().delete(asignacion);
    }

    public List<Asignacion> buscarPorUnidad(Integer idUnidad){
        return ServiceLocator.getInstanceAsignacionDAO().findByOneParameter(idUnidad, "idUnidadAprendizaje");
    }


}
