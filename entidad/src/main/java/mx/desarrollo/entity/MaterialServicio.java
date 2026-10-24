package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MaterialServicio", uniqueConstraints = @UniqueConstraint(columnNames = {"idProducto", "idServicio"}))
public class MaterialServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMaterialServicio")
    private Integer idMaterialServicio;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idServicio", nullable = false)
    private Servicio servicio;

    @NotNull
    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    public MaterialServicio() {
    }

    public Integer getIdMaterialServicio() {
        return idMaterialServicio;
    }

    public void setIdMaterialServicio(Integer idMaterialServicio) {
        this.idMaterialServicio = idMaterialServicio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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

}
