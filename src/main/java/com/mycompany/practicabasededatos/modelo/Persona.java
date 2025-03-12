package com.mycompany.practicabasededatos.modelo;

import java.sql.Date;

public class Persona {

    private int id_persona;
    private String documento_identidad;
    private String nombre;
    private String apellido;
    private Date fecha_nacimiento;
    private String telefono;
    private String email;
    private String direccion;

    // Constructor completo
    public Persona(int id_persona, String documento_identidad, String nombre, String apellido, Date fecha_nacimiento, String telefono, String email, String direccion) {
        this.id_persona = id_persona;
        this.documento_identidad = documento_identidad;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    // Constructor vacío
    public Persona() {
    }

    // Constructor con solo id_persona
    public Persona(int id_persona) {
        this.id_persona = id_persona;
    }

    // Getters y Setters
    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getDocumento_identidad() {
        return documento_identidad;
    }

    public void setDocumento_identidad(String documento_identidad) {
        this.documento_identidad = documento_identidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
