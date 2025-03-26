package com.mycompany.practicabasededatos.modelo;

// Importa las clases necesarias para manejar la conexión a la base de datos y las clases del modelo
import com.mycompany.practicabasededatos.database.ClienteDAO;
import com.mycompany.practicabasededatos.database.DatabaseConnection;
import com.mycompany.practicabasededatos.database.EmpleadoDAO;
import com.mycompany.practicabasededatos.database.HabitacionDAO;
import com.mycompany.practicabasededatos.database.PersonaDAO;
import com.mycompany.practicabasededatos.database.ReservaDAO;
import com.mycompany.practicabasededatos.database.TareaDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    // Logger para registrar mensajes de error y depuración
    private static final Logger LOGGER = Logger.getLogger(Modelo.class.getName());

    // Instancias de los DAOs para interactuar con la base de datos
    private PersonaDAO personaDAO = new PersonaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private TareaDAO tareaDAO = new TareaDAO();
    private HabitacionDAO habitacionDAO = new HabitacionDAO();
    private ReservaDAO reservaDAO = new ReservaDAO();

    // Método para obtener tareas pendientes
    public LinkedList<Tarea> obtenerTareasPendientes() {
        return tareaDAO.obtenerTareasPendientes();
    }

    // Método para obtener tareas en proceso
    public LinkedList<Tarea> obtenerTareasEnProceso() {
        return tareaDAO.obtenerTareasEnProceso();
    }

    // Método para insertar una persona
    public int insertarPersona(Persona persona) {
        return personaDAO.insertarPersona(persona);
    }
    // Método para obtener personas únicas
    public ObservableList<String> obtenerPersonasUnicas() {
    ObservableList<String> personas = FXCollections.observableArrayList();
    String sql = "SELECT DISTINCT p.documento_identidad, p.nombre, p.apellido, " +
                 "CASE WHEN c.id_cliente IS NOT NULL THEN 'Cliente' ELSE 'Empleado' END AS tipo " +
                 "FROM persona p " +
                 "LEFT JOIN cliente c ON p.id_persona = c.id_persona " +
                 "LEFT JOIN empleado e ON p.id_persona = e.id_persona";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String tipo = rs.getString("tipo");
            String persona = rs.getString("documento_identidad") + " - " +
                             rs.getString("nombre") + " " +
                             rs.getString("apellido") + " (" + tipo + ")";
            personas.add(persona);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return personas;
}
// Método para obtener el tipo de cliente por ID
public String obtenerTipoClientePorId(int idCliente) {
    String sql = "SELECT " +
                 "CASE " +
                 "WHEN EXISTS (SELECT 1 FROM cliente WHERE id_cliente = ?) AND EXISTS (SELECT 1 FROM empleado WHERE id_persona = ?) THEN 'Ambos' " +
                 "WHEN EXISTS (SELECT 1 FROM cliente WHERE id_cliente = ?) THEN 'Cliente' " +
                 "WHEN EXISTS (SELECT 1 FROM empleado WHERE id_persona = ?) THEN 'Empleado' " +
                 "ELSE 'Desconocido' END AS tipo";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idCliente);
        stmt.setInt(2, idCliente);
        stmt.setInt(3, idCliente);
        stmt.setInt(4, idCliente);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("tipo");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "Desconocido"; // Valor predeterminado si no se encuentra el tipo
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
    // Método para obtener clientes
    public ObservableList<Cliente> obtenerClientes() {
        try {
            List<Cliente> listaClientes = clienteDAO.obtenerClientes(); // Llama al DAO para obtener los clientes
            return FXCollections.observableArrayList(listaClientes);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los clientes", e);
        }
        return FXCollections.observableArrayList(); // Devuelve una lista vacía en caso de error
    }

    // Método para obtener el ID de un cliente por DNI
    public int obtenerIdClientePorDni(String dni) {
        try {
            return clienteDAO.obtenerIdClientePorDni(dni); // Llama al DAO para buscar el ID
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener el ID del cliente por DNI", e);
        }
        return -1; // Devuelve -1 si ocurre un error
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



    // Método para obtener la lista de habitaciones
    public ObservableList<Habitacion> obtenerHabitaciones() {
        try {
            List<Habitacion> listaHabitaciones = habitacionDAO.obtenerHabitaciones();
            return FXCollections.observableArrayList(listaHabitaciones);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener las habitaciones", e);
        }
        return FXCollections.observableArrayList();
    }

    // Método para obtener la lista de habitaciones sin reserva
    public ObservableList<String> obtenerHabitacionesSinReserva() {
        ObservableList<String> habitaciones = FXCollections.observableArrayList();
        String sql = "SELECT h.numero_habitacion " +
                     "FROM habitacion h " +
                     "WHERE h.estado = 'DISPONIBLE'";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                habitaciones.add(rs.getString("numero_habitacion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return habitaciones;
    }
    // Método para crear una habitación
    public int crearHabitacion(Habitacion habitacion) {
        try {
            return habitacionDAO.crearHabitacion(habitacion);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear la habitación", e);
        }
        return -1;
    }

    // Método para actualizar una habitación
    public boolean actualizarHabitacion(Habitacion habitacion) {
        try {
            habitacionDAO.actualizarHabitacion(habitacion);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar la habitación", e);
        }
        return false;
    }

    // Método para eliminar una habitación
    public boolean eliminarHabitacion(int idHabitacion) {
        try {
            habitacionDAO.eliminarHabitacion(idHabitacion);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar la habitación", e);
        }
        return false;
    }

    // Método para obtener la lista de reservas
    public ObservableList<Reserva> obtenerReservas() {
        try {
            // Llama al método del DAO para obtener las reservas
            List<Reserva> listaReservas = reservaDAO.obtenerReservas(); // Asegúrate de que este método incluya el atributo 'estado'
            return FXCollections.observableArrayList(listaReservas);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener las reservas desde la base de datos", e);
        }
        return FXCollections.observableArrayList(); // Devuelve una lista vacía en caso de error
    }

    public int obtenerProximoIdReserva() {
        try {
            return reservaDAO.obtenerProximoIdReserva();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener el último ID de reserva", e);
        }
        return 0; // Devuelve 0 si ocurre un error
    }
    // Método para actualizar una reserva
    public boolean actualizarReserva(Reserva reserva) {
        try {
            reservaDAO.actualizarReserva(reserva);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar la reserva", e);
        }
        return false;
    }


        // Método para actualizar una reserva 
        public boolean actualitzarReserva(Reserva reserva) {
            // Lógica para actualizar la reserva en la base de datos
            System.out.println("Reserva actualizada: " + reserva.getId_reserva());
            return true; // Simulación de éxito
        }
        // Método para actualizar el estado de una reserva
        public void actualizarEstadoReserva(int idReserva, String nuevoEstado) throws SQLException {
            String sql = "UPDATE reserva SET estado = ? WHERE id_reserva = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
        
                stmt.setString(1, nuevoEstado);
                stmt.setInt(2, idReserva);
        
                stmt.executeUpdate();
            }
        }
        // Método para actualizar el estado de una habitación
        public boolean actualizarEstadoHabitacion(int idHabitacion, String nuevoEstado) {
            ReservaDAO reservaDAO = new ReservaDAO(); // Instanciar el DAO
            try {
                reservaDAO.actualizarEstadoHabitacion(idHabitacion, nuevoEstado);
                return true; // Si la actualización fue exitosa
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Si hubo un error
            }
        }
        
        // Método para eliminar una reserva 
        public boolean eliminarReserva(int idReserva) {
            // Lógica para eliminar la reserva en la base de datos
            System.out.println("Reserva eliminada: " + idReserva);
            return true; // Simulación de éxito
        }
    
        // Método para crear una reserva

        public boolean crearReserva(Reserva reserva) {
            // Lógica para insertar la reserva en la base de datos
            System.out.println("Reserva creada: " + reserva.getId_reserva());
            return true; // Simulación de éxito
        }
        public ObservableList<String> obtenerPersonasSinReserva() {
            return reservaDAO.obtenerPersonasSinReserva();
        }

        public int obtenerIdClientePorDocumento(String documento) {
            return reservaDAO.obtenerIdClientePorDocumento(documento);
        }

        public int obtenerIdHabitacionPorNumero(String numeroHabitacion) {
            ReservaDAO reservaDAO = new ReservaDAO();
            return reservaDAO.obtenerIdHabitacionPorNumero(numeroHabitacion);
        }
        
        public int crearReservaConExito(Reserva reserva) {
            try {
                // Llama al método existente que devuelve el ID de la reserva creada
                int idReserva = reservaDAO.crearReserva(reserva);
                return idReserva; // Devuelve el ID de la reserva creada
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al crear la reserva", e);
            }
            return -1; // Devuelve -1 si ocurre un error
        }

        public boolean registrarReserva(Reserva reserva) {
            try {
                // Crear la reserva en la base de datos
                int idReserva = crearReservaConExito(reserva);
                if (idReserva > 0) {
                    // Cambiar el estado de la habitación a "OCUPADA"
                    reservaDAO.actualizarEstadoHabitacion(reserva.getId_habitacion(), "OCUPADA");
                    
                    return true;
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al registrar la reserva", e);
            }
            return false;
        }

        

    }


