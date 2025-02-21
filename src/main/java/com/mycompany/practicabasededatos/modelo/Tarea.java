package com.mycompany.practicabasededatos.modelo;

import java.sql.Date;

public class Tarea {
    private int id_tarea;
    private String descripcion;
    private EstadoTarea estadoTarea;
    private Date fecha_creacion;
    private Date fecha_ejecucion;

    // Constructor corregido
    public Tarea(int id_tarea, String descripcion, EstadoTarea estadoTarea, Date fecha_creacion, Date fecha_ejecucion) {
        this.id_tarea = id_tarea;
        this.descripcion = descripcion;
        this.estadoTarea = estadoTarea;
        this.fecha_creacion = fecha_creacion;
        this.fecha_ejecucion = fecha_ejecucion;
    }

    public Tarea() {
    }
    
    // Getters
    public int getId_tarea() {
        return id_tarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoTarea getEstadoTarea() {
        return estadoTarea;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public Date getFecha_ejecucion() {
        return fecha_ejecucion;
    }

    // Setters
    public void setId_tarea(int id_tarea) {
        this.id_tarea = id_tarea;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstadoTarea(EstadoTarea estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public void setFecha_ejecucion(Date fecha_ejecucion) {
        this.fecha_ejecucion = fecha_ejecucion;
    }
}
