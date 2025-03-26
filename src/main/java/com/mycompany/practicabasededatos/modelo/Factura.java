package com.mycompany.practicabasededatos.modelo;

import java.time.LocalDate;

public class Factura {
    private int id_factura;
    private LocalDate fecha_emision;
    private String metodo_pago;
    private double base_imponible;
    private double iva;
    private double total;
    private int id_reserva;

    public Factura(LocalDate fecha_emision, String metodo_pago, double base_imponible, double iva, double total, int id_reserva) {
        this.fecha_emision = fecha_emision;
        this.metodo_pago = metodo_pago;
        this.base_imponible = base_imponible;
        this.iva = iva;
        this.total = total;
        this.id_reserva = id_reserva;
    }

    public Factura(int id_factura, LocalDate fecha_emision, String metodo_pago, double base_imponible, double iva, double total, int id_reserva) {
        this.id_factura = id_factura;
        this.fecha_emision = fecha_emision;
        this.metodo_pago = metodo_pago;
        this.base_imponible = base_imponible;
        this.iva = iva;
        this.total = total;
        this.id_reserva = id_reserva;
    }

    // Getters y Setters
    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public LocalDate getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDate fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public double getBase_imponible() {
        return base_imponible;
    }

    public void setBase_imponible(double base_imponible) {
        this.base_imponible = base_imponible;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }
}