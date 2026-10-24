package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer idUsuario;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombrePersona", length = 100, nullable = false)
    private String nombrePersona;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombreUsuario", length = 50, nullable = false, unique = true)
    private String nombreUsuario;

    @NotBlank
    @Size(max = 255)
    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @NotBlank
    @Size(max = 30)
    @Column(name = "rol", length = 30, nullable = false)
    private String rol;

    public Usuario() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
