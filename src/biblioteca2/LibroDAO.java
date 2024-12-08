package biblioteca2;

import java.util.HashMap;

public interface LibroDAO {

    HashMap<Integer, Libro> obtenerLibros();

    boolean insertarLibro(Libro libroInsert);
    
    boolean eliminarLibro(int libroDelete);
    
    int obtenerUltimoId();
    
    boolean modificarLibro(String accion, String nuevoDato, int idLibro);
}
