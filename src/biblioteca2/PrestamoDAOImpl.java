package biblioteca2;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class PrestamoDAOImpl implements PrestamoDAO {

    private Connection con;

    public PrestamoDAOImpl(Connection con) {
        this.con = con;
    }

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
