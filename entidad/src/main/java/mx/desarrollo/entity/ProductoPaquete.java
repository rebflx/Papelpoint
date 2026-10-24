package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ProductoPaquete", uniqueConstraints = @UniqueConstraint(columnNames = {"idPaquete", "idProducto"}))
public class ProductoPaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProductoPaquete")
    private Integer idProductoPaquete;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idPaquete", nullable = false)
    private Paquete paquete;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    @NotNull
    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    public ProductoPaquete() {
    }

    public Integer getIdProductoPaquete() {
        return idProductoPaquete;
    }

    public void setIdProductoPaquete(Integer idProductoPaquete) {
        this.idProductoPaquete = idProductoPaquete;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
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

}
