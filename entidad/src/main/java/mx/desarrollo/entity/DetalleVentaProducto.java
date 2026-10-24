package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DetalleVentaProducto")
public class DetalleVentaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalleProducto")
    private Integer idDetalleProducto;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idVenta", nullable = false)
    private Venta venta;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    @NotNull
    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "precioUnitario", precision = 10, scale = 2, nullable = false)
    private Double precioUnitario;

    public DetalleVentaProducto() {
    }

    public Integer getIdDetalleProducto() {
        return idDetalleProducto;
    }

    public void setIdDetalleProducto(Integer idDetalleProducto) {
        this.idDetalleProducto = idDetalleProducto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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
