package biblioteca2;

/**
 * Clase que representa un usuario.
 */
public class Usuario {

    private String nombre;
    private String apellidos;

    /**
     * Constructor por defecto de la clase Usuario.
     */
    public Usuario() {
    }

    /**
     * Constructor de la clase Usuario con parámetros.
     *
     * @param nombre El nombre del usuario.
     * @param apellidos Los apellidos del usuario.
     */
    public Usuario(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nuevo nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     *
     * @return Los apellidos del usuario.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     *
     * @param apellidos Los nuevos apellidos del usuario.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Devuelve un objeto Usuario en cadena de carácteres.
     *
     * @return Una cadena con la información del usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" + "nombre=" + nombre + ", apellidos=" + apellidos + '}';
    }

}
