package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Cliente;
import com.mycompany.practicabasededatos.modelo.TipoCliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Método para insertar un cliente
   public void insertarCliente(Cliente cliente) throws SQLException {
        // Consulta SQL para insertar un nuevo cliente en la tabla 'cliente'
        String sql = "INSERT INTO cliente (id_persona, tipo_cliente, tarjeta_credito, fecha_registro) VALUES (?, ?, ?, ?)";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asigna los valores a los parámetros de la consulta SQL
            stmt.setInt(1, cliente.getId_persona());
            stmt.setString(2, cliente.getTipoCliente().toString());
            stmt.setString(3, cliente.getTarjeta_credito());
            stmt.setDate(4, new java.sql.Date(cliente.getFecha_registro().getTime()));

            // Ejecuta la consulta SQL
            stmt.executeUpdate();
        }
    }

    // Método para obtener todos los clientes
    public List<Cliente> obtenerClientes() throws SQLException {
        // Crea una lista para almacenar los clientes
        List<Cliente> clientes = new ArrayList<>();
        // Consulta SQL para obtener todos los registros de la tabla 'cliente'
        String sql = "SELECT * FROM cliente";

        // Establece una conexión con la base de datos y ejecuta la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre los resultados de la consulta
            while (rs.next()) {
                // Crea un nuevo objeto Cliente y asigna los valores de la base de datos
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
                // Añade el cliente a la lista
                clientes.add(cliente);
            }
        }
        // Devuelve la lista de clientes
        return clientes;
    }
    // Método para obtener un cliente por su ID
    public int obtenerIdClientePorDni(String dni) throws SQLException {
        String sql = "SELECT id_cliente FROM cliente c JOIN persona p ON c.id_persona = p.id_persona WHERE p.dni = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_cliente");
                }
            }
        }
        return -1; // Devuelve -1 si no se encuentra el cliente
    }

    // Método para actualizar un cliente
    public void actualizarCliente(Cliente cliente) throws SQLException {
        // Consulta SQL para actualizar un cliente en la tabla 'cliente'
        String sql = "UPDATE cliente SET tipo_cliente = ?, tarjeta_credito = ?, fecha_registro = ? WHERE id_cliente = ?";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asigna los valores a los parámetros de la consulta SQL
            stmt.setString(1, cliente.getTipoCliente().toString());
            stmt.setString(2, cliente.getTarjeta_credito());
            stmt.setDate(3, new java.sql.Date(cliente.getFecha_registro().getTime()));
            stmt.setInt(4, cliente.getId_cliente());

            // Ejecuta la consulta SQL
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un cliente por su ID
    public void eliminarCliente(int idCliente) throws SQLException {
        // Consulta SQL para eliminar un cliente de la tabla 'cliente' por su ID
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asigna el valor del ID del cliente al parámetro de la consulta SQL
            stmt.setInt(1, idCliente);
            // Ejecuta la consulta SQL
            stmt.executeUpdate();
        }
    }
}
