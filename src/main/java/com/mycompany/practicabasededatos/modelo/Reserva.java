package com.mycompany.practicabasededatos.modelo;

import java.time.LocalDate;

public class Reserva {
    private int id_reserva;
    private TipoReserva tipo_reserva;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private LocalDate fecha_reserva;
    private double precio_total;
    private String documento_identidad;
    private int id_cliente;
    private int id_habitacion;
    private String estado;
    private String numeroHabitacion; // Nuevo campo para el número de habitación

    // Constructor completo
    public Reserva(int id_reserva, TipoReserva tipo_reserva, LocalDate fecha_inicio, LocalDate fecha_fin,
            LocalDate fecha_reserva, double precio_total, int id_cliente, int id_habitacion,
            String estado, String numeroHabitacion) {
        this.id_reserva = id_reserva;
        this.tipo_reserva = tipo_reserva;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_reserva = fecha_reserva;
        this.precio_total = precio_total;
        this.id_cliente = id_cliente;
        this.id_habitacion = id_habitacion;
        this.estado = estado;
        this.numeroHabitacion = numeroHabitacion;
    }
    public Reserva(int id_reserva, TipoReserva tipo_reserva, LocalDate fecha_inicio, LocalDate fecha_fin,
    LocalDate fecha_reserva, double precio_total, String documento_identidad, int id_habitacion,
    String estado, String numeroHabitacion) {
this.id_reserva = id_reserva;
this.tipo_reserva = tipo_reserva;
this.fecha_inicio = fecha_inicio;
this.fecha_fin = fecha_fin;
this.fecha_reserva = fecha_reserva;
this.precio_total = precio_total;
this.documento_identidad = documento_identidad;
this.id_habitacion = id_habitacion;
this.estado = estado;
this.numeroHabitacion = numeroHabitacion;
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

    public String getDocumento_identidad() {
        return documento_identidad;
    }

    public void setDocumento_identidad(String documento_identidad) {
        this.documento_identidad = documento_identidad;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

}
