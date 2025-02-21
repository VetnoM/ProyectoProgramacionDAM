/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practicabasededatos.modelo;

import java.sql.Date;

/**
 *
 * @author eric_
 */
public class Factura {
    private int id_factura;
    private Date fecha_emision;
    private String metodo_pago;
    private double base_imponible;
    private double iva;
    private double total;
    private int id_reserva;
//constructor
    public Factura(int id_factura, Date fecha_emision, String metodo_pago, double base_imponible, double iva, double total, int id_reserva) {
        this.id_factura = id_factura;
        this.fecha_emision = fecha_emision;
        this.metodo_pago = metodo_pago;
        this.base_imponible = base_imponible;
        this.iva = iva;
        this.total = total;
        this.id_reserva = id_reserva;
    }
    
    //getter

    public int getId_factura() {
        return id_factura;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public double getBase_imponible() {
        return base_imponible;
    }

    public double getIva() {
        return iva;
    }

    public double getTotal() {
        return total;
    }

    public int getId_reserva() {
        return id_reserva;
    }
    //setter

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public void setFecha_emision(Date fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public void setBase_imponible(double base_imponible) {
        this.base_imponible = base_imponible;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    @Override
    public String toString() {
        return "Factura{" + "id_factura=" + id_factura + ", fecha_emision=" + fecha_emision + ", metodo_pago=" + metodo_pago + ", base_imponible=" + base_imponible + ", iva=" + iva + ", total=" + total + ", id_reserva=" + id_reserva + '}';
    }
    
    
    
}
