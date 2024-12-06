package biblioteca2;

import java.util.ArrayList;
import java.sql.*;

public class CategoriaDAOc implements CategoriaDAO {

    private Connection con;

    public CategoriaDAOc(Connection con) {
        this.con = con;
    }

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
