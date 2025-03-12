package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.EstadoLaboral;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // Método para insertar un empleado
    public void insertarEmpleado(int idPersona, Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleado (id_persona, lugar_trabajo, salario, estado_laboral, fecha_contratacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPersona);
            stmt.setString(2, empleado.getLugar_trabajo());
            stmt.setDouble(3, empleado.getSalario_bruto());
            stmt.setString(4, empleado.getEstadolaboral().name());
            stmt.setDate(5, new java.sql.Date(empleado.getFecha_contratacion().getTime()));

            stmt.executeUpdate();
        }
    }

    // Método para obtener todos los empleados
    public List<Empleado> obtenerEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleado";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setId_empleado(rs.getInt("id_empleado"));
                empleado.setId_persona(rs.getInt("id_persona"));
                empleado.setLugar_trabajo(rs.getString("lugar_trabajo"));
                empleado.setSalario_bruto(rs.getDouble("salario"));
                empleado.setEstadolaboral(EstadoLaboral.valueOf(rs.getString("estado_laboral")));
                empleado.setFecha_contratacion(rs.getDate("fecha_contratacion"));
                empleados.add(empleado);
            }
        }
        return empleados;
    }

    public int obtenerIdEmpleadoPorDocumento(Connection conn, String documento) throws SQLException {
        String query = "SELECT empleado.id_empleado FROM empleados e " +
                       "JOIN personas p ON e.idPersona = p.idPersona " +
                       "WHERE p.documento = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idEmpleado"); // Devuelve el idEmpleado
            }
        }
        return -1; // Si no se encuentra, devuelve -1
    }
    

    // Método para actualizar un empleado
    public void actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleado SET lugar_trabajo = ?, salario = ?, estado_laboral = ?, fecha_contratacion = ? WHERE id_empleado = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getLugar_trabajo());
            stmt.setDouble(2, empleado.getSalario_bruto());
            stmt.setString(3, empleado.getEstadolaboral().name());
            stmt.setDate(4, new java.sql.Date(empleado.getFecha_contratacion().getTime()));
            stmt.setInt(5, empleado.getId_empleado());

            stmt.executeUpdate();
        }
    }


    // Método para eliminar un empleado por su ID
    public void eliminarEmpleado(int idEmpleado) throws SQLException {
        String sql = "DELETE FROM empleado WHERE id_empleado = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpleado);
            stmt.executeUpdate();
        }
    }
}
