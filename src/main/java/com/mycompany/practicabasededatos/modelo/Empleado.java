package com.mycompany.practicabasededatos.modelo;

import java.sql.Date;

public class Empleado extends Persona {
    
    private int id_empleado;
    private String lugar_trabajo;
    private double salario_bruto;
    private EstadoLaboral estadolaboral;
    private Date fecha_contratacion;

    // Constructor completo
    public Empleado(int id_persona, String lugar_trabajo, double salario_bruto, EstadoLaboral estadolaboral, Date fecha_contratacion) {
        super(id_persona);
        this.lugar_trabajo = lugar_trabajo;
        this.salario_bruto = salario_bruto;
        this.estadolaboral = estadolaboral;
        this.fecha_contratacion = fecha_contratacion;
    }

    public Empleado(int id_persona, String documento, String direccion, String email, 
    double salario_bruto, String lugarTrabajo, EstadoLaboral estadoLaboral) {
super(id_persona, documento, direccion, email);  // Llamar al constructor de la clase base (Persona)
this.salario_bruto = salario_bruto;
this.lugar_trabajo = lugarTrabajo;
this.estadolaboral = estadoLaboral;
}

    // Constructor vacío
    public Empleado() {
    }

    // Getters
    public int getId_empleado() {
        return id_empleado;
    }

    public String getLugar_trabajo() {
        return lugar_trabajo;
    }

    public double getSalario_bruto() {
        return salario_bruto;
    }

    public EstadoLaboral getEstadolaboral() {
        return estadolaboral;
    }

    public Date getFecha_contratacion() {
        return fecha_contratacion;
    }

    // Setters
    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public void setLugar_trabajo(String lugar_trabajo) {
        this.lugar_trabajo = lugar_trabajo;
    }

    public void setSalario_bruto(double salario_bruto) {
        this.salario_bruto = salario_bruto;
    }

    public void setEstadolaboral(EstadoLaboral estadolaboral) {
        this.estadolaboral = estadolaboral;
    }

    public void setFecha_contratacion(Date fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }

@Override
public String toString() {
    return (getDocumento_identidad() != null ? getDocumento_identidad() : "Sin Documento") + " - " +
           (getApellido() != null ? getApellido() : "Apellido Desconocido") + ", " +
           (getNombre() != null ? getNombre() : "Nombre Desconocido");
}


}
