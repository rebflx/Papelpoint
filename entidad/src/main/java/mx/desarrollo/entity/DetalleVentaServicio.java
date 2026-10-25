package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DetalleVentaServicio")
public class DetalleVentaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalleServicio")
    private Integer idDetalleServicio;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idVenta", nullable = false)
    private Venta venta;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idServicio", nullable = false)
    private Servicio servicio;

    @NotNull
    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "precioUnitario")
    private Double precioUnitario;

    public DetalleVentaServicio() {
    }

    public Integer getIdDetalleServicio() {
        return idDetalleServicio;
    }

    public void setIdDetalleServicio(Integer idDetalleServicio) {
        this.idDetalleServicio = idDetalleServicio;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

}
