package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DetalleVentaPaquete")
public class DetalleVentaPaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetallePaquete")
    private Integer idDetallePaquete;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idVenta", nullable = false)
    private Venta venta;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idPaquete", nullable = false)
    private Paquete paquete;

    @NotNull
    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "precioUnitario")
    private Double precioUnitario;

    public DetalleVentaPaquete() {
    }

    public Integer getIdDetallePaquete() {
        return idDetallePaquete;
    }

    public void setIdDetallePaquete(Integer idDetallePaquete) {
        this.idDetallePaquete = idDetallePaquete;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
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
