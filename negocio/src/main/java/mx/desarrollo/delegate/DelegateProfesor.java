package mx.desarrollo.delegate;

import mx.desarrollo.entity.Profesor;
import mx.desarrollo.entity.Unidad_aprendizaje;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;


public class DelegateProfesor {
    public void saveProfesor(Profesor profesor){
        ServiceLocator.getInstanceProfesorDAO().save(profesor);
    }

    public List<Profesor> getProfesor() {
        return ServiceLocator.getInstanceProfesorDAO().findAll();
    }
}