package biblioteca2;

import java.sql.*;
import java.util.HashMap;

public class LibroDAOImpl implements LibroDAO {

    private Connection con;

    public LibroDAOImpl(Connection con) {
        this.con = con;
    }

    @Override
    public HashMap<Integer, Libro> obtenerLibros() {
        HashMap<Integer, Libro> libros = new HashMap<>();
        try {
            String consulta = "SELECT * FROM libro;"; // Consulta SQL
            ResultSet rs = con.createStatement().executeQuery(consulta); // Ejecutar la consulta
            while (rs.next()) {
                int id = rs.getInt("id"); // Obtener el ID del libro (clave única)
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
