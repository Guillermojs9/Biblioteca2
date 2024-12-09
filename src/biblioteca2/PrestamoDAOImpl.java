package biblioteca2;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Implementación de la interfaz PrestamoDAO.
 */
public class PrestamoDAOImpl implements PrestamoDAO {

    private Connection con;

    /**
     * Constructor de la clase. Recibe una conexión a la base de datos para
     * interactuar con ella.
     *
     * @param con Conexión a la base de datos.
     */
    public PrestamoDAOImpl(Connection con) {
        this.con = con;
    }

    /**
     * Obtiene todos los libros que no están prestados.
     *
     * @return Un HashMap donde la clave es el ID del libro y el valor es el
     * objeto Libro. Este HashMap contiene los libros que no están prestados.
     */
    @Override
    public HashMap<Integer, Libro> obtenerLibrosLibres() {
        HashMap<Integer, Libro> librosLibres = new HashMap<>();
        try {
            String consulta = "SELECT * FROM libro LEFT JOIN prestamos ON prestamos.idLibro = libro.id WHERE idPrestamo IS NULL;";
            ResultSet rs = con.createStatement().executeQuery(consulta);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int categoria = rs.getInt("categoria");
                Libro libro = new Libro(nombre, autor, editorial, categoria);
                librosLibres.put(id, libro);
            }
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta. Error: " + ex.getMessage());
        }
        return librosLibres;
    }

    /**
     * Obtiene todos los libros que están prestados.
     *
     * @return Un HashMap donde la clave es el ID del libro y el valor es el
     * objeto Libro. Este HashMap contiene solo los libros que están prestados.
     */
    @Override
    public HashMap<Integer, Libro> obtenerLibrosPrestados() {
        HashMap<Integer, Libro> librosPrestados = new HashMap<>();
        try {
            String consulta = "SELECT * FROM libro INNER JOIN prestamos ON prestamos.idLibro = libro.id;";
            ResultSet rs = con.createStatement().executeQuery(consulta);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int categoria = rs.getInt("categoria");
                Libro libro = new Libro(nombre, autor, editorial, categoria);
                librosPrestados.put(id, libro);
            }
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta. Error: " + ex.getMessage());
        }
        return librosPrestados;
    }

    /**
     * Préstamo de un libro a un usuario.
     *
     * @param idLibro El ID del libro que se desea prestar.
     * @param idUsuario El ID del usuario que solicita el préstamo.
     * @return true si el préstamo fue exitoso, false en caso contrario.
     */
    @Override
    public boolean prestarLibro(int idLibro, int idUsuario) {
        try {
            String insert = "INSERT INTO prestamos (idLibro, idUsuario, fechaPrestamo) VALUES (?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setInt(1, idLibro);
            ps.setInt(2, idUsuario);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            ps.setString(3, formattedDateTime);

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al prestar el libro. Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Elimina el registro de un libro prestado, marcándolo como devuelto.
     *
     * @param idLibro El ID del libro que se desea devolver.
     * @return true si la devolución fue exitosa, false en caso contrario.
     */
    @Override
    public boolean devolverLibro(int idLibro) {
        try {
            String insert = "DELETE FROM prestamos WHERE idLibro = (?);";
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setInt(1, idLibro);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al devolver el libro. Error: " + ex.getMessage());
            return false;
        }
    }

}
