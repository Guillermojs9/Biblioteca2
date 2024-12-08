package biblioteca2;

import java.util.HashMap;

public interface UsuarioDAO {

    HashMap<Integer, Usuario> obtenerUsuarios();

    boolean insertarUsuario(Usuario usuarioInsert);

    int obtenerUltimoId();

    boolean eliminarUsuario(int idUsuarioDelete);
    
    boolean modificarUsuario(String accion, String nuevoDato, int idUsuario);

}
