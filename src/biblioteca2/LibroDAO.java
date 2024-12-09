package biblioteca2;

import java.util.HashMap;

/**
 * Interfaz que define las operaciones CRUD para manejar libros.
 */
public interface LibroDAO {

    /**
     * Obtiene todos los libros de la base de datos.
     * 
     * @return Un HashMap donde la clave es el ID del libro y el valor es el objeto Libro.
     */
    HashMap<Integer, Libro> obtenerLibros();

    /**
     * Inserta un nuevo libro.
     * 
     * @param libroInsert El libro que se desea insertar.
     * @return true si el libro se insertó correctamente, false en caso contrario.
     */
    boolean insertarLibro(Libro libroInsert);

    /**
     * Elimina un libro según su ID.
     * 
     * @param libroDelete El ID del libro que se desea eliminar.
     * @return true si el libro fue eliminado con éxito, false en caso contrario.
     */
    boolean eliminarLibro(int libroDelete);

    /**
     * Obtiene el ID del último libro registrado.
     * 
     * @return El ID del último libro.
     */
    int obtenerUltimoId();

    /**
     * Modifica los detalles de un libro.
     * 
     * @param accion La acción que se va a realizar.
     * @param nuevoDato El nuevo dato que se quiere establecer.
     * @param idLibro El ID del libro que se quiere modificar.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    boolean modificarLibro(String accion, String nuevoDato, int idLibro);
}

