package com.mycompany.practicabasededatos.modelo;

import java.sql.Date;

public class Reserva {
    private int id_reserva;
    private String tipo_reserva;
    private Date fecha_inicio;
    private Date fecha_fin;
    private Date fecha_reserva;
    private double precio_total;
    private int id_cliente;

    public Reserva(int id_reserva, String tipo_reserva, Date fecha_inicio, Date fecha_fin, Date fecha_reserva, double precio_total, int id_cliente) {
        this.id_reserva = id_reserva;
        this.tipo_reserva = tipo_reserva;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_reserva = fecha_reserva;
        this.precio_total = precio_total;
        this.id_cliente = id_cliente;
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public String getTipo_reserva() {
        return tipo_reserva;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public Date getFecha_reserva() {
        return fecha_reserva;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public int getId_cliente() {
        return id_cliente;
    }
}
