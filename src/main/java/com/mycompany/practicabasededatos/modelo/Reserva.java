package com.mycompany.practicabasededatos.modelo;

import java.time.LocalDate;

public class Reserva {
    private int id_reserva;
    private TipoReserva tipo_reserva;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private LocalDate fecha_reserva;
    private double precio_total;
    private double tipo_iva; // Nuevo atributo
    private int id_cliente;

    // Constructor completo
    public Reserva(int id_reserva, TipoReserva tipo_reserva, LocalDate fecha_inicio, LocalDate fecha_fin, LocalDate fecha_reserva, double precio_total, double tipo_iva, int id_cliente) {
        this.id_reserva = id_reserva;
        this.tipo_reserva = tipo_reserva;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_reserva = fecha_reserva;
        this.precio_total = precio_total;
        this.tipo_iva = tipo_iva; // Inicialización del nuevo atributo
        this.id_cliente = id_cliente;
    }

    // Getters y Setters
    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public TipoReserva getTipo_reserva() {
        return tipo_reserva;
    }

    public void setTipo_reserva(TipoReserva tipo_reserva) {
        this.tipo_reserva = tipo_reserva;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public LocalDate getFecha_reserva() {
        return fecha_reserva;
    }

    public void setFecha_reserva(LocalDate fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public double getTipo_iva() {
        return tipo_iva;
    }

    public void setTipo_iva(double tipo_iva) {
        this.tipo_iva = tipo_iva;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }
}
