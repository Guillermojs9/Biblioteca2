package biblioteca2;

import java.util.HashMap;

/**
 * Interfaz que define las operaciones para manejar los préstamos de libros.
 */
public interface PrestamoDAO {

    /**
     * Obtiene los libros que están disponibles para préstamo.
     * 
     * @return Un HashMap donde la clave es el ID del libro y el valor es el objeto Libro.
     */
    HashMap<Integer, Libro> obtenerLibrosLibres();

    /**
     * Obtiene los libros prestados.
     * 
     * @return Un HashMap donde la clave es el ID del libro y el valor es el objeto Libro.
     */
    HashMap<Integer, Libro> obtenerLibrosPrestados();

    /**
     * Presta un libro a un usuario.
     * 
     * @param idLibro El ID del libro que se va a prestar.
     * @param idUsuario El ID del usuario que recibe el préstamo.
     * @return true si el préstamo se realiza con éxito, false en caso contrario.
     */
    boolean prestarLibro(int idLibro, int idUsuario);

    /**
     * Devuelve un libro.
     * 
     * @param idLibro El ID del libro que se devuelve.
     * @return true si la devolución se realiza con éxito, false en caso contrario.
     */
    boolean devolverLibro(int idLibro);
}

