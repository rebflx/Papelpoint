package mx.desarrollo.ui;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import mx.desarrollo.entity.Unidad_aprendizaje;
import mx.desarrollo.facade.FacadeUnidad;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class UnidadABean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUnidad;
    private String nombre;
    private int horasClase;
    private int horasTaller;
    private int horasLaboratorio;
    private List<Unidad_aprendizaje> unidadesFiltradas;
    private Unidad_aprendizaje unidadSeleccionada;
    private String filtroNombre;



    //eliminar unidad

    public void eliminarUnidad(){
        if(unidadSeleccionada != null) {
            try {
                FacadeUnidad facade = ServiceFacadeLocator.getInstanceFacadeUnidad();
                facade.eliminarUnidad(unidadSeleccionada);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito", "Unidad de aprendizaje eliminada correctamente"));
                unidadSeleccionada = null;
            } catch (IllegalArgumentException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            }
        }
        limpiarCampos();

    }



    //obtener los cambios realizados a la unidad
    public void setNuevosAtributos(){
            unidadSeleccionada.setNombre(nombre);
            unidadSeleccionada.setHorasClase(horasClase);
            unidadSeleccionada.setHorasTaller(horasTaller);
            unidadSeleccionada.setHorasLaboratorio(horasLaboratorio);
        }


    public void cargarUnidadSeleccionada() {
        if(unidadSeleccionada != null){
            nombre = unidadSeleccionada.getNombre();
            horasClase = unidadSeleccionada.getHorasClase();
            horasTaller = unidadSeleccionada.getHorasTaller();
            horasLaboratorio = unidadSeleccionada.getHorasLaboratorio();
            idUnidad = unidadSeleccionada.getIdUnidad();
        }
    }

    //Modificar Unidad
    public void modificarUnidad() {
        if (unidadSeleccionada != null) {
            setNuevosAtributos();

            FacadeUnidad facade = ServiceFacadeLocator.getInstanceFacadeUnidad();
            facade.modificarUnidad(unidadSeleccionada);
        }
        limpiarCampos();
    }


    //crear objeto
public void guardarUnidad(){
    Unidad_aprendizaje unidad = new Unidad_aprendizaje();

    unidad.setNombre(nombre);
    unidad.setHorasClase(horasClase);
    unidad.setHorasTaller(horasTaller);
    unidad.setHorasLaboratorio(horasLaboratorio);

    FacadeUnidad facade = ServiceFacadeLocator.getInstanceFacadeUnidad();
    //llamar a facade
    facade.agregarUnidad(unidad);
    limpiarCampos();
}

    //Consultar lista de unidades
    public List<Unidad_aprendizaje> getUnidades(){
        FacadeUnidad facade = ServiceFacadeLocator.getInstanceFacadeUnidad();
    //llamar a facade
    return facade.obtenerUnidades();
    }



    // Comparar los nombres de las unidades con el filtro de la lista
    public List<Unidad_aprendizaje> buscarUnidades() {
        //creamos una lista con las unidades y una nueva para hacerla comparacion
        List<Unidad_aprendizaje> unidades = getUnidades();
        List<Unidad_aprendizaje> unidadesFiltradas = new ArrayList<>();

        // Validamos el filtro
        if (filtroNombre == null || filtroNombre.isEmpty()) {
            return unidades;
        }
        // Verificamos la lista completa de unidades
        for (Unidad_aprendizaje unidad : unidades) {
            if (unidad.getNombre().toLowerCase().contains(filtroNombre.toLowerCase())) {
                unidadesFiltradas.add(unidad);
            }
        }
        return unidadesFiltradas;
    }


    private void limpiarCampos() {
        nombre = "";
        horasClase = 0;
        horasTaller = 0;
        horasLaboratorio = 0;

        unidadSeleccionada = null;
    }



    //GETTERS Y SETTERS
    public int getIdUnidad() {return idUnidad;}
    public void setIdUnidad(int idUnidad) {this.idUnidad = idUnidad;}

    public int getHorasClase() {return horasClase;}
    public void setHorasClase(int horasClase) {this.horasClase = horasClase;}

    public int getHorasTaller() {return horasTaller;}
    public void setHorasTaller(int horasTaller) {this.horasTaller = horasTaller;}

    public int getHorasLab() {return horasLaboratorio;}
    public void setHorasLab(int horasLaboratorio) {this.horasLaboratorio = horasLaboratorio;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public Unidad_aprendizaje getUnidadSeleccionada() {return unidadSeleccionada;}
    public void setUnidadSeleccionada(Unidad_aprendizaje unidad) {this.unidadSeleccionada = unidad;}

    public void setFiltroNombre(String filtroNombre) {this.filtroNombre = filtroNombre;}
    public String getFiltroNombre() {return filtroNombre;}



}
