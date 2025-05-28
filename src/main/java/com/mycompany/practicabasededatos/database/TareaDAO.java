package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.EstadoTarea;
import com.mycompany.practicabasededatos.modelo.Tarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TareaDAO {

    // Crear una nueva tarea y devolver su ID
    public int crearTarea(String descripcion, Date fechaCreacion, Date fechaEjecucion, String estado)
            throws SQLException {
        String sql = "INSERT INTO tarea (descripcion, fecha_creacion, fecha_ejecucion, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, descripcion);
            stmt.setDate(2, fechaCreacion);

            if (fechaEjecucion != null) {
                stmt.setDate(3, fechaEjecucion);
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            stmt.setString(4, estado);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }
        }
    }

    // Método para obtener tareas asignadas a un empleado
    public List<Tarea> obtenerTareasPorEmpleado(String documentoEmpleado) {
        List<Tarea> tareas = new ArrayList<>();

        String query = "SELECT t.id_tarea, t.descripcion, t.estado, t.fecha_creacion, t.fecha_ejecucion " +
                "FROM tarea t " +
                "JOIN empleado e ON t.id_empleado = e.id_empleado " +
                "JOIN persona p ON e.id_persona = p.id_persona " +
                "WHERE p.documento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, documentoEmpleado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idTarea = rs.getInt("id_tarea");
                    String descripcion = rs.getString("descripcion");
                    String estadoStr = rs.getString("estado");
                    Date fechaCreacion = rs.getDate("fecha_creacion");
                    Date fechaEjecucion = rs.getDate("fecha_ejecucion");

                    EstadoTarea estadoTarea = EstadoTarea.valueOf(estadoStr.toUpperCase());

                    Tarea tarea = new Tarea(idTarea, descripcion, estadoTarea, fechaCreacion, fechaEjecucion);
                    tareas.add(tarea);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tareas;
    }

    // En el DAO (TareaDAO), un método que devuelva empleados con tareas asignadas:
public List<Empleado> obtenerEmpleadosConTareas() throws SQLException {
    List<Empleado> empleadosConTareas = new ArrayList<>();
    String sql = "SELECT DISTINCT e.id_empleado, p.nombre, p.apellido FROM empleado e " +
                 "JOIN persona p ON e.id_persona = p.id_persona " +
                 "JOIN realizar_tarea rt ON e.id_empleado = rt.id_empleado";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Empleado empleado = new Empleado();
            empleado.setId_empleado(rs.getInt("id_empleado"));
            empleado.setNombre(rs.getString("nombre"));
            empleado.setApellido(rs.getString("apellido"));
            empleadosConTareas.add(empleado);
        }
    }
    return empleadosConTareas;
}


    public boolean asignarTareaAEmpleado(Tarea tarea, Empleado empleado) {
        String sql = "INSERT INTO realizar_tarea (id_empleado, id_tarea) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empleado.getId_empleado());
            stmt.setInt(2, tarea.getId_tarea());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tieneTarea(int idTarea, int idEmpleado) {
    String sql = "SELECT COUNT(*) FROM realizar_tarea WHERE id_tarea = ? AND id_empleado = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idTarea);
        stmt.setInt(2, idEmpleado);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    // public boolean asignarTarea(String documentoEmpleado, int idTarea) {
    //     String sql = "INSERT INTO tarea_empleado (id_tarea, id_empleado) " +
    //             "SELECT ?, id_empleado FROM empleado e " +
    //             "JOIN persona p ON e.id_persona = p.id_persona " +
    //             "WHERE p.documento = ?";
    //     try (Connection conn = DatabaseConnection.getConnection();
    //             PreparedStatement stmt = conn.prepareStatement(sql)) {

    //         stmt.setInt(1, idTarea);
    //         stmt.setString(2, documentoEmpleado);
    //         int filasAfectadas = stmt.executeUpdate();
    //         return filasAfectadas > 0;

    //     } catch (SQLException e) {
    //         System.err.println("Error al asignar la tarea: " + e.getMessage());
    //     }
    //     return false;
    // }

    public List<Tarea> obtenerTareas() {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tarea";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Tarea tarea = new Tarea(
                        rs.getInt("id_tarea"),
                        rs.getString("descripcion"),
                        EstadoTarea.valueOf(rs.getString("estado")),
                        rs.getDate("fecha_creacion"),
                        rs.getDate("fecha_ejecucion"));
                tareas.add(tarea);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }

  public List<Tarea> obtenerTareasAsignadasAEmpleado(int idEmpleado) {
    List<Tarea> tareas = new ArrayList<>();
    String sql = "SELECT t.id_tarea, t.descripcion, t.fecha_creacion, t.fecha_ejecucion, " +
                 "rt.estado AS estado_individual " +
                 "FROM tarea t " +
                 "JOIN realizar_tarea rt ON t.id_tarea = rt.id_tarea " +
                 "WHERE rt.id_empleado = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idEmpleado);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Tarea tarea = new Tarea();
            tarea.setId_tarea(rs.getInt("id_tarea"));
            tarea.setDescripcion(rs.getString("descripcion"));
            tarea.setFecha_creacion(rs.getDate("fecha_creacion"));
            tarea.setFecha_ejecucion(rs.getDate("fecha_ejecucion"));

            // Usamos el estado individual de la asignación
            String estadoStr = rs.getString("estado_individual");
            tarea.setEstadoTarea(EstadoTarea.valueOf(estadoStr.toUpperCase()));

            tareas.add(tarea);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return tareas;
}


    public LinkedList<Tarea> obtenerTareasPendientes() {
        LinkedList<Tarea> tareasPendientes = new LinkedList<>();
        String query = "SELECT * FROM tarea WHERE estado = 'PENDIENTE'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarea tarea = new Tarea(
                        rs.getInt("id_tarea"),
                        rs.getString("descripcion"),
                        EstadoTarea.PENDIENTE,
                        rs.getDate("fecha_creacion"),
                        rs.getDate("fecha_ejecucion"));
                tareasPendientes.add(tarea);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tareasPendientes;
    }

    public LinkedList<Tarea> obtenerTareasEnProceso() {
        LinkedList<Tarea> tareasEnProceso = new LinkedList<>();
        String query = "SELECT * FROM tarea WHERE estado = 'EN_PROCESO'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarea tarea = new Tarea(
                        rs.getInt("id_tarea"),
                        rs.getString("descripcion"),
                        EstadoTarea.EN_PROCESO,
                        rs.getDate("fecha_creacion"),
                        rs.getDate("fecha_ejecucion"));
                tareasEnProceso.add(tarea);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tareasEnProceso;
    }

    // Obtener tareas asignadas a un empleado según documento

    // Asignar tarea a empleado insertando en realizar_tarea
    public boolean asignarTareaAEmpleado(int idTarea, int idEmpleado) {
        String sql = "INSERT INTO realizar_tarea (id_empleado, id_tarea) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idTarea);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método auxiliar para obtener id_empleado desde documento
    public int obtenerIdEmpleadoPorDocumento(String documentoEmpleado) throws SQLException {
        String sql = "SELECT e.id_empleado FROM empleado e " +
                "JOIN persona p ON e.id_persona = p.id_persona " +
                "WHERE p.documento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documentoEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_empleado");
                } else {
                    throw new SQLException("Empleado no encontrado con documento: " + documentoEmpleado);
                }
            }
        }
    }

