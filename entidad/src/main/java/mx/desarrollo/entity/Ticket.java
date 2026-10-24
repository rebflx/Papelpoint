package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTicket")
    private Integer idTicket;

    @NotNull
    @Column(name = "fechaExpedicion", nullable = false)
    private LocalDateTime fechaExpedicion;

    @NotBlank
    @Size(max = 50)
    @Column(name = "folio", length = 50, nullable = false, unique = true)
    private String folio;

    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "idVenta", nullable = false, unique = true)
    private Venta venta;

    public Ticket() {
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public LocalDateTime getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(LocalDateTime fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

}
