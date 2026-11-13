package mx.desarrollo.delegate;

import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Venta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ResumenCorte {

    public static class Linea {
        private final int transacciones;
        private final double monto;

        Linea(int transacciones, double monto) {
            this.transacciones = transacciones;
            this.monto = monto;
        }

        public int getTransacciones() {
            return transacciones;
        }

        public double getMonto() {
            return monto;
        }
    }

    private Integer idCorte;
    private Usuario usuario;
    private LocalDate dia;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;

    private int numVentas;
    private double totalVentas;
    private final Map<String, Linea> porMetodo = new LinkedHashMap<>();
    private int transaccionesEfectivo;
    private double ventasEfectivo;
    private int numCanceladas;
    private double montoCanceladas;
    private int ventasSinCorte;

    private Double totalContado;
    private final Map<String, Double> totalPorUsuario = new LinkedHashMap<>();
    private List<Venta> ventas = new ArrayList<>();


    public boolean isCerrado() {
        return fechaCierre != null && idCorte != null;
    }


    public Integer getIdCorte() {
        return idCorte;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDia() {
        return dia;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }


    public int getNumVentas() {
        return numVentas;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public Map<String, Linea> getPorMetodo() {
        return Collections.unmodifiableMap(porMetodo);
    }

    public int getTransaccionesEfectivo() {
        return transaccionesEfectivo;
    }

    public double getVentasEfectivo() {
        return ventasEfectivo;
    }

    public int getTransaccionesNoEfectivo() {
        return numVentas - transaccionesEfectivo;
    }

    public double getVentasNoEfectivo() {
        return Math.round((totalVentas - ventasEfectivo) * 100) / 100.0;
    }

    public int getNumCanceladas() {
        return numCanceladas;
    }

    public double getMontoCanceladas() {
        return montoCanceladas;
    }

    public int getVentasSinCorte() {
        return ventasSinCorte;
    }

    public List<Venta> getVentas() {
        return Collections.unmodifiableList(ventas);
    }

    public Map<String, Double> getTotalPorUsuario() {
        return Collections.unmodifiableMap(totalPorUsuario);
    }


    public Double getTotalContado() {
        return totalContado;
    }


    public Double getDiferencia() {
        if (totalContado == null) {
            return null;
        }
        return Math.round((totalContado - totalVentas) * 100) / 100.0;
    }


    public double diferenciaCon(double efectivoContado, double fondoInicial) {
        return Math.round(((efectivoContado - fondoInicial) - ventasEfectivo) * 100) / 100.0;
    }

    public double efectivoEsperadoEnCaja(double fondoInicial) {
        return Math.round((fondoInicial + ventasEfectivo) * 100) / 100.0;
    }


    void setIdCorte(Integer idCorte) {
        this.idCorte = idCorte;
    }

    void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    void setDia(LocalDate dia) {
        this.dia = dia;
    }

    void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    void setTotales(int numVentas, double totalVentas, int transaccionesEfectivo, double ventasEfectivo,
                    int numCanceladas, double montoCanceladas) {
        this.numVentas = numVentas;
        this.totalVentas = totalVentas;
        this.transaccionesEfectivo = transaccionesEfectivo;
        this.ventasEfectivo = ventasEfectivo;
        this.numCanceladas = numCanceladas;
        this.montoCanceladas = montoCanceladas;
    }

    void setVentasSinCorte(int ventasSinCorte) {
        this.ventasSinCorte = ventasSinCorte;
    }

    void agregarMetodo(String metodo, int transacciones, double monto) {
        porMetodo.put(metodo, new Linea(transacciones, monto));
    }

    void agregarTotalUsuario(String nombre, double monto) {
        totalPorUsuario.put(nombre, monto);
    }

    void setTotalContado(Double totalContado) {
        this.totalContado = totalContado;
    }

    void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }
}
