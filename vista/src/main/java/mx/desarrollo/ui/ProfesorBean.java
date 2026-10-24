package mx.desarrollo.ui;

import mx.desarrollo.entity.Profesor;
import mx.desarrollo.facade.FacadeProfesor;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.util.Comparator;

import java.util.List;

@Named
@RequestScoped
public class ProfesorBean {


    private  String nombre;
    private  String apellidoPaterno;
    private  String apellidoMaterno;
    private  String rfc;
    private  String idProfesor;


    //crear objeto
    public void guardarProfesor(){
        Profesor profesor = new Profesor();

        profesor.setNombre(nombre);
        profesor.setApellidoP(apellidoPaterno);
        profesor.setApellidoM(apellidoMaterno);
        profesor.setRfc(rfc);

        FacadeProfesor facade = ServiceFacadeLocator.getInstanceFacadeProfesor();

        //llamar a facade
        facade.guardarProfesor(profesor);
        limpiarCampos();
    }

    //Consultar lista de profesores
    public List<Profesor> getProfesores(){
        FacadeProfesor facade = ServiceFacadeLocator.getInstanceFacadeProfesor();

        return facade.obtenerProfesoresOrdenados();
    }

    private void limpiarCampos() {
        nombre = "";
        apellidoMaterno = "";
        apellidoPaterno = "";
        rfc = "";
    }

    //GETTERS Y SETTERS

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getRfc() {
        return rfc;
    }
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

}
