package com.mycompany.practicabasededatos.database;
import com.mycompany.practicabasededatos.modelo.EstadoTarea;
import com.mycompany.practicabasededatos.modelo.Tarea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {
    public void insertarTarea(Tarea tarea) throws SQLException {
        String sql = "INSERT INTO TAREA (descripcion, estado, fecha_creacion, fecha_ejecucion) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarea.getDescripcion());
            stmt.setString(2, tarea.getEstadoTarea().toString());
            stmt.setDate(3, new java.sql.Date(tarea.getFecha_creacion().getTime()));
            stmt.setDate(4, tarea.getFecha_ejecucion() != null ? new java.sql.Date(tarea.getFecha_ejecucion().getTime()) : null);

            stmt.executeUpdate();
        }
    }

       // Método para obtener todas las tareas
    public List<Tarea> obtenerTareas() {
        List<Tarea> tareas = new ArrayList<>();
        String consulta = "SELECT * FROM TAREA";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id_tarea = rs.getInt("id_tarea");
                String descripcion = rs.getString("descripcion");
                String estadoStr = rs.getString("estado");  // Obtener el estado como String
                EstadoTarea estado = EstadoTarea.valueOf(estadoStr.toUpperCase());  // Convertir a enum
                java.sql.Date fecha_creacion = rs.getDate("fecha_creacion");
                java.sql.Date fecha_ejecucion = rs.getDate("fecha_ejecucion");

                Tarea tarea = new Tarea(id_tarea, descripcion, estado, fecha_creacion, fecha_ejecucion);
                tareas.add(tarea);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Esto se lanza si el estado en la base de datos no coincide con los valores del enum
            System.out.println("Error: EstadoTarea no válido en la base de datos.");
        }

        return tareas;
    }

    public void actualizarTarea(Tarea tarea) throws SQLException {
        String sql = "UPDATE TAREA SET descripcion = ?, estado = ?, fecha_creacion = ?, fecha_ejecucion = ? WHERE id_tarea = ?";

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
        String sql = "DELETE FROM TAREA WHERE id_tarea = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarea);
            stmt.executeUpdate();
        }
    }
}
