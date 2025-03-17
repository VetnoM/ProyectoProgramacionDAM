package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Habitacion;
import com.mycompany.practicabasededatos.modelo.EstadoHabitacion;
import com.mycompany.practicabasededatos.modelo.TipoHabitacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {

    // Método para obtener una habitación por su ID
    public Habitacion obtenerHabitacionPorId(int id) {
        Habitacion habitacion = null;
        String consulta = "SELECT * FROM habitacion WHERE id_habitacion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    int id_habitacion = rs.getInt("id_habitacion");
                    String numero_habitacion = rs.getString("numero_habitacion");
                    int id_reserva = rs.getInt("id_reserva");
                    TipoHabitacion tipo = TipoHabitacion.valueOf(rs.getString("tipo").toUpperCase());
                    int capacidad = rs.getInt("capacidad");
                    EstadoHabitacion estado = EstadoHabitacion.valueOf(rs.getString("estado").toUpperCase());
                    String descripcion = rs.getString("descripcion");
                    double precio_noche_ad = rs.getDouble("precio_noche_ad");
                    double precio_noche_mp = rs.getDouble("precio_noche_mp");

                    habitacion = new Habitacion(id_habitacion, numero_habitacion, id_reserva, tipo, capacidad, estado, descripcion, precio_noche_ad, precio_noche_mp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Esto se lanza si el estado o tipo en la base de datos no coincide con los valores del enum
            System.out.println("Error: EstadoHabitacion o TipoHabitacion no válido en la base de datos.");
        }

        return habitacion;
    }

    // Método para obtener todas las habitaciones
    public List<Habitacion> obtenerHabitaciones() throws SQLException {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT * FROM habitacion";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Habitacion habitacion = new Habitacion(
                        rs.getInt("id_habitacion"),
                        rs.getString("numero_habitacion"),
                        rs.getInt("id_reserva"),
                        TipoHabitacion.valueOf(rs.getString("tipo").toUpperCase()),
                        rs.getInt("capacidad"),
                        EstadoHabitacion.valueOf(rs.getString("estado").toUpperCase()),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_noche_ad"),
                        rs.getDouble("precio_noche_mp")
                );
                habitaciones.add(habitacion);
            }
        }
        return habitaciones;
    }

    // Método para agregar una habitación
    public int crearHabitacion(Habitacion habitacion) throws SQLException {
        String sql = "INSERT INTO habitacion (numero_habitacion, id_reserva, tipo, capacidad, estado, descripcion, precio_noche_ad, precio_noche_mp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, habitacion.getNumero_habitacion());
            stmt.setInt(2, habitacion.getId_reserva());
            stmt.setString(3, habitacion.getTipo().name());
            stmt.setInt(4, habitacion.getCapacidad());
            stmt.setString(5, habitacion.getEstado().name());
            stmt.setString(6, habitacion.getDescripcion());
            stmt.setDouble(7, habitacion.getPrecio_noche_ad());
            stmt.setDouble(8, habitacion.getPrecio_noche_mp());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating habitacion failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating habitacion failed, no ID obtained.");
                }
            }
        }
    }

    // Método para actualizar una habitación
    public void actualizarHabitacion(Habitacion habitacion) throws SQLException {
        String sql = "UPDATE habitacion SET numero_habitacion = ?, id_reserva = ?, tipo = ?, capacidad = ?, estado = ?, descripcion = ?, precio_noche_ad = ?, precio_noche_mp = ? WHERE id_habitacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, habitacion.getNumero_habitacion());
            stmt.setInt(2, habitacion.getId_reserva());
            stmt.setString(3, habitacion.getTipo().name());
            stmt.setInt(4, habitacion.getCapacidad());
            stmt.setString(5, habitacion.getEstado().name());
            stmt.setString(6, habitacion.getDescripcion());
            stmt.setDouble(7, habitacion.getPrecio_noche_ad());
            stmt.setDouble(8, habitacion.getPrecio_noche_mp());
            stmt.setInt(9, habitacion.getId_habitacion());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar una habitación
    public void eliminarHabitacion(int idHabitacion) throws SQLException {
        String sql = "DELETE FROM habitacion WHERE id_habitacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHabitacion);
            stmt.executeUpdate();
        }
    }
}
