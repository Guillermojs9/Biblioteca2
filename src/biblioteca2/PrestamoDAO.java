package biblioteca2;

import java.util.HashMap;

public interface PrestamoDAO {

    HashMap<Integer, Libro> obtenerLibrosLibres();
    
    HashMap<Integer, Libro> obtenerLibrosPrestados();
    
    boolean prestarLibro(int idLibro, int idUsuario);
    
    boolean devolverLibro(int idLibro);

}
