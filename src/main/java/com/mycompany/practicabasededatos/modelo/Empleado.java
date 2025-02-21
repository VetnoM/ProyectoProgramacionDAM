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
public class Empleado extends Persona{
    
    private int id_empleado;
    private String lugar_trabajo;
    private double salario_bruto;
    private EstadoLaboral estadolaboral;
    private Date fecha_contratacion;
//constructor

    public Empleado(int id_empleado, String lugar_trabajo, double salario_bruto, EstadoLaboral estadolaboral, Date fecha_contratacion, int id_persona, String documento_identidad, String nombre, String apellido, Date fecha_nacimiento, String telefono, String email, String direccion) {
        super(id_persona, documento_identidad, nombre, apellido, fecha_nacimiento, telefono, email, direccion);
        this.id_empleado = id_empleado;
        this.lugar_trabajo = lugar_trabajo;
        this.salario_bruto = salario_bruto;
        this.estadolaboral = estadolaboral;
        this.fecha_contratacion = fecha_contratacion;
    }
//constructor vacio
    public Empleado() {
    }
    
    //constructor para mostrar los datos
    public Empleado(int aInt, String string, String string0, String string1, double aDouble, String string2, Date date) {
        throw new UnsupportedOperationException("No soportado Empleado"); 
    }


//getter

 
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
    
    //setter

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
    

    
    
}
