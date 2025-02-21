package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    public void insertarReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO RESERVA (tipo_reserva, fecha_inicio, fecha_fin, fecha_reserva, precio_total, id_cliente) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reserva.getTipoReserva().toString());
            stmt.setDate(2, new java.sql.Date(reserva.getFecha_inicio().getTime()));
            stmt.setDate(3, new java.sql.Date(reserva.getFecha_fin().getTime()));
            stmt.setDate(4, new java.sql.Date(reserva.getFecha_reserva().getTime()));
            stmt.setDouble(5, reserva.getPrecio_total());
            stmt.setInt(6, reserva.getId_cliente());

            stmt.executeUpdate();
        }
    }

    public List<Reserva> obtenerReservas() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM RESERVA";

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

    public void actualizarReserva(Reserva reserva) throws SQLException {
        String sql = "UPDATE RESERVA SET tipo_reserva = ?, fecha_inicio = ?, fecha_fin = ?, fecha_reserva = ?, precio_total = ?, id_cliente = ? WHERE id_reserva = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reserva.getTipoReserva().toString());
            stmt.setDate(2, new java.sql.Date(reserva.getFecha_inicio().getTime()));
            stmt.setDate(3, new java.sql.Date(reserva.getFecha_fin().getTime()));
            stmt.setDate(4, new java.sql.Date(reserva.getFecha_reserva().getTime()));
            stmt.setDouble(5, reserva.getPrecio_total());
            stmt.setInt(6, reserva.getId_cliente());
            stmt.setInt(7, reserva.getId_reserva());

            stmt.executeUpdate();
        }
    }

    public void eliminarReserva(int idReserva) throws SQLException {
        String sql = "DELETE FROM RESERVA WHERE id_reserva = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);
            stmt.executeUpdate();
        }
    }
}
