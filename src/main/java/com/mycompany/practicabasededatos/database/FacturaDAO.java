package com.mycompany.practicabasededatos.database;
import com.mycompany.practicabasededatos.modelo.Factura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    public void insertarFactura(Factura factura) throws SQLException {
        String sql = "INSERT INTO FACTURA (fecha_emision, metodo_pago, base_imponible, iva, total, id_reserva) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(factura.getFecha_emision().getTime()));
            stmt.setString(2, factura.getMetodo_pago());
            stmt.setDouble(3, factura.getBase_imponible());
            stmt.setDouble(4, factura.getIva());
            stmt.setDouble(5, factura.getTotal());
            stmt.setInt(6, factura.getId_reserva());

            stmt.executeUpdate();
        }
    }

    public List<Factura> obtenerFacturas() throws SQLException {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM FACTURA";               
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Factura factura = new Factura(
                        rs.getInt("id_factura"),
                        rs.getDate("fecha_emision"),
                        rs.getString("metodo_pago"),
                        rs.getDouble("base_imponible"),
                        rs.getDouble("iva"),
                        rs.getDouble("total"),
                        rs.getInt("id_reserva")
                );
                facturas.add(factura);
            }
        }
        return facturas;
    }

    public void actualizarFactura(Factura factura) throws SQLException {
        String sql = "UPDATE FACTURA SET fecha_emision = ?, metodo_pago = ?, base_imponible = ?, iva = ?, total = ?, id_reserva = ? WHERE id_factura = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(factura.getFecha_emision().getTime()));
            stmt.setString(2, factura.getMetodo_pago());
            stmt.setDouble(3, factura.getBase_imponible());
            stmt.setDouble(4, factura.getIva());
            stmt.setDouble(5, factura.getTotal());
            stmt.setInt(6, factura.getId_reserva());
            stmt.setInt(7, factura.getId_factura());

            stmt.executeUpdate();
            
        }
    }

    public void eliminarFactura(int idFactura) throws SQLException {
        String sql = "DELETE FROM FACTURA WHERE id_factura = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFactura);
            stmt.executeUpdate();
        }
    }
}
