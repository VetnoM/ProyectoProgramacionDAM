package com.mycompany.practicabasededatos.database;

import com.mycompany.practicabasededatos.modelo.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    public void insertarEmpleado(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO EMPLEADO (nombre, apellido, lugar_trabajo, salario_bruto, estado_laboral, fecha_contratacion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getLugar_trabajo());
            stmt.setDouble(4, empleado.getSalario_bruto());
            stmt.setString(5, empleado.getEstadolaboral().toString());
            stmt.setDate(6, new java.sql.Date(empleado.getFecha_contratacion().getTime()));

            stmt.executeUpdate();
        }
    }

    public List<Empleado> obtenerEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM EMPLEADO";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("lugar_trabajo"),
                        rs.getDouble("salario_bruto"),
                        rs.getString("estado_laboral"),
                        rs.getDate("fecha_contratacion")
                );
                empleados.add(empleado);
            }
        }
        return empleados;
    }

    public void actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE EMPLEADO SET nombre = ?, apellido = ?, lugar_trabajo = ?, salario_bruto = ?, estado_laboral = ?, fecha_contratacion = ? WHERE id_empleado = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getLugar_trabajo());
            stmt.setDouble(4, empleado.getSalario_bruto());
            stmt.setString(5, empleado.getEstadolaboral().toString());
            stmt.setDate(6, new java.sql.Date(empleado.getFecha_contratacion().getTime()));
            stmt.setInt(7, empleado.getId_empleado());

            stmt.executeUpdate();
        }
    }

    public void eliminarEmpleado(int idEmpleado) throws SQLException {
        String sql = "DELETE FROM EMPLEADO WHERE id_empleado = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEmpleado);
            stmt.executeUpdate();
        }
    }
}
