package datacoders.modelo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDConnection {

    // Hemos cambiado 'online_store' por 'gestion_pedidos' para que coincida con tu MySQL
    private static final String URL =
            "jdbc:mysql://localhost:3306/gestion_pedidos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "rootsql1.26";

    private BDConnection() {
        // Constructor privado para evitar que se creen objetos de esta clase
    }

    public static Connection getConnection() throws SQLException {
        try {
            // Carga el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL en el proyecto", e);
        }
    }
}