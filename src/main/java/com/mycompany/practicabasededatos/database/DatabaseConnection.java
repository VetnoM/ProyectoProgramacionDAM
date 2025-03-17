package com.mycompany.practicabasededatos.database;

// Importaciones necesarias para manejar la conexión a la base de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL de la base de datos MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/dam_bd";
    // Usuario de la base de datos MySQL
    private static final String USER = "usuariodam";  // Cambia esto por tu usuario de MySQL
    // Contraseña de la base de datos MySQL
    private static final String PASSWORD = "usuariodam";  // Cambia esto por tu contraseña

    // Método para obtener una conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        // Devuelve una conexión a la base de datos utilizando los parámetros especificados
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
