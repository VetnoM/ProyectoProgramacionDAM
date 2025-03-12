package com.mycompany.practicabasededatos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dam_bd";
    private static final String USER = "usuariodam";  // Cambia esto por tu usuario de MySQL
    private static final String PASSWORD = "usuariodam";  // Cambia esto por tu contraseña

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
