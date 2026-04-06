package datacoders.modelo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión con MySQL.
 *
 * NOTA PARA EL GRUPO:
 * - Centraliza los datos de conexión para no repetirlos en cada DAO.
 * - Si en el futuro cambia el nombre de la base de datos, usuario o contraseña,
 *   solo habrá que modificar esta clase.
 * - Los DAO deben usar siempre BDConnection.getConnection().
 */
public class BDConnection {

    // MODIFICAR según nuestra configuración local de MySQL
    private static final String URL = "____________________________________";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Constructor privado para evitar instanciar esta clase
    private BDConnection() {
    }

    /**
     * Devuelve una conexión abierta a la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}