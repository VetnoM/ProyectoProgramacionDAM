package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Persona;
import java.sql.*;
import java.util.ArrayList;

public class PersonaDAO {

    // Método para obtener todas las personas
    public static ArrayList<Persona> obtenerPersonas() {
        ArrayList<Persona> personas = new ArrayList<>();
        String query = "SELECT * FROM PERSONA";

        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Persona persona = new Persona(
                        rs.getInt("id_persona"),
                        rs.getString("documento_identidad"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion")
                );
                personas.add(persona);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener personas: " + e.getMessage());
        }
        return personas;
    }

    // Método para insertar una persona
    public int insertarPersona(Persona persona) {
        String query = "INSERT INTO PERSONA (documento_identidad, nombre, apellido, fecha_nacimiento, telefono, email, direccion) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int idGenerado= -1;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            // Validación de datos antes de insertar
            if (persona.getDocumento_identidad() == null || persona.getNombre() == null || persona.getApellido() == null) {
                System.out.println("Faltan datos obligatorios para la persona.");
                return idGenerado;
            }

            pst.setString(1, persona.getDocumento_identidad());
            pst.setString(2, persona.getNombre());
            pst.setString(3, persona.getApellido());
            pst.setDate(4, new java.sql.Date(persona.getFecha_nacimiento().getTime())); // Conversión de java.util.Date a java.sql.Date
            pst.setString(5, persona.getTelefono());
            pst.setString(6, persona.getEmail());
            pst.setString(7, persona.getDireccion());
            
            int filasAfectadas = pst.executeUpdate(); 
            
            if (filasAfectadas > 0) {
            ResultSet rs = pst.getGeneratedKeys(); // Obtener el ID generado
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }
        }
            
        } catch (SQLException e) {
            System.out.println("Error al insertar persona: " + e.getMessage());
        }
        return idGenerado;
    }

    // Método para actualizar una persona
    public boolean actualizarPersona(Persona persona) {
        String query = "UPDATE PERSONA SET documento_identidad = ?, nombre = ?, apellido = ?, fecha_nacimiento = ?, telefono = ?, email = ?, direccion = ? " +
                       "WHERE id_persona = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, persona.getDocumento_identidad());
            pst.setString(2, persona.getNombre());
            pst.setString(3, persona.getApellido());
            pst.setDate(4, new java.sql.Date(persona.getFecha_nacimiento().getTime())); // Conversión de java.util.Date a java.sql.Date
            pst.setString(5, persona.getTelefono());
            pst.setString(6, persona.getEmail());
            pst.setString(7, persona.getDireccion());
            pst.setInt(8, persona.getId_persona());
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar persona: " + e.getMessage());
        }
        return false;
    }

    // Método para eliminar una persona por su ID
    public boolean eliminarPersona(int idPersona) {
        String query = "DELETE FROM PERSONA WHERE id_persona = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, idPersona);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar persona: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener una persona por su ID
    public Persona obtenerPersonaPorId(int idPersona) {
        Persona persona = null;
        String query = "SELECT * FROM PERSONA WHERE id_persona = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, idPersona);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    persona = new Persona(
                            rs.getInt("id_persona"),
                            rs.getString("documento_identidad"),
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
            System.out.println("Error al obtener persona por ID: " + e.getMessage());
        }
        return persona;
    }
}
