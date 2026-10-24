package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CorteCaja")
public class CorteCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCorte")
    private Integer idCorte;

    @DecimalMin("0.00")
    @Column(name = "totalCalculado", precision = 10, scale = 2)
    private Double totalCalculado;

    @Column(name = "fechaCierre")
    private LocalDateTime fechaCierre;

    @NotNull
    @Column(name = "fechaInicio", nullable = false)
    private LocalDateTime fechaInicio;

    @DecimalMin("0.00")
    @Column(name = "totalContado", precision = 10, scale = 2)
    private Double totalContado;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public CorteCaja() {
    }

    public Integer getIdCorte() {
        return idCorte;
    }

    public void setIdCorte(Integer idCorte) {
        this.idCorte = idCorte;
    }

    public Double getTotalCalculado() {
        return totalCalculado;
    }

    public void setTotalCalculado(Double totalCalculado) {
        this.totalCalculado = totalCalculado;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Double getTotalContado() {
        return totalContado;
    }

    public void setTotalContado(Double totalContado) {
        this.totalContado = totalContado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
