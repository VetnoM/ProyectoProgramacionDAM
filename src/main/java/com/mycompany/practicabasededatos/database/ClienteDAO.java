package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Cliente;
import com.mycompany.practicabasededatos.modelo.TipoCliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Método para insertar un cliente
    public void insertarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (id_persona, tipo_cliente, tarjeta_credito, fecha_registro) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getId_persona());
            stmt.setString(2, cliente.getTipoCliente().toString());
            stmt.setString(3, cliente.getTarjeta_credito());
            stmt.setDate(4, new java.sql.Date(cliente.getFecha_registro().getTime()));

            stmt.executeUpdate();
        }
    }

    // Método para obtener todos los clientes
    public List<Cliente> obtenerClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId_cliente(rs.getInt("id_cliente"));
                cliente.setFecha_registro(rs.getDate("fecha_registro"));
                cliente.setTarjeta_credito(rs.getString("tarjeta_credito"));
                cliente.setTipoCliente(TipoCliente.valueOf(rs.getString("tipo_cliente")));
                cliente.setId_persona(rs.getInt("id_persona"));
                // Los siguientes atributos se heredan de Persona
                cliente.setDocumento_identidad(rs.getString("documento"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    // Método para actualizar un cliente
    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET tipo_cliente = ?, tarjeta_credito = ?, fecha_registro = ? WHERE id_cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getTipoCliente().toString());
            stmt.setString(2, cliente.getTarjeta_credito());
            stmt.setDate(3, new java.sql.Date(cliente.getFecha_registro().getTime()));
            stmt.setInt(4, cliente.getId_cliente());

            stmt.executeUpdate();
        }
    }

    // Método para eliminar un cliente por su ID
    public void eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        }
    }
}
