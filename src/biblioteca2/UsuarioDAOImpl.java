package biblioteca2;

import java.sql.*;
import java.util.HashMap;

public class UsuarioDAOImpl implements UsuarioDAO {

    private Connection con;

    public UsuarioDAOImpl(Connection con) {
        this.con = con;
    }

    @Override
    public HashMap<Integer, Usuario> obtenerUsuarios() {
        HashMap<Integer, Usuario> usuarios = new HashMap<>();
        try {
            String consulta = "SELECT * FROM usuario;";
            ResultSet rs = con.createStatement().executeQuery(consulta);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                Usuario usuario = new Usuario(nombre, apellidos);
                usuarios.put(id, usuario);
            }
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta. Error: " + ex.getMessage());
        }
        return usuarios;
    }

    @Override
    public boolean insertarUsuario(Usuario usuarioInsert) {
        try {
            String insert = "INSERT INTO usuario (nombre, apellidos) VALUES (?, ?);";
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, usuarioInsert.getNombre());
            ps.setString(2, usuarioInsert.getApellidos());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al insertar el usuario. Error: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public int obtenerUltimoId() {
        int id = 0;
        try {
            String consulta = "SELECT MAX(id) AS id FROM usuario;";
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
    public boolean eliminarUsuario(int idUsuarioDelete) {
        try {
            String delete = "DELETE FROM usuario WHERE id = ?;";
            PreparedStatement ps = con.prepareStatement(delete);
            ps.setInt(1, idUsuarioDelete);
            int numFilasBorradas = ps.executeUpdate();
            return numFilasBorradas > 0;
        } catch (SQLException ex) {
            System.out.println("Error al eliminar el usuario. Error: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean modificarUsuario(String accion, String nuevoDato, int idUsuario) {
         boolean exito = false;
        try {
            String update = "";
            switch (accion) {
                case "nombre":
                    update = "UPDATE usuario SET nombre = ? WHERE id = ?";
                    break;
                case "apellidos":
                    update = "UPDATE usuario SET apellidos = ? WHERE id = ?";
                    break;
            }

            if (!update.isEmpty()) {
                PreparedStatement ps = con.prepareStatement(update);
                ps.setString(1, nuevoDato);
                ps.setInt(2, idUsuario);
                int filasAfectadas = ps.executeUpdate();
                exito = filasAfectadas > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error al modificar el usuario. Error: " + ex.getMessage());
        }
        return exito;
    }
}
