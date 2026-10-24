package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Respaldo")
public class Respaldo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRespaldo")
    private Integer idRespaldo;

    @NotNull
    @Column(name = "fechaRespaldo", nullable = false)
    private LocalDateTime fechaRespaldo;

    @NotBlank
    @Size(max = 500)
    @Column(name = "rutaArchivo", length = 500, nullable = false)
    private String rutaArchivo;

    @NotBlank
    @Size(max = 30)
    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    public Respaldo() {
    }

    public Integer getIdRespaldo() {
        return idRespaldo;
    }

    public void setIdRespaldo(Integer idRespaldo) {
        this.idRespaldo = idRespaldo;
    }

    public LocalDateTime getFechaRespaldo() {
        return fechaRespaldo;
    }

    public void setFechaRespaldo(LocalDateTime fechaRespaldo) {
        this.fechaRespaldo = fechaRespaldo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
