package biblioteca2;

import java.sql.*;
import java.util.HashMap;

/**
 * Implementación de la interfaz LibroDAO.
 */
public class LibroDAOImpl implements LibroDAO {

    private Connection con;

    /**
     * Constructor de la clase. Recibe una conexión a la base de datos para interactuar con ella.
     * 
     * @param con Conexión a la base de datos.
     */
    public LibroDAOImpl(Connection con) {
        this.con = con;
    }

    /**
     * Obtiene todos los libros de la base de datos.
     * 
     * @return Un HashMap donde la clave es el ID del libro y el valor es el objeto Libro.
     */
    @Override
    public HashMap<Integer, Libro> obtenerLibros() {
        HashMap<Integer, Libro> libros = new HashMap<>();
        try {
            String consulta = "SELECT * FROM libro;";
            ResultSet rs = con.createStatement().executeQuery(consulta);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int categoria = rs.getInt("categoria");
                Libro libro = new Libro(nombre, autor, editorial, categoria);
                libros.put(id, libro);
            }
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta. Error: " + ex.getMessage());
        }
        return libros;
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     * 
     * @param libroInsert El libro que se desea insertar.
     * @return true si el libro se insertó correctamente, false en caso contrario.
     */
    @Override
    public boolean insertarLibro(Libro libroInsert) {
        try {
            String insert = "INSERT INTO libro (nombre, autor, editorial, categoria) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, libroInsert.getNombre());
            ps.setString(2, libroInsert.getAutor());
            ps.setString(3, libroInsert.getEditorial());
            ps.setInt(4, libroInsert.getCategoria());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al insertar el libro. Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el ID del último libro registrado en la base de datos.
     * 
     * @return El ID del último libro.
     */
    @Override
    public int obtenerUltimoId() {
        int id = 0;
        try {
            String consulta = "SELECT MAX(id) AS id FROM libro;";
            ResultSet rs = con.createStatement().executeQuery(consulta);
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el último ID. Error: " + ex.getMessage());
        }
        return id;
    }

    /**
     * Elimina un libro de la base de datos según su ID.
     * 
     * @param libroDelete El ID del libro a eliminar.
     * @return true si el libro fue eliminado con éxito, false en caso contrario.
     */
    @Override
    public boolean eliminarLibro(int libroDelete) {
        try {
            String delete = "DELETE FROM libro WHERE id = ?;";
            PreparedStatement ps = con.prepareStatement(delete);
            ps.setInt(1, libroDelete);
            int numFilasBorradas = ps.executeUpdate();
            return numFilasBorradas > 0;
        } catch (SQLException ex) {
            System.out.println("Error al eliminar el libro. Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Modifica los detalles de un libro en la base de datos.
     * 
     * @param accion La acción que se va a realizar.
     * @param nuevoDato El nuevo dato que se quiere establecer.
     * @param idLibro El ID del libro que se desea modificar.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean modificarLibro(String accion, String nuevoDato, int idLibro) {
        boolean exito = false;
        try {
            String update = "";
            switch (accion) {
                case "titulo":
                    update = "UPDATE libro SET nombre = ? WHERE id = ?";
                    break;
                case "autor":
                    update = "UPDATE libro SET autor = ? WHERE id = ?";
                    break;
                case "editorial":
                    update = "UPDATE libro SET editorial = ? WHERE id = ?";
                    break;
                case "categoria":
                    update = "UPDATE libro SET categoria = ? WHERE id = ?";
                    break;
            }

            if (!update.isEmpty()) {
                PreparedStatement ps = con.prepareStatement(update);
                ps.setString(1, nuevoDato);
                if (accion.equals("categoria")) {
                    ps.setInt(1, Integer.parseInt(nuevoDato));
                }
                ps.setInt(2, idLibro);
                int filasAfectadas = ps.executeUpdate();
                exito = filasAfectadas > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error al modificar el libro. Error: " + ex.getMessage());
        }
        return exito;
    }

}

