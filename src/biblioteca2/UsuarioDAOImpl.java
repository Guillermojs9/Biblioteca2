package biblioteca2;

import java.sql.*;
import java.util.HashMap;

/**
 * Implementación de la interfaz UsuarioDAO.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    private Connection con;

    /**
     * Constructor de la clase UsuarioDAOImpl. Recibe una conexión a la base de
     * datos para poder interactuar con ella.
     *
     * @param con Conexión a la base de datos.
     */
    public UsuarioDAOImpl(Connection con) {
        this.con = con;
    }

    /**
     * Obtiene todos los usuarios de la base de datos.
     *
     * @return Un HashMap donde la clave es el ID del usuario y el valor es el
     * objeto Usuario.
     */
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

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuarioInsert El objeto Usuario que se desea insertar en la base
     * de datos.
     * @return true si el usuario fue insertado correctamente, false en caso
     * contrario.
     */
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

    /**
     * Obtiene el ID del último usuario registrado en la base de datos.
     *
     * @return El ID del último usuario insertado.
     */
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

    /**
     * Elimina un usuario de la base de datos según su ID.
     *
     * @param idUsuarioDelete El ID del usuario que se desea eliminar.
     * @return true si el usuario fue eliminado con éxito, false en caso
     * contrario.
     */
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

    /**
     * Modifica un dato específico de un usuario en la base de datos.
     *
     * @param accion La acción que se va a realizar (puede ser "nombre" o
     * "apellidos").
     * @param nuevoDato El nuevo dato que se desea asignar al usuario.
     * @param idUsuario El ID del usuario que se desea modificar.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
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
