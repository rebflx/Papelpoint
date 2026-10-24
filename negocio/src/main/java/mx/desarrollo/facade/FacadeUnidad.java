package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateAsignacion;
import mx.desarrollo.delegate.DelegateUnidadAprendizaje;
import mx.desarrollo.entity.Asignacion;
import mx.desarrollo.entity.Unidad_aprendizaje;

import java.util.List;

public class FacadeUnidad {
    private final DelegateUnidadAprendizaje delegateUnidad;

    public FacadeUnidad() {
        this.delegateUnidad = new DelegateUnidadAprendizaje();
    }

    public void agregarUnidad(Unidad_aprendizaje unidad) {
        delegateUnidad.saveUnidadAprendizaje(unidad);
        System.out.println("facade: agregarUnidad");

    }

    public void eliminarUnidad(Unidad_aprendizaje unidad) {
        //verifica que la unidad no este asignada an nadie
        DelegateAsignacion delegateAsignacion = new DelegateAsignacion();
        List<Asignacion> asignaciones = delegateAsignacion.buscarPorUnidad(unidad.getIdUnidad());

        if(!asignaciones.isEmpty()){
            throw new IllegalArgumentException("No se puede eliminar: La unidad ya esta asignada a un profesor");
        }

        delegateUnidad.eliminarUnidad(unidad);
    }

    public void modificarUnidad(Unidad_aprendizaje unidad) {
        delegateUnidad.modificarUnidad(unidad);
    }

    public List<Unidad_aprendizaje> obtenerUnidades() {
        return delegateUnidad.getUnidadesAprendizaje();
    }

}
