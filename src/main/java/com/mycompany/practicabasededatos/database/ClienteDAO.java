package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void insertarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO CLIENTE (nombre, apellido, tipo_cliente, tarjeta_credito, fecha_registro) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getTipoCliente().toString());
            stmt.setString(4, cliente.getTarjeta_credito());
            stmt.setDate(5, new java.sql.Date(cliente.getFecha_registro().getTime()));

            stmt.executeUpdate();
        }
    }

    public List<Cliente> obtenerClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("tipo_cliente"),
                        rs.getString("tarjeta_credito"),
                        rs.getDate("fecha_registro")
                );
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE CLIENTE SET nombre = ?, apellido = ?, tipo_cliente = ?, tarjeta_credito = ?, fecha_registro = ? WHERE id_cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getTipoCliente().toString());
            stmt.setString(4, cliente.getTarjeta_credito());
            stmt.setDate(5, new java.sql.Date(cliente.getFecha_registro().getTime()));
            stmt.setInt(6, cliente.getId_cliente());

            stmt.executeUpdate();
        }
    }

    public void eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM CLIENTE WHERE id_cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        }
    }
}
