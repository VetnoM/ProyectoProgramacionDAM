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
public class Cliente extends Persona {
    private int id_cliente;
    private Date fecha_registro;
    private String tarjeta_credito;
    private TipoCliente tipoCliente;
// constructores

    public Cliente(int id_cliente, Date fecha_registro, String tarjeta_credito, TipoCliente tipoCliente, int id_persona, String documento_identidad, String nombre, String apellido, Date fecha_nacimiento, String telefono, String email, String direccion) {
        super(id_persona, documento_identidad, nombre, apellido, fecha_nacimiento, telefono, email, direccion);
        this.id_cliente = id_cliente;
        this.fecha_registro = fecha_registro;
        this.tarjeta_credito = tarjeta_credito;
        this.tipoCliente = tipoCliente;
    }

    public Cliente(int id_cliente, Date fecha_registro, String tarjeta_credito, TipoCliente tipoCliente) {
        this.id_cliente = id_cliente;
        this.fecha_registro = fecha_registro;
        this.tarjeta_credito = tarjeta_credito;
        this.tipoCliente = tipoCliente;
    }

//constructor vacio
    public Cliente() {
    }
    
    //constructor para mostrar clientes
    public Cliente(int aInt, String string, String string0, String string1, String string2, Date date) {
        throw new UnsupportedOperationException("No soportado Cliente");
    }
 //getter 



   

    public int getId_cliente() {
        return id_cliente;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public String getTarjeta_credito() {
        return tarjeta_credito;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
    //Setters

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public void setTarjeta_credito(String tarjeta_credito) {
        this.tarjeta_credito = tarjeta_credito;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    @Override
    public String toString() {
        return  super.toString()+"Cliente{" + "id_cliente=" + id_cliente + ", fecha_registro=" + fecha_registro + ", tarjeta_credito=" + tarjeta_credito + ", tipoCliente=" + tipoCliente + '}';
    }
    
    
}
