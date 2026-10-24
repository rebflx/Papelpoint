package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVenta")
    private Integer idVenta;

    @NotBlank
    @Size(max = 30)
    @Column(name = "metodoPago", length = 30, nullable = false)
    private String metodoPago;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private Double subtotal;

    @NotBlank
    @Size(max = 30)
    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private Double total;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne()
    @JoinColumn(name = "idCorte")
    private CorteCaja corteCaja;

    public Venta() {
    }

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CorteCaja getCorteCaja() {
        return corteCaja;
    }

    public void setCorteCaja(CorteCaja corteCaja) {
        this.corteCaja = corteCaja;
    }

}