public boolean marcarTareaEmpleadoComoCompletada(int idEmpleado, int idTarea) {
    String updateEstadoIndividual = "UPDATE realizar_tarea SET estado = 'COMPLETADA' " +
                                    "WHERE id_empleado = ? AND id_tarea = ?";
    
    String checkPendientes = "SELECT COUNT(*) FROM realizar_tarea " +
                             "WHERE id_tarea = ? AND estado != 'COMPLETADA'";

    String updateEstadoGlobal = "UPDATE tarea SET estado = 'COMPLETADA', fecha_ejecucion = CURRENT_DATE " +
                                "WHERE id_tarea = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);

        // 1. Marcar tarea como completada por el empleado
        try (PreparedStatement stmt1 = conn.prepareStatement(updateEstadoIndividual)) {
            stmt1.setInt(1, idEmpleado);
            stmt1.setInt(2, idTarea);
            stmt1.executeUpdate();
        }

        // 2. Verificar si todos la completaron
        boolean todosCompletaron = false;
        try (PreparedStatement stmt2 = conn.prepareStatement(checkPendientes)) {
            stmt2.setInt(1, idTarea);
            ResultSet rs = stmt2.executeQuery();
            if (rs.next()) {
                int pendientes = rs.getInt(1);
                todosCompletaron = pendientes == 0;
            }
        }

        // 3. Si todos completaron, actualizar el estado global
        if (todosCompletaron) {
            try (PreparedStatement stmt3 = conn.prepareStatement(updateEstadoGlobal)) {
                stmt3.setInt(1, idTarea);
                stmt3.executeUpdate();
            }
        }

        conn.commit();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean yaTieneAsingado(int idTarea, int idEmpleado) {
    String sql = "SELECT COUNT(*) FROM realizar_tarea WHERE id_tarea = ? AND id_empleado = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idTarea);
        stmt.setInt(2, idEmpleado);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace(); // o log de error
    }
    return false;
}



    public void actualizarTarea(Tarea tarea) throws SQLException {
        String sql = "UPDATE tarea SET descripcion = ?, estado = ?, fecha_creacion = ?, fecha_ejecucion = ? WHERE id_tarea = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarea.getDescripcion());
            stmt.setString(2, tarea.getEstadoTarea().toString());
            stmt.setDate(3, new java.sql.Date(tarea.getFecha_creacion().getTime()));
            stmt.setDate(4, tarea.getFecha_ejecucion() != null ? new java.sql.Date(tarea.getFecha_ejecucion().getTime())
                    : null);
            stmt.setInt(5, tarea.getId_tarea());

            stmt.executeUpdate();
        }
    }

    public void eliminarTarea(int idTarea) throws SQLException {
        String sql = "DELETE FROM tarea WHERE id_tarea = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarea);
            stmt.executeUpdate();
        }
    }
}