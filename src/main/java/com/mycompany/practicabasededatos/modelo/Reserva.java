
package com.mycompany.practicabasededatos.modelo;

import java.sql.Date;


public class Reserva {
    
    private int id_reserva;
    private TipoReserva tipoReserva;
    private Date fecha_inicio;
    private Date fecha_fin;
    private Date fecha_reserva;
    private double precio_total;
    private int id_cliente;
    private int id_habitacion;
//constructor
    public Reserva(int id_reserva, TipoReserva tipoReserva, Date fecha_inicio, Date fecha_fin, Date fecha_reserva, double precio_total, int id_cliente, int id_habitacion) {
        this.id_reserva = id_reserva;
        this.tipoReserva = tipoReserva;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_reserva = fecha_reserva;
        this.precio_total = precio_total;
        this.id_cliente = id_cliente;
        this.id_habitacion= id_habitacion;
    }

    public Reserva() {
    }
    //constructor para obtener las reservas

    public Reserva(int aInt, String string, Date date, Date date0, Date date1, double aDouble, int aInt0) {
        throw new UnsupportedOperationException("No soportado Reserva"); 
    }
 

    //getter

   
    public int getId_reserva() {
        return id_reserva;
    }

    public TipoReserva getTipoReserva() {
        return tipoReserva;
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

    public int getId_habitacion() {
        return id_habitacion;
    }
    
    //setters

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public void setTipoReserva(TipoReserva tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public void setFecha_reserva(Date fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }


    
    
    
}
