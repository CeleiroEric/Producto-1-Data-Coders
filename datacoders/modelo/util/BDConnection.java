package datacoders.modelo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
  * NOTA PARA EL GRUPO:
 * - Centraliza los datos de conexión para no repetirlos en cada DAO.
 * - Si en el futuro cambia el nombre de la base de datos, usuario o contraseña,
 *   solo habrá que modificar esta clase.
 * - Los DAO deben usar siempre BDConnection.getConnection().
 */
public class BDConnection {

    // Adaptado usando la configuración encontrada en la clase Conexion.
    // IMPORTANTE: el nombre de la base de datos se ha ajustado a "online_store"
    // porque es el que aparece en schema.sql y procedures.sql.
    private static final String URL =
            "jdbc:mysql://localhost:3306/online_store?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "rootsql1.26";

    // Constructor privado para evitar instanciar esta clase
    private BDConnection() {
    }

    /**
     * Devuelve una conexión abierta a la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Carga explícita del driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL", e);
        }
    }
}