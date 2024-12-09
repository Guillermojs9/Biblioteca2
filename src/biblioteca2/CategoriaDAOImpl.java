package biblioteca2;

import java.util.ArrayList;
import java.sql.*;

/**
 * Implementación de la interfaz CategoriaDAO.
 */
public class CategoriaDAOImpl implements CategoriaDAO {

    private Connection con;

    /**
     * Constructor de la clase CategoriaDAOImpl.
     *
     * @param con Objeto Connection para interactuar con la base de datos.
     */
    public CategoriaDAOImpl(Connection con) {
        this.con = con;
    }

    /**
     * Obtiene una lista de todas las categorías de la base de datos.
     *
     * @return Una lista de cadenas que representan las categorías.
     */
    @Override
    public ArrayList<String> obtenerCategorias() {
        ArrayList<String> categorias = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM categoria;";
            ResultSet rs = con.createStatement().executeQuery(consulta);
            while (rs.next()) {
                String categoria = rs.getString("categoria");
                categorias.add(categoria);
            }
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta. Error: " + ex.getMessage());
        }
        return categorias;
    }

    /**
     * Inserta una nueva categoría en la base de datos.
     *
     * @param categoria El nombre de la categoría que se desea insertar.
     * @return true si la categoría se insertó, false en caso contrario.
     */
    @Override
    public boolean insertarCategoria(String categoria) {
        try {
            String insert = "INSERT INTO categoria (categoria) VALUES (?);";
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, categoria);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al insertar la categoría. Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Elimina una categoría de la base de datos.
     *
     * @param categoria El nombre de la categoría que se desea eliminar.
     * @return true si al menos una fila fue eliminada, false en caso contrario.
     */
    @Override
    public boolean eliminarCategoria(String categoria) {
        try {
            String delete = "DELETE FROM categoria WHERE categoria = ?;";
            PreparedStatement ps = con.prepareStatement(delete);
            ps.setString(1, categoria);
            int numFilasBorradas = ps.executeUpdate();
            return numFilasBorradas > 0;
        } catch (SQLException ex) {
            System.out.println("Error al eliminar la categoría. Error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Verifica si una categoría existe en la base de datos.
     *
     * @param categoria El nombre de la categoría que se desea verificar.
     * @return true si la categoría existe, false en caso contrario.
     */
    @Override
    public boolean existeCategoria(String categoria) {
        try {
            String consulta = "SELECT COUNT(*) FROM categoria WHERE categoria = ?;";
            PreparedStatement ps = con.prepareStatement(consulta);
            ps.setString(1, categoria);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error al verificar la categoría. Error: " + ex.getMessage());
        }
        return false;
    }
}

