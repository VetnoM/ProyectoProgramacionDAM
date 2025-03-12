package com.mycompany.practicabasededatos.modelo;

import com.mycompany.practicabasededatos.database.ClienteDAO;
import com.mycompany.practicabasededatos.database.DatabaseConnection;
import com.mycompany.practicabasededatos.database.EmpleadoDAO;
import com.mycompany.practicabasededatos.database.PersonaDAO;
import com.mycompany.practicabasededatos.database.TareaDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Modelo {

    private static final Logger LOGGER = Logger.getLogger(Modelo.class.getName());

    private PersonaDAO personaDAO = new PersonaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private TareaDAO tareaDAO = new TareaDAO();

    // Obtener tareas pendientes
    public LinkedList<Tarea> obtenerTareasPendientes() {
        return tareaDAO.obtenerTareasPendientes();
    }

    // Obtener tareas en proceso
    public LinkedList<Tarea> obtenerTareasEnProceso() {
        return tareaDAO.obtenerTareasEnProceso();
    }

    // Método para insertar una persona
    public int insertarPersona(Persona persona) {
        return personaDAO.insertarPersona(persona);
    }

    // Método para insertar un cliente
    public void insertarCliente(int idPersona, Cliente cliente) {
        try {
            if (idPersona != -1) {
                cliente.setId_persona(idPersona);
                clienteDAO.insertarCliente(cliente);
            } else {
                LOGGER.log(Level.SEVERE, "Error al insertar la persona para el cliente");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al enviar los datos al clienteDAO", ex);
        }
    }

    // Método para insertar un empleado
    public void insertarEmpleado(int idPersona, Empleado empleado) {
        try {
            if (idPersona != -1) {
                empleadoDAO.insertarEmpleado(idPersona, empleado);
            } else {
                LOGGER.log(Level.SEVERE, "Error al insertar la persona para el empleado");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al enviar los datos al empleadoDAO", ex);
        }
    }

    // Método para obtener personas
    public ArrayList<Persona> obtenerPersonas() {
        return personaDAO.obtenerPersonas();
    }

    // Método para cargar la lista de personas
    public void cargarListaPersonas(ListView<String> listPersonas) {
        ArrayList<Persona> personas = obtenerPersonas();
        listPersonas.getItems().clear();
        for (Persona p : personas) {
            listPersonas.getItems().add(p.getNombre() + " " + p.getApellido());
        }
    }
    // APARTADO DE TAREAS


      // Método para crear una tarea
      public int crearTarea(String descripcion, Date fechaCreacion) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return tareaDAO.crearTarea(descripcion, fechaCreacion);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear la tarea", e);
        }
        return -1;
    }

    // Método para crear y asignar una tarea
    public int crearYAsignarTarea(String descripcion, Date fechaEjecucion, String documentoEmpleado, Date fechaCreacion) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            int idTarea = tareaDAO.crearTarea(descripcion, fechaEjecucion);
            if (idTarea > 0) {
                boolean asignada = tareaDAO.asignarTarea(documentoEmpleado, idTarea);
                if (asignada) {
                    return idTarea;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear y asignar la tarea", e);
        }
        return -1;
    }

    // Método para obtener la lista de tareas
    public ObservableList<Tarea> obtenerTareas() {
        try {
            List<Tarea> listaTareas = tareaDAO.obtenerTareas();
            return FXCollections.observableArrayList(listaTareas);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener las tareas", e);
        }
        return FXCollections.observableArrayList();
    }

    // Método para actualizar una tarea
    public boolean actualizarTarea(Tarea tarea) {
        try {
            tareaDAO.actualizarTarea(tarea);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar la tarea", e);
        }
        return false;
    }

    // Método para eliminar una tarea
    public boolean eliminarTarea(int idTarea) {
        try {
            tareaDAO.eliminarTarea(idTarea);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la tarea", e);
        }
        return false;
    }

    // Método para obtener las tareas clasificadas
    public LinkedList<Tarea> obtenerTareasPendientesYEnProceso() {
        LinkedList<Tarea> tareasPendientes = new LinkedList<>();
        LinkedList<Tarea> tareasEnProceso = new LinkedList<>();
        try {
            List<Tarea> todasTareas = tareaDAO.obtenerTareas();
            for (Tarea tarea : todasTareas) {
                if (tarea.getEstadoTarea() == EstadoTarea.PENDIENTE) {
                    tareasPendientes.add(tarea);
                } else if (tarea.getEstadoTarea() == EstadoTarea.EN_PROCESO) {
                    tareasEnProceso.add(tarea);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener las tareas", e);
        }

        LinkedList<Tarea> tareasClasificadas = new LinkedList<>();
        tareasClasificadas.addAll(tareasPendientes);
        tareasClasificadas.addAll(tareasEnProceso);

        return tareasClasificadas;
    }

    // Método para obtener la lista de empleados
    public List<Empleado> obtenerEmpleados() {
        try {
            return empleadoDAO.obtenerEmpleados();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los empleados", e);
            return new ArrayList<>();
        }
    }

    // Método para asignar una tarea a un empleado
    public boolean asignarTareaAEmpleado(Tarea tarea, Empleado empleado) {
        return tareaDAO.asignarTareaAEmpleado(tarea, empleado);
    }
}