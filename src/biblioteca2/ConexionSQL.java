package biblioteca2;

import java.sql.*;

/**
 * Esta clase gestiona la conexión a la base de datos de la biblioteca. Utiliza
 * el patrón Singleton para asegurarse de que solo haya una instancia de la
 * conexión a la base de datos.
 *
 * @author guillermo
 */
public class ConexionSQL {

    private static String url = "jdbc:mariadb://172.26.157.64:3306/BIBLIOTECA";
    private static String user = "root";
    private static String password = "root";
    private static Connection instance;

    /**
     * Constructor privado para que no haya instancias de la clase
     */
    private ConexionSQL() {

    }

    /**
     * Obtiene la única instancia para la conexión a la base de datos. Aunque se
     * llame varias veces, siempre va a devolver la misma instancia de la
     * conexión
     *
     * @return La instancia de la conexión de la base de datos.
     */
    public static Connection getInstance() {
        if (instance == null) {
            try {
                instance = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                System.out.println("Error en la conexión a la base de datos. Error: " + ex.getMessage());
            }
        }
        return instance;
    }
}
