package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Reserva;
import com.mycompany.practicabasededatos.modelo.TipoReserva;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class ReservaDAO {
    public ObservableList<Reserva> obtenerReservas() {
        ObservableList<Reserva> reservas = FXCollections.observableArrayList();
        String sql = "SELECT r.id_reserva, r.tipo_reserva, r.fecha_inicio, r.fecha_fin, r.fecha_reserva, " +
                     "r.precio_total, r.id_cliente, r.id_habitacion, r.estado, h.numero_habitacion " +
                     "FROM reserva r " +
                     "LEFT JOIN habitacion h ON r.id_habitacion = h.id_habitacion";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                Reserva reserva = new Reserva(
                    rs.getInt("id_reserva"),
                    TipoReserva.valueOf(rs.getString("tipo_reserva")),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin").toLocalDate(),
                    rs.getDate("fecha_reserva").toLocalDate(),
                    rs.getDouble("precio_total"),
                    rs.getInt("id_cliente"),
                    rs.getInt("id_habitacion"),
                    rs.getString("estado"),
                    rs.getString("numero_habitacion") // Obtener el número de habitación
                );
    
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
    // metodo para obtener personas sin reserva
    public ObservableList<String> obtenerPersonasSinReserva() {
        ObservableList<String> personas = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT p.documento, p.nombre, p.apellido, " +
                     "CASE WHEN c.id_cliente IS NOT NULL THEN 'Cliente' ELSE 'Empleado' END AS tipo " +
                     "FROM persona p " +
                     "LEFT JOIN cliente c ON p.id_persona = c.id_persona " +
                     "LEFT JOIN empleado e ON p.id_persona = e.id_persona " +
                     "WHERE p.id_persona NOT IN (SELECT r.id_cliente FROM reserva r)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String persona = rs.getString("documento") + " - " +
                                 rs.getString("nombre") + " " +
                                 rs.getString("apellido") + " (" + tipo + ")";
                personas.add(persona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personas;
    }

    public int crearReserva(Reserva reserva) throws SQLException {
        int nuevoId = obtenerProximoIdReserva(); // Obtener el nuevo ID manualmente
        reserva.setId_reserva(nuevoId); // Asignar el ID a la reserva
        String sql = "INSERT INTO reserva (id_reserva, tipo_reserva, fecha_inicio, fecha_fin, fecha_reserva, precio_total, id_cliente, id_habitacion, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, reserva.getId_reserva());
            stmt.setString(2, reserva.getTipo_reserva().name());
            stmt.setDate(3, java.sql.Date.valueOf(reserva.getFecha_inicio()));
            stmt.setDate(4, java.sql.Date.valueOf(reserva.getFecha_fin()));
            stmt.setDate(5, java.sql.Date.valueOf(reserva.getFecha_reserva()));
            stmt.setDouble(6, reserva.getPrecio_total());
            stmt.setInt(7, reserva.getId_cliente());
            stmt.setInt(8, reserva.getId_habitacion()); // Asignar el id_habitacion
            stmt.setString(9, "Pendiente de facturar"); // Estado inicial
    
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo crear la reserva. Ninguna fila fue afectada.");
            }
    
            return reserva.getId_reserva();
        }
    }

    

    public void actualizarReserva(Reserva reserva) throws SQLException {
        String sql = "UPDATE reserva SET tipo_reserva = ?, fecha_inicio = ?, fecha_fin = ?, fecha_reserva = ?, precio_total = ?, id_cliente = ?, id_habitacion = ?, estado = ? WHERE id_reserva = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, reserva.getTipo_reserva().toString());
            stmt.setDate(2, java.sql.Date.valueOf(reserva.getFecha_inicio()));
            stmt.setDate(3, java.sql.Date.valueOf(reserva.getFecha_fin()));
            stmt.setDate(4, java.sql.Date.valueOf(reserva.getFecha_reserva()));
            stmt.setDouble(5, reserva.getPrecio_total());
            stmt.setInt(6, reserva.getId_cliente());
            stmt.setInt(7, reserva.getId_habitacion());
            stmt.setString(8, reserva.getEstado()); // Actualizar el estado
            stmt.setInt(9, reserva.getId_reserva());
    
            stmt.executeUpdate();
        }
    }
    public void eliminarReserva(int idReserva) throws SQLException {
        String sql = "DELETE FROM reserva WHERE id_reserva = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            stmt.executeUpdate();
        }
    }
    
   public void actualizarEstadoHabitacion(int idHabitacion, String nuevoEstado) throws SQLException {
        String sql = "UPDATE habitacion SET estado = ? WHERE id_habitacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idHabitacion);
    
            stmt.executeUpdate();
        }
    }
    public int obtenerIdClientePorDocumento(String documento) {
        int idCliente = -1; // Valor por defecto si no se encuentra
    
        String sql = "SELECT c.id_cliente " +
                     "FROM cliente c " +
                     "JOIN persona p ON c.id_persona = p.id_persona " +
                     "WHERE p.documento = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                idCliente = rs.getInt("id_cliente");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return idCliente;
    }

 
    

    public int obtenerIdHabitacionPorNumero(String numeroHabitacion) {
        int idHabitacion = -1; // Valor por defecto si no se encuentra
    
        String sql = "SELECT id_habitacion FROM habitacion WHERE numero_habitacion = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, numeroHabitacion);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                idHabitacion = rs.getInt("id_habitacion");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return idHabitacion;
    }
    
    

    public int obtenerProximoIdReserva() throws SQLException {
        String sql = "SELECT MAX(id_reserva) AS max_id FROM reserva";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1; // Incrementa el valor máximo en 1
            } else {
                return 1; // Si no hay registros, comienza desde 1
            }
        }
    }
}
