package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.EstadoLaboral;
import com.mycompany.practicabasededatos.modelo.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // Método para insertar un empleado
    public void insertarEmpleado(int idPersona, Empleado empleado) throws SQLException {
        // Consulta SQL para insertar un nuevo empleado en la tabla 'empleado'
        String sql = "INSERT INTO empleado (id_persona, lugar_trabajo, salario, estado_laboral, fecha_contratacion) VALUES (?, ?, ?, ?, ?)";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asigna los valores a los parámetros de la consulta SQL
            stmt.setInt(1, idPersona);
            stmt.setString(2, empleado.getLugar_trabajo());
            stmt.setDouble(3, empleado.getSalario_bruto());
            stmt.setString(4, empleado.getEstadolaboral().name());
            stmt.setDate(5, new java.sql.Date(empleado.getFecha_contratacion().getTime()));

            // Ejecuta la consulta SQL
            stmt.executeUpdate();
        }
    }

public List<Empleado> obtenerEmpleados() throws SQLException {
    List<Empleado> empleados = new ArrayList<>();

    String sql = 
       " SELECT e.id_empleado, e.lugar_trabajo, e.salario, e.estado_laboral, e.fecha_contratacion, "+
              " p.id_persona, p.documento, p.nombre, p.apellido, p.fecha_nacimiento, p.telefono, p.email, p.direccion "+
        "FROM empleado e "+
        "JOIN persona p ON e.id_persona = p.id_persona"
    ;

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Empleado e = new Empleado();
            e.setId_empleado(rs.getInt("id_empleado"));
            e.setLugar_trabajo(rs.getString("lugar_trabajo"));
            e.setSalario_bruto(rs.getDouble("salario"));
            e.setEstadolaboral(EstadoLaboral.valueOf(rs.getString("estado_laboral")));
            e.setFecha_contratacion(rs.getDate("fecha_contratacion"));

            // Datos de la persona heredados
            e.setId_persona(rs.getInt("id_persona"));
            e.setDocumento_identidad(rs.getString("documento"));
            e.setNombre(rs.getString("nombre"));
            e.setApellido(rs.getString("apellido"));
            e.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
            e.setTelefono(rs.getString("telefono"));
            e.setEmail(rs.getString("email"));
            e.setDireccion(rs.getString("direccion"));

            empleados.add(e);
        }
    }

    return empleados;
}

    // Método para obtener el ID de un empleado por su documento
    public int obtenerIdEmpleadoPorDocumento(Connection conn, String documento) throws SQLException {
        // Consulta SQL para obtener el ID del empleado por su documento
        String query = "SELECT empleado.id_empleado FROM empleados e " +
                       "JOIN personas p ON e.idPersona = p.idPersona " +
                       "WHERE p.documento = ?";
        
        // Prepara la consulta SQL
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Asigna el valor del documento al parámetro de la consulta SQL
            stmt.setString(1, documento);
            // Ejecuta la consulta SQL
            ResultSet rs = stmt.executeQuery();
            // Si se encuentra el empleado, devuelve su ID
            if (rs.next()) {
                return rs.getInt("id_empleado"); // Devuelve el id_empleado
            }
        }
        // Si no se encuentra, devuelve -1
        return -1;
    }

    // Método para actualizar un empleado
    public void actualizarEmpleado(Empleado empleado) throws SQLException {
        // Consulta SQL para actualizar un empleado en la tabla 'empleado'
        String sql = "UPDATE empleado SET lugar_trabajo = ?, salario = ?, estado_laboral = ?, fecha_contratacion = ? WHERE id_empleado = ?";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asigna los valores a los parámetros de la consulta SQL
            stmt.setString(1, empleado.getLugar_trabajo());
            stmt.setDouble(2, empleado.getSalario_bruto());
            stmt.setString(3, empleado.getEstadolaboral().name());
            stmt.setDate(4, new java.sql.Date(empleado.getFecha_contratacion().getTime()));
            stmt.setInt(5, empleado.getId_empleado());

            // Ejecuta la consulta SQL
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un empleado por su ID
    public void eliminarEmpleado(int idEmpleado) throws SQLException {
        // Consulta SQL para eliminar un empleado de la tabla 'empleado' por su ID
        String sql = "DELETE FROM empleado WHERE id_empleado = ?";

        // Establece una conexión con la base de datos y prepara la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asigna el valor del ID del empleado al parámetro de la consulta SQL
            stmt.setInt(1, idEmpleado);
            // Ejecuta la consulta SQL
            stmt.executeUpdate();
        }
    }

      // Método para obtener un empleado por ID de persona
    public Empleado obtenerEmpleadoPorIdPersona(int idPersona) {
    String sql = "SELECT * FROM empleado WHERE id_persona = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idPersona);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // Obtener datos de persona
            PersonaDAO personaDAO = new PersonaDAO();
            Persona persona = personaDAO.obtenerPersonaPorDocumento(rs.getString("documento"));

            // Convertir Strings a Enums
            EstadoLaboral estadoLaboral = EstadoLaboral.valueOf(rs.getString("estado_laboral").toUpperCase());

            return new Empleado(persona.getId_persona(), persona.getDocumento_identidad(), persona.getDireccion(),
                                persona.getEmail(), rs.getDouble("salario_bruto"), 
                                rs.getString("lugar_trabajo"), estadoLaboral);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


}
