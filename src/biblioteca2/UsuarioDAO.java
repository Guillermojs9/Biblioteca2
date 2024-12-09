package biblioteca2;

import java.util.HashMap;

/**
 * Interfaz que define las operaciones CRUD para manejar usuarios.
 */
public interface UsuarioDAO {

    /**
     * Obtiene todos los usuarios de la base de datos.
     *
     * @return Un HashMap donde la clave es el ID del usuario y el valor es el
     * objeto Usuario.
     */
    HashMap<Integer, Usuario> obtenerUsuarios();

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuarioInsert El objeto Usuario que se desea insertar en la base
     * de datos.
     * @return true si el usuario fue insertado correctamente, false en caso
     * contrario.
     */
    boolean insertarUsuario(Usuario usuarioInsert);

    /**
     * Obtiene el ID del último usuario registrado en la base de datos.
     *
     * @return El ID del último usuario insertado.
     */
    int obtenerUltimoId();

    /**
     * Elimina un usuario de la base de datos según su ID.
     *
     * @param idUsuarioDelete El ID del usuario que se desea eliminar.
     * @return true si el usuario fue eliminado con éxito, false en caso
     * contrario.
     */
    boolean eliminarUsuario(int idUsuarioDelete);

    /**
     * Modifica un dato específico de un usuario en la base de datos.
     *
     * @param accion La acción que se va a realizar.
     * @param nuevoDato El nuevo dato que se desea asignar al usuario.
     * @param idUsuario El ID del usuario que se desea modificar.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    boolean modificarUsuario(String accion, String nuevoDato, int idUsuario);

}
