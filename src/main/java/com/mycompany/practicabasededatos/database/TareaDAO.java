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
    public int crearTarea(String descripcion, Date fechaCreacion) throws SQLException {
        String sql = "INSERT INTO tareas (descripcion, fecha_creacion) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, descripcion);
            stmt.setDate(2, fechaCreacion);
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

    public boolean asignarTareaAEmpleado(Tarea tarea, Empleado empleado) {
        String query = "UPDATE tarea SET estado = ?, id_empleado = ? WHERE id_tarea = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, EstadoTarea.EN_PROCESO.name());
            ps.setInt(2, empleado.getId_empleado());
            ps.setInt(3, tarea.getId_tarea());

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean asignarTarea(String documentoEmpleado, int idTarea) {
        String sql = "INSERT INTO tarea_empleado (id_tarea, id_empleado) " +
                     "SELECT ?, id_empleado FROM empleado e " +
                     "JOIN persona p ON e.id_persona = p.id_persona " +
                     "WHERE p.documento = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarea);
            stmt.setString(2, documentoEmpleado);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al asignar la tarea: " + e.getMessage());
        }
        return false;
    }

    public List<Tarea> obtenerTareas() throws SQLException {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Tarea tarea = new Tarea(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        EstadoTarea.valueOf(rs.getString("estado")),
                        rs.getDate("fecha_creacion"),
                        rs.getDate("fecha_ejecucion")
                );
                tareas.add(tarea);
            }
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
                        rs.getDate("fecha_ejecucion")
                );
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
                        rs.getDate("fecha_ejecucion")
                );
                tareasEnProceso.add(tarea);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tareasEnProceso;
    }

    public void actualizarTarea(Tarea tarea) throws SQLException {
        String sql = "UPDATE tarea SET descripcion = ?, estado = ?, fecha_creacion = ?, fecha_ejecucion = ? WHERE id_tarea = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarea.getDescripcion());
            stmt.setString(2, tarea.getEstadoTarea().toString());
            stmt.setDate(3, new java.sql.Date(tarea.getFecha_creacion().getTime()));
            stmt.setDate(4, tarea.getFecha_ejecucion() != null ? new java.sql.Date(tarea.getFecha_ejecucion().getTime()) : null);
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