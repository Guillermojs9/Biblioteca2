package biblioteca2;

import java.util.ArrayList;

/**
 * Interfaz que define las operaciones CRUD para manejar categorías.
 */
public interface CategoriaDAO {

    /**
     * Verifica si una categoría existe en el sistema.
     *
     * @param categoria El nombre de la categoría a verificar.
     * @return true si la categoría existe, false en caso contrario.
     */
    boolean existeCategoria(String categoria);

    /**
     * Obtiene una lista de todas las categorías disponibles en el sistema.
     *
     * @return Un ArrayList con los nombres de todas las categorías.
     */
    ArrayList<String> obtenerCategorias();

    /**
     * Inserta una nueva categoría.
     *
     * @param categoriaInsert El nombre de la categoría a insertar.
     * @return true si la categoría fue insertada exitosamente, false si no.
     */
    boolean insertarCategoria(String categoriaInsert);

    /**
     * Elimina una categoría.
     *
     * @param categoriaDelete El nombre de la categoría a eliminar.
     * @return true si la categoría fue eliminada exitosamente, false si no.
     */
    boolean eliminarCategoria(String categoriaDelete);
}

