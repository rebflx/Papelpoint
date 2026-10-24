package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
@Entity
@Table(name = "asignacion")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAsignacion", nullable = false)
    private Integer idAsignacion;
    /****/

    @NotNull
    @Column(name = "idProfesor", nullable = false)
    private Integer idProfesor;


    @NotNull
    @Column(name = "idUnidadAprendizaje", nullable = false)
    private Integer idUnidadAprendizaje;

    @NotNull
    @Column(name = "diaSemana", nullable = false)
    private String diaSemana;

    @NotNull
    @Column(name = "horaInicio", nullable = false)
    private LocalTime horaInicio;


    @NotNull
    @Column(name = "horaFin", nullable = false)
    private LocalTime horaFin;


    public Integer getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(Integer idAsignacion) {
        this.idAsignacion = idAsignacion;
    }
    public Integer getIdUnidadAprendizaje() {
        return idUnidadAprendizaje;
    }

    public void setIdUnidadAprendizaje(Integer idUnidad) {
        this.idUnidadAprendizaje = idUnidad;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }


    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }



}