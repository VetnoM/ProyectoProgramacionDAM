/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practicabasededatos.modelo;
import com.mycompany.practicabasededatos.database.ClienteDAO;
import com.mycompany.practicabasededatos.database.EmpleadoDAO;
import com.mycompany.practicabasededatos.database.PersonaDAO;
import java.sql.Date;
import java.util.ArrayList;

public class Modelo {
    private PersonaDAO personaDAO = new PersonaDAO();
    private ClienteDAO clientesDAO = new ClienteDAO();
    private EmpleadoDAO empleadosDAO = new EmpleadoDAO();

    // Método para insertar una persona
    public int insertarPersona(Persona persona) {
        return personaDAO.insertarPersona(persona);
    }

    // Método para insertar un cliente
    public void insertarCliente(int idPersona, String tipoCliente, String tarjetaCredito, Date fechaRegistro) {
        clientesDAO.insertarCliente(idPersona, tipoCliente, tarjetaCredito, fechaRegistro);
    }

    // Método para insertar un empleado
    public void insertarEmpleado(int idPersona, String lugarTrabajo, double salario, String estadoLaboral, Date fechaContratacion) {
        empleadosDAO.insertarEmpleado(idPersona, lugarTrabajo, salario, estadoLaboral, fechaContratacion);
    }

    // Método para obtener la lista de personas
    public ArrayList<Persona> obtenerPersonas() {
        return personaDAO.obtenerPersonas();
    }
    
    private void cargarListaPersonas() {
    // Obtener la lista de personas desde el Modelo
    Modelo modelo = new Modelo();
    ArrayList<Persona> personas = modelo.obtenerPersonas();
    
    // Limpiar el ListView antes de llenarlo
    listPersonas.getItems().clear();
    
    // Llenar el ListView con los nombres de las personas
    for (Persona p : personas) {
        listPersonas.getItems().add(p.getNombre() + " " + p.getApellido());
    }
}

}
