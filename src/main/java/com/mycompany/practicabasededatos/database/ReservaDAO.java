package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Reserva;
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
                Reserva reserva = new Reserva(
                        rs.getInt("id_reserva"),
                        rs.getString("tipo_reserva"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getDate("fecha_reserva"),
                        rs.getDouble("precio_total"),
                        rs.getInt("id_cliente")
                );
                reservas.add(reserva);
            }
        }
        return reservas;
    }

    public int crearReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reserva (tipo_reserva, fecha_inicio, fecha_fin, fecha_reserva, precio_total, id_cliente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reserva.getTipo_reserva());
            stmt.setDate(2, reserva.getFecha_inicio());
            stmt.setDate(3, reserva.getFecha_fin());
            stmt.setDate(4, reserva.getFecha_reserva());
            stmt.setDouble(5, reserva.getPrecio_total());
            stmt.setInt(6, reserva.getId_cliente());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating reserva failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating reserva failed, no ID obtained.");
                }
            }
        }
    }

    public void actualizarReserva(Reserva reserva) throws SQLException {
        String sql = "UPDATE reserva SET tipo_reserva = ?, fecha_inicio = ?, fecha_fin = ?, fecha_reserva = ?, precio_total = ?, id_cliente = ? WHERE id_reserva = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reserva.getTipo_reserva());
            stmt.setDate(2, reserva.getFecha_inicio());
            stmt.setDate(3, reserva.getFecha_fin());
            stmt.setDate(4, reserva.getFecha_reserva());
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
}
