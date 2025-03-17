package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Persona;
import java.sql.*;
import java.util.ArrayList;

public class PersonaDAO {

    // Método para obtener todas las personas
    public ArrayList<Persona> obtenerPersonas() {
        // Crea una lista para almacenar las personas
        ArrayList<Persona> personas = new ArrayList<>();
        // Consulta SQL para obtener todos los registros de la tabla 'persona'
        String query = "SELECT * FROM persona";

        // Establece una conexión con la base de datos y ejecuta la consulta SQL
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            // Itera sobre los resultados de la consulta
            while (rs.next()) {
                // Crea un nuevo objeto Persona y asigna los valores de la base de datos
                Persona persona = new Persona(
                        rs.getInt("id_persona"),
                        rs.getString("documento"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion")
                );
                // Añade la persona a la lista
                personas.add(persona);
            }
        } catch (SQLException e) {
            // Manejo de excepciones SQL
            System.out.println("Error al obtener personas: " + e.getMessage());
        }
        // Devuelve la lista de personas
        return personas;
    }

    // Método para insertar una persona y devolver el ID generado
    public int insertarPersona(Persona persona) {
        // Consulta SQL para insertar una nueva persona en la tabla 'persona'
        String query = "INSERT INTO persona (documento, nombre, apellido, fecha_nacimiento, telefono, email, direccion) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        // Variable para almacenar el ID generado
        int idGenerado = -1;

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Validación de datos antes de insertar
            if (persona.getDocumento_identidad() == null || persona.getNombre() == null || persona.getApellido() == null) {
                System.out.println("Faltan datos obligatorios para la persona.");
                return idGenerado;
            }

            // Asigna los valores a los parámetros de la consulta SQL
            pst.setString(1, persona.getDocumento_identidad());
            pst.setString(2, persona.getNombre());
            pst.setString(3, persona.getApellido());
            pst.setDate(4, new java.sql.Date(persona.getFecha_nacimiento().getTime())); // Conversión de java.util.Date a java.sql.Date
            pst.setString(5, persona.getTelefono());
            pst.setString(6, persona.getEmail());
            pst.setString(7, persona.getDireccion());
            
            // Ejecuta la consulta SQL
            int filasAfectadas = pst.executeUpdate(); 
            
            // Si se insertó correctamente, obtiene el ID generado
            if (filasAfectadas > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) { // Obtener el ID generado
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            // Manejo de excepciones SQL
            System.out.println("Error al insertar persona: " + e.getMessage());
        }
        // Devuelve el ID generado
        return idGenerado;
    }

    // Método para obtener el ID de una persona por su documento de identidad
    public int obtenerID_persona(Persona p) {
        // Consulta SQL para obtener el ID de la persona por su documento de identidad
        String query = "SELECT id_persona FROM persona WHERE documento = ?";
        // Variable para almacenar el ID generado
        int idGenerado = -1;
        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            // Asigna el valor del documento de identidad al parámetro de la consulta SQL
            pst.setString(1, p.getDocumento_identidad());
            // Ejecuta la consulta SQL
            try (ResultSet rs = pst.executeQuery()) {
                // Si se encuentra la persona, obtiene su ID
                if (rs.next()) {
                    idGenerado = rs.getInt("id_persona");
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones SQL
            System.out.println("Error al obtener ID de persona: " + e.getMessage());
        }
        // Devuelve el ID generado
        return idGenerado;
    }

    // Método para actualizar una persona
    public boolean actualizarPersona(Persona persona) {
        // Consulta SQL para actualizar una persona en la tabla 'persona'
        String query = "UPDATE persona SET documento = ?, nombre = ?, apellido = ?, fecha_nacimiento = ?, telefono = ?, email = ?, direccion = ? " +
                       "WHERE id_persona = ?";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            // Asigna los valores a los parámetros de la consulta SQL
            pst.setString(1, persona.getDocumento_identidad());
            pst.setString(2, persona.getNombre());
            pst.setString(3, persona.getApellido());
            pst.setDate(4, new java.sql.Date(persona.getFecha_nacimiento().getTime())); // Conversión de java.util.Date a java.sql.Date
            pst.setString(5, persona.getTelefono());
            pst.setString(6, persona.getEmail());
            pst.setString(7, persona.getDireccion());
            pst.setInt(8, persona.getId_persona());
            
            // Ejecuta la consulta SQL
            int rowsAffected = pst.executeUpdate();
            // Devuelve true si se actualizó correctamente, false en caso contrario
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Manejo de excepciones SQL
            System.out.println("Error al actualizar persona: " + e.getMessage());
        }
        // Devuelve false si hubo un error
        return false;
    }

    // Método para eliminar una persona por su ID
    public boolean eliminarPersona(int idPersona) {
        // Consulta SQL para eliminar una persona de la tabla 'persona' por su ID
        String query = "DELETE FROM persona WHERE id_persona = ?";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            // Asigna el valor del ID de la persona al parámetro de la consulta SQL
            pst.setInt(1, idPersona);
            // Ejecuta la consulta SQL
            int rowsAffected = pst.executeUpdate();
            // Devuelve true si se eliminó correctamente, false en caso contrario
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Manejo de excepciones SQL
            System.out.println("Error al eliminar persona: " + e.getMessage());
        }
        // Devuelve false si hubo un error
        return false;
    }

    // Método para obtener una persona por su ID
    public Persona obtenerPersonaPorId(int idPersona) {
        // Variable para almacenar la persona obtenida
        Persona persona = null;
        // Consulta SQL para obtener una persona por su ID
        String query = "SELECT * FROM persona WHERE id_persona = ?";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            // Asigna el valor del ID de la persona al parámetro de la consulta SQL
            pst.setInt(1, idPersona);
            // Ejecuta la consulta SQL
            try (ResultSet rs = pst.executeQuery()) {
                // Si se encuentra la persona, crea un nuevo objeto Persona y asigna los valores de la base de datos
                if (rs.next()) {
                    persona = new Persona(
                            rs.getInt("id_persona"),
                            rs.getString("documento"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getDate("fecha_nacimiento"),
                            rs.getString("telefono"),
                            rs.getString("email"),
                            rs.getString("direccion")
                    );
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones SQL
            System.out.println("Error al obtener persona por ID: " + e.getMessage());
        }
        // Devuelve la persona obtenida
        return persona;
    }
}
