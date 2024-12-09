package biblioteca2;

/**
 * Esta clase representa un libro. Guarda la información básica como el nombre,
 * autor, editorial y categoría del libro.
 */
public class Libro {

    private String nombre;
    private String autor;
    private String editorial;
    private int categoria;

    /**
     * Constructor vacío. Se usa cuando no tienes valores iniciales para el
     * libro.
     */
    public Libro() {
    }

    /**
     * Constructor que recibe todos los detalles de un libro.
     *
     * @param nombre El nombre del libro.
     * @param autor El autor del libro.
     * @param editorial La editorial que publicó el libro.
     * @param categoria La categoría del libro.
     */
    public Libro(String nombre, String autor, String editorial, int categoria) {
        this.nombre = nombre;
        this.autor = autor;
        this.editorial = editorial;
        this.categoria = categoria;
    }

    /**
     * Obtiene el nombre del libro.
     *
     * @return El nombre del libro.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del libro.
     *
     * @param nombre El nombre del libro.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el autor del libro.
     *
     * @return El autor del libro.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Establece el autor del libro.
     *
     * @param autor El autor del libro.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtiene la editorial del libro.
     *
     * @return La editorial del libro.
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Establece la editorial del libro.
     *
     * @param editorial La editorial que publicó el libro.
     */
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    /**
     * Obtiene la categoría del libro.
     *
     * @return La categoría del libro (como un número).
     */
    public int getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del libro.
     *
     * @param categoria La categoría del libro (como un número).
     */
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    /**
     * Método que devuelve un libro en cadena de carácteres.
     *
     * @return Una cadena que describe el libro.
     */
    @Override
    public String toString() {
        return "Libro{" + "nombre=" + nombre + ", autor=" + autor + ", editorial=" + editorial + ", categoria=" + categoria + '}';
    }

}
