package com.mycompany.practicabasededatos.modelo;

import java.sql.Date;

public class Cliente extends Persona {

    private int id_cliente;
    private Date fecha_registro;
    private String tarjeta_credito;
    private TipoCliente tipoCliente;

    // Constructor completo
    public Cliente(int id_persona, Date fecha_registro, String tarjeta_credito, TipoCliente tipoCliente) {
        super(id_persona);
        this.fecha_registro = fecha_registro;
        this.tarjeta_credito = tarjeta_credito;
        this.tipoCliente = tipoCliente;
    }

    public Cliente(int id_persona, String documento, String direccion, String email, TipoCliente tipoCliente) {
        super(id_persona, documento, direccion, email);  // Llamar al constructor de la clase base (Persona)
        this.tipoCliente = tipoCliente;
    }

    // Constructor vacío
    public Cliente() {
    }

    // Getters
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

    // Setters
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
}
