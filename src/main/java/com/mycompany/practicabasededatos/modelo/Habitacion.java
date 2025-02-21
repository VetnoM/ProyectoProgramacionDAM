/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practicabasededatos.modelo;

/**
 *
 * @author eric_
 */
public class Habitacion {
    private int id_habitacion;
    private String numero_habitacion;
    private TipoHabitacion tipo;
    private EstadoHabitacion estado;
    private String descripcion;
    private double precio_noche_ad;
    private double precio_noche_mp;
    private int capacidad;
//constructor
    public Habitacion(int id_habitacion, String numero_habitacion, TipoHabitacion tipo, EstadoHabitacion estado, String descripcion, double precio_noche_ad, double pprecio_noche_mp, int capacidad) {
        this.id_habitacion = id_habitacion;
        this.numero_habitacion = numero_habitacion;
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
        this.precio_noche_ad = precio_noche_ad;
        this.precio_noche_mp = pprecio_noche_mp;
        this.capacidad = capacidad;
    }

    public Habitacion() {
    }
    
    //constructor para mostrar las habitaicones
     public Habitacion(int id_habitacion, String numero_habitacion, String tipo, int capacidad, EstadoHabitacion estado, String descripcion, double precio_noche_ad, double precio_noche_mp) {
        throw new UnsupportedOperationException("No soportado Habitacion"); 
    }
    //getter

   

    public int getId_habitacion() {
        return id_habitacion;
    }

    public String getNumero_habitacion() {
        return numero_habitacion;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio_noche_ad() {
        return precio_noche_ad;
    }

    public double getPrecio_noche_mp() {
        return precio_noche_mp;
    }

    public int getCapacidad() {
        return capacidad;
    }
    
    //setter

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public void setNumero_habitacion(String numero_habitacion) {
        this.numero_habitacion = numero_habitacion;
    }

    public void setTipo(TipoHabitacion tipo) {
        this.tipo = tipo;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio_noche_ad(double precio_noche_ad) {
        this.precio_noche_ad = precio_noche_ad;
    }

    public void setPrecio_noche_mp(double pprecio_noche_mp) {
        this.precio_noche_mp = pprecio_noche_mp;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

  
}
