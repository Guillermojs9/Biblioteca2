package biblioteca2;

import java.util.ArrayList;

public interface CategoriaDAO {

    boolean existeCategoria(String categoria);
    
    ArrayList<String> obtenerCategorias();

    boolean insertarCategoria(String categoriaInsert);

    boolean eliminarCategoria(String categoriaDelete);
}
