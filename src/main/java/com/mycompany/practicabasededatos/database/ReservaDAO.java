package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Reserva;
import com.mycompany.practicabasededatos.modelo.TipoReserva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public List<Reserva> obtenerReservas() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reserva";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Convertir java.sql.Date a LocalDate y tipo_reserva a TipoReserva
                Reserva reserva = new Reserva(
                        rs.getInt("id_reserva"),
                        TipoReserva.valueOf(rs.getString("tipo_reserva")),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getDate("fecha_reserva").toLocalDate(),
                        rs.getDouble("precio_total"),
                        rs.getDouble("tipo_iva"), // Obtener el valor de tipo_iva
                        rs.getInt("id_cliente")
                );
                reservas.add(reserva);
            }
        }
        return reservas;
    }
    public int crearReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reserva (id_reserva, tipo_reserva, fecha_inicio, fecha_fin, fecha_reserva, precio_total, tipo_iva, id_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, reserva.getId_reserva());
            stmt.setString(2, reserva.getTipo_reserva().name());
            stmt.setDate(3, java.sql.Date.valueOf(reserva.getFecha_inicio()));
            stmt.setDate(4, java.sql.Date.valueOf(reserva.getFecha_fin()));
            stmt.setDate(5, java.sql.Date.valueOf(reserva.getFecha_reserva()));
            stmt.setDouble(6, reserva.getPrecio_total());
            stmt.setDouble(7, reserva.getTipo_iva()); // Incluir tipo_iva
            stmt.setInt(8, reserva.getId_cliente());
    
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo crear la reserva. Ninguna fila fue afectada.");
            }
    
            return reserva.getId_reserva();
        }
    }

public void actualizarReserva(Reserva reserva) throws SQLException {
    String sql = "UPDATE reserva SET tipo_reserva = ?, fecha_inicio = ?, fecha_fin = ?, fecha_reserva = ?, precio_total = ?, id_cliente = ? WHERE id_reserva = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, reserva.getTipo_reserva().toString());
        stmt.setDate(2, java.sql.Date.valueOf(reserva.getFecha_inicio())); // Convertir LocalDate a java.sql.Date
        stmt.setDate(3, java.sql.Date.valueOf(reserva.getFecha_fin()));    // Convertir LocalDate a java.sql.Date
        stmt.setDate(4, java.sql.Date.valueOf(reserva.getFecha_reserva())); // Convertir LocalDate a java.sql.Date
        stmt.setDouble(5, reserva.getPrecio_total());
        stmt.setInt(6, reserva.getId_cliente());
        stmt.setInt(7, reserva.getId_reserva());

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
