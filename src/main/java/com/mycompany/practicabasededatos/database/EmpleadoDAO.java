package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.EstadoLaboral;
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

    // Método para obtener todos los empleados
    public List<Empleado> obtenerEmpleados() throws SQLException {
        // Crea una lista para almacenar los empleados
        List<Empleado> empleados = new ArrayList<>();
        // Consulta SQL para obtener todos los registros de la tabla 'empleado'
        String sql = "SELECT * FROM empleado";

        // Establece una conexión con la base de datos y ejecuta la consulta SQL
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Itera sobre los resultados de la consulta
            while (rs.next()) {
                // Crea un nuevo objeto Empleado y asigna los valores de la base de datos
                Empleado empleado = new Empleado();
                empleado.setId_empleado(rs.getInt("id_empleado"));
                empleado.setId_persona(rs.getInt("id_persona"));
                empleado.setLugar_trabajo(rs.getString("lugar_trabajo"));
                empleado.setSalario_bruto(rs.getDouble("salario"));
                empleado.setEstadolaboral(EstadoLaboral.valueOf(rs.getString("estado_laboral")));
                empleado.setFecha_contratacion(rs.getDate("fecha_contratacion"));
                // Añade el empleado a la lista
                empleados.add(empleado);
            }
        }
        // Devuelve la lista de empleados
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
}
