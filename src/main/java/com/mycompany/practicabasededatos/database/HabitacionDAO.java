package com.mycompany.practicabasededatos.database;
import com.mycompany.practicabasededatos.database.DatabaseConnection;
import com.mycompany.practicabasededatos.modelo.Habitacion;
import com.mycompany.practicabasededatos.modelo.EstadoHabitacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {

    // Método para obtener una habitación por su ID
    public Habitacion obtenerHabitacionPorId(int id) {
        Habitacion habitacion = null;
        String consulta = "SELECT * FROM HABITACION WHERE id_habitacion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int id_habitacion = rs.getInt("id_habitacion");
                String numero_habitacion = rs.getString("numero_habitacion");
                String tipo = rs.getString("tipo");
                int capacidad = rs.getInt("capacidad");
                String estadoStr = rs.getString("estado");  // Obtener el estado como String
                EstadoHabitacion estado = EstadoHabitacion.valueOf(estadoStr.toUpperCase());  // Convertir a enum
                String descripcion = rs.getString("descripcion");
                double precio_noche_ad = rs.getDouble("precio_noche_ad");
                double precio_noche_mp = rs.getDouble("precio_noche_mp");

                habitacion = new Habitacion(id_habitacion, numero_habitacion, tipo, capacidad, estado, descripcion, precio_noche_ad, precio_noche_mp);
            }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Esto se lanza si el estado en la base de datos no coincide con los valores del enum
            System.out.println("Error: EstadoHabitacion no válido en la base de datos.");
        }

        return habitacion;
    }

    // Método para obtener todas las habitaciones
    public List<Habitacion> obtenerHabitaciones() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String consulta = "SELECT * FROM HABITACION";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id_habitacion = rs.getInt("id_habitacion");
                String numero_habitacion = rs.getString("numero_habitacion");
                String tipo = rs.getString("tipo");
                int capacidad = rs.getInt("capacidad");
                String estadoStr = rs.getString("estado");  // Obtener el estado como String
                EstadoHabitacion estado = EstadoHabitacion.valueOf(estadoStr.toUpperCase());  // Convertir a enum
                String descripcion = rs.getString("descripcion");
                double precio_noche_ad = rs.getDouble("precio_noche_ad");
                double precio_noche_mp = rs.getDouble("precio_noche_mp");

                Habitacion habitacion = new Habitacion(id_habitacion, numero_habitacion, tipo, capacidad, estado, descripcion, precio_noche_ad, precio_noche_mp);
                habitaciones.add(habitacion);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la habitacion "+ e);
        } catch (IllegalArgumentException e) {
            // Esto se lanza si el estado en la base de datos no coincide con los valores del enum
            System.out.println("Error: EstadoHabitacion no válido en la base de datos.");
        }

        return habitaciones;
    }

    // Método para agregar una habitación
    public boolean agregarHabitacion(Habitacion habitacion) {
        String consulta = "INSERT INTO HABITACION (numero_habitacion, tipo, capacidad, estado, descripcion, precio_noche_ad, precio_noche_mp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {

            stmt.setString(1, habitacion.getNumero_habitacion());
            stmt.setString(2, habitacion.getTipo().toString());
            stmt.setInt(3, habitacion.getCapacidad());
            stmt.setString(4, habitacion.getEstado().toString());  // Convertir el enum a String
            stmt.setString(5, habitacion.getDescripcion());
            stmt.setDouble(6, habitacion.getPrecio_noche_ad());
            stmt.setDouble(7, habitacion.getPrecio_noche_mp());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar habitacion "+ e);
            return false;
        }
    }

    // Método para actualizar una habitación
    public boolean actualizarHabitacion(Habitacion habitacion) {
        String consulta = "UPDATE HABITACION SET numero_habitacion = ?, tipo = ?, capacidad = ?, estado = ?, descripcion = ?, precio_noche_ad = ?, precio_noche_mp = ? WHERE id_habitacion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {

            stmt.setString(1, habitacion.getNumero_habitacion());
            stmt.setString(2, habitacion.getTipo().toString());
            stmt.setInt(3, habitacion.getCapacidad());
            stmt.setString(4, habitacion.getEstado().toString());  // Convertir el enum a String
            stmt.setString(5, habitacion.getDescripcion());
            stmt.setDouble(6, habitacion.getPrecio_noche_ad());
            stmt.setDouble(7, habitacion.getPrecio_noche_mp());
            stmt.setInt(8, habitacion.getId_habitacion());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar"+ e);
            return false;
        }
    }

    // Método para eliminar una habitación
    public boolean eliminarHabitacion(int id) {
        String consulta = "DELETE FROM HABITACION WHERE id_habitacion = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consulta)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar "+e);
            return false;
        }
    }
}
