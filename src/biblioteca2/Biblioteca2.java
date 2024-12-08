package biblioteca2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Este clase muestra el funcionamiento básico de un programa jdbc que está
 * asociado a una base de datos
 *
 * @author guillermo
 */
public class Biblioteca2 {

    private static final String URL = "jdbc:mariadb://172.26.157.64:3306/BIBLIOTECA";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final Connection CON = ConexionSQL.getInstance();
    private static final CategoriaDAOImpl CATEGORIA_DAO = new CategoriaDAOImpl(CON);
    private static final LibroDAOImpl LIBRO_DAO = new LibroDAOImpl(CON);
    private static final UsuarioDAOImpl USUARIO_DAO = new UsuarioDAOImpl(CON);
    private static final PrestamoDAOImpl PRESTAMO_DAO = new PrestamoDAOImpl(CON);
    private static final int VACIAR_TABLAS = 1;
    private static final int RELLENAR_CATEGORIAS = 2;
    private static final int CARGAR_FICHERO = 3;
    private static final int MOSTRAR_LIBROS_PRESTADOS = 4;
    private static final int MOSTRAR_LIBROS_POR_CATEGORIA = 5;
    private static final int GESTION_ENTIDADES = 6;
    private static final int GESTION_CATEGORIAS = 1;
    private static final int GESTION_LIBROS = 2;
    private static final int GESTION_PRESTAMOS = 3;
    private static final int GESTION_USUARIOS = 4;
    private static final int INSERTAR_CATEGORIA = 1;
    private static final int MOSTRAR_CATEGORIAS = 2;
    private static final int ELIMINAR_CATEGORIA = 3;
    private static final int INSERTAR_LIBRO = 1;
    private static final int CONSULTAR_LIBROS = 2;
    private static final int MODIFICAR_LIBRO = 3;
    private static final int ELIMINAR_LIBRO = 4;
    private static final int MODIFICAR_TITULO = 1;
    private static final int MODIFICAR_AUTOR = 2;
    private static final int MODIFICAR_EDITORIAL = 3;
    private static final int MODIFICAR_CATEGORIA = 4;
    private static final int INSERTAR_USUARIO = 1;
    private static final int CONSULTAR_USUARIOS = 2;
    private static final int MODIFICAR_USUARIO = 3;
    private static final int ELIMINAR_USUARIO = 4;
    private static final int MODIFICAR_NOMBRE = 1;
    private static final int MODIFICAR_APELLIDOS = 2;
    private static final int PRESTAR_LIBRO = 1;
    private static final int DEVOLVER_LIBRO = 2;
    private static final int SALIR = 0;
    private static final String[] OPCIONES_MENU_PRINCIPAL
            = {
                "Salir",
                "Vaciar todas las tablas",
                "Rellenar tabla categorías con valores por defecto",
                "Cargar fichero script (datos.sql)",
                "Mostrar todos los libros prestados",
                "Mostrar libro por categoría",
                "Gestión entidades"
            };
    private static ArrayList<String> categorias = CATEGORIA_DAO.obtenerCategorias();
    private static HashMap<Integer, Libro> libros = LIBRO_DAO.obtenerLibros();
    private static HashMap<Integer, Usuario> usuarios = USUARIO_DAO.obtenerUsuarios();
    private static HashMap<Integer, Libro> librosLibres = PRESTAMO_DAO.obtenerLibrosLibres();
    private static HashMap<Integer, Libro> librosPrestados = PRESTAMO_DAO.obtenerLibrosPrestados();
    private static final String[] MENU_GESTION
            = {
                "Salir",
                "Gestión de categorías",
                "Gestión de libros",
                "Gestión de prestámos",
                "Gestión de usuarios"
            };

    /**
     * Main de mi programa que se encarga del funcionamiento principal,
     * gestionando el menú con las diferentes opciones
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            boolean salir = false;
            do {
                mostrarMenuPrincipal();
                int opcion = LeerDatosTeclado.leerInt("Selecciona una opción del menú:", 0, 6);
                System.out.println();
                switch (opcion) {
                    case VACIAR_TABLAS:
                        vaciarTablas();
                        break;
                    case RELLENAR_CATEGORIAS:
                        defaultCategorias();
                        break;
                    case CARGAR_FICHERO:
                        File f = new File("./ficheros/datos.sql");
                        ejecutarScript(f);
                        break;
                    case MOSTRAR_LIBROS_PRESTADOS:
                        mostrarLibrosPrestados();
                        break;
                    case MOSTRAR_LIBROS_POR_CATEGORIA:
                        mostrarLibrosPorCategoria();
                        break;
                    case GESTION_ENTIDADES:
                        menuGestionEntidades();
                        break;
                    case SALIR:
                        salir = true;
                }
            } while (!salir);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Biblioteca2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Muestra el menú principal
     */
    public static void mostrarMenuPrincipal() {
        System.out.println();
        for (int i = 0; i < OPCIONES_MENU_PRINCIPAL.length; i++) {
            System.out.println(i + "." + OPCIONES_MENU_PRINCIPAL[i]);
        }
        System.out.println();
    }

    /**
     * Muestra el menú de las categorías
     */
    public static void mostrarCategorias() {
        System.out.println();
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + "-." + categorias.get(i));
        }
        System.out.println();
    }

    public static void mostrarMenuGestion() {
        System.out.println();
        for (int i = 0; i < MENU_GESTION.length; i++) {
            System.out.println(i + "-." + MENU_GESTION[i]);
        }
    }

    /**
     * Vacia las tablas de la base de datos
     */
    public static void vaciarTablas() {
        try {
            Statement stmt = CON.createStatement();
            String[] tablas = {"prestamos", "usuario", "libro", "categoria"};
            CON.setAutoCommit(false);
            for (String tabla : tablas) {
                String sql = "DELETE FROM " + tabla;
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            CON.commit();
            System.out.println("Se han vaciado todas las tablas \n");
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar la consulta. Error: " + ex.getMessage());
        }
    }

    /**
     * Inserta las categorrías de los libros
     */
    public static void defaultCategorias() {
        try {
            boolean categoriasImportadas = true;
            CON.setAutoCommit(false);
            Statement stmt = CON.createStatement();
            BufferedReader br = new BufferedReader(new FileReader("./ficheros/categorias.sql"));
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    stmt.execute(linea);
                } catch (SQLIntegrityConstraintViolationException ex) {
                    categoriasImportadas = false;
                }
            }
            CON.commit();
            CON.setAutoCommit(true);

            if (categoriasImportadas) {
                System.out.println("Se han importado todas las categorías");
            } else {
                System.out.println("No se ha realizado la operación porque se han intentado introducir claves duplicadas");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException e) {
                System.out.println("Error al hacer el rollback. Error: " + e.getMessage());
            }
        } catch (IOException ex) {
            System.out.println("Error leyendo el archivo. Error: " + ex.getMessage());
        }
    }

    /**
     * Ejecuta el archivo que se pasa por parámetro. Debe ser un archivo que
     * contenga instrucciones SQL, sino, salta excepción
     *
     * @param f Archivo que se quiere ejecutar
     */
    public static void ejecutarScript(File f) {
        try {
            CON.setAutoCommit(false);
            Statement stmt = CON.createStatement();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while ((linea = br.readLine()) != null) {
                stmt.execute(linea);
            }
            CON.commit();
            CON.setAutoCommit(true);
            System.out.println("Datos cargados correctamente \n");
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar el script. Error: " + ex.getMessage());
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                System.out.println("Error al hacer el rollback. Error: " + ex1.getMessage());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("El archivo especificado no existe. Error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Se ha producido un error leyendo el archivo: " + ex.getMessage());
        }

    }

    /**
     * Muestra por pantalla los libros prestados de la biblioteca realizando una
     * consulta a la base de datos.
     */
    public static void mostrarLibrosPrestados() {
        try {
            ResultSet rs = CON.createStatement().executeQuery("SELECT libro.id, libro.nombre as 'Nombre libro', autor, editorial, categoria, usuario.nombre as 'Nombre Usuario', usuario.apellidos, fechaPrestamo "
                    + "FROM libro INNER JOIN prestamos ON idLibro = libro.id "
                    + "INNER JOIN usuario ON idUsuario = usuario.id ORDER BY libro.nombre");
            while (rs.next()) {
                String id = rs.getString("id");
                String nombreLibro = rs.getString("Nombre Libro");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String categoria = rs.getString("categoria");
                String nombreUsuario = rs.getString("Nombre Usuario");
                String apellidosUsuario = rs.getString("apellidos");
                String fechaPrestamo = rs.getString("fechaPrestamo");
                System.out.println("ID: " + id + ", Nombre Libro: " + nombreLibro + ", Autor: " + autor
                        + ", Editorial: " + editorial + ", Categoria: " + categoria
                        + ", Nombre Usuario: " + nombreUsuario + ", Apellidos Usuario: " + apellidosUsuario
                        + ", Fecha Préstamo: " + fechaPrestamo);

            }
            System.out.println();
        } catch (SQLException ex) {
            System.out.println("La consulta introducida no es válida. Error: " + ex.getMessage());
        }
    }

    /**
     * Muestra por pantalla los libros de una determinada categoría haciendo una
     * consulta a la base de datos.
     */
    public static void mostrarLibrosPorCategoria() {
        try {
            mostrarCategorias();
            int numCategoria = LeerDatosTeclado.leerInt("Introduce el número de una categoría:", 1, categorias.size());
            System.out.println();
            String consulta = "SELECT * FROM libro WHERE UPPER(categoria) = UPPER(?)";
            PreparedStatement stmt = CON.prepareStatement(consulta);
            stmt.setInt(1, numCategoria);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                do {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    System.out.println("Id: " + id + ", Nombre: " + nombre);
                } while (rs.next());
                System.out.println();
            } else {
                System.out.println("No se ha encontrado ningún libro con esa categoría");
            }
        } catch (SQLException ex) {
            System.out.println("La consulta introducida no es válida. Error: " + ex.getMessage());
        }
    }

    public static void menuGestionEntidades() {
        boolean salir = false;
        do {
            mostrarMenuGestion();
            int opcion = LeerDatosTeclado.leerInt("Introduzca una opción", 0, MENU_GESTION.length);
            switch (opcion) {
                case GESTION_CATEGORIAS:
                    gestionCategorias();
                    break;
                case GESTION_LIBROS:
                    gestionLibros();
                    break;
                case GESTION_PRESTAMOS:
                    gestionPrestamos();
                    break;
                case GESTION_USUARIOS:
                    gestionUsuarios();
                    break;
                case SALIR:
                    salir = true;
            }
        } while (!salir);
    }

    public static void gestionPrestamos() {
        boolean salir = false;
        do {
            mostrarMenuGestionPrestamos();
            int opcion = LeerDatosTeclado.leerInt("Introduzca una opción", 0, 2);
            switch (opcion) {
                case PRESTAR_LIBRO:
                    prestarLibro();
                    break;
                case DEVOLVER_LIBRO:
                    devolverLibro();
                    break;
                case SALIR:
                    salir = true;
                    break;
            }
        } while (!salir);
    }

    public static void devolverLibro() {
        mostraHashLibrosPrestados();
        int idLibro = LeerDatosTeclado.leerInt("Introduzca el ID de libro que quiere devolver", 1);
        if (librosPrestados.containsKey(idLibro)) {
            Libro libro = librosPrestados.get(idLibro);
            boolean devolver = PRESTAMO_DAO.devolverLibro(idLibro);
            if (devolver) {
                librosLibres.put(idLibro, libro);
                System.out.println(libro);
                librosPrestados.remove(idLibro);
                System.out.println("Se ha devuelto el libro");
            }
        } else {
            System.out.println("No existe ningún libro prestado con ese ID");
        }
    }

    public static void prestarLibro() {
        mostrarLibrosLibres();
        int idLibro = LeerDatosTeclado.leerInt("Introduzca el ID de libro que quiere prestar", 1);
        if (librosLibres.containsKey(idLibro)) {
            Libro libro = librosLibres.get(idLibro);
            consultarUsuarios();
            int idUsuario = LeerDatosTeclado.leerInt("Introduzca el ID del usuario al que prestar el libro", 1);
            if (usuarios.containsKey(idUsuario)) {
                boolean insert = PRESTAMO_DAO.prestarLibro(idLibro, idUsuario);
                if (insert) {
                    librosPrestados.put(idLibro, libro);
                    librosLibres.remove(idLibro);
                    System.out.println("El libro se ha prestado");
                }
            } else {
                System.out.println("No existe ningún usuario con ese ID");
            }
        } else {
            System.out.println("No existe ningún libro libre con ese ID");
        }
    }

    public static void mostraHashLibrosPrestados() {
        for (Map.Entry<Integer, Libro> entry : librosPrestados.entrySet()) {
            Integer id = entry.getKey();
            Libro libro = entry.getValue();
            System.out.println("ID: " + id + ", " + libro);
        }
    }

    public static void mostrarLibrosLibres() {
        for (Map.Entry<Integer, Libro> entry : librosLibres.entrySet()) {
            Integer id = entry.getKey();
            Libro libro = entry.getValue();
            System.out.println("ID: " + id + ", " + libro);
        }
    }

    public static void mostrarMenuGestionPrestamos() {
        System.out.println("0. Salir \n1. Prestar libro\n2. Devolver libro");
    }

    public static void mostrarMenuGestionUsuarios() {
        System.out.println("0. Salir \n1. Insertar nuevo usuario\n2. Consultar usuarios\n3. Modificar un usuario\n4. Eliminar un usuario");
    }

    public static void eliminarUsuario() {
        consultarUsuarios();
        int idUsuarioDelete = LeerDatosTeclado.leerInt("Introduzca el ID del usuario que desea eliminar: ", 0);
        boolean delete = USUARIO_DAO.eliminarUsuario(idUsuarioDelete);
        if (delete) {
            usuarios.remove(idUsuarioDelete);
            System.out.println("El usuario se ha eliminado");
        } else {
            System.out.println("No existe ningún usuario con ese ID");
        }
    }

    public static void consultarUsuarios() {
        for (Map.Entry<Integer, Usuario> entry : usuarios.entrySet()) {
            Integer id = entry.getKey();
            Usuario usuario = entry.getValue();
            System.out.println("ID: " + id + ", " + usuario);
        }
    }

    public static void insertarUsuario() {
        String nombre = LeerDatosTeclado.leerString("Introduzca el nombre de usuario:");
        String apellidos = LeerDatosTeclado.leerString("Introduzca los apellidos del usuario:");
        Usuario usuarioInsert = new Usuario(nombre, apellidos);
        boolean insert = USUARIO_DAO.insertarUsuario(usuarioInsert);
        if (insert) {
            int nuevoId = USUARIO_DAO.obtenerUltimoId();
            usuarios.put(nuevoId, usuarioInsert);
            System.out.println("Se ha insertado el usuario");
        } else {
            System.out.println("Error al insertar el usuario");
        }
    }

    public static void modificarUsuario() {
        consultarUsuarios();
        int idUsuarioUpdate = LeerDatosTeclado.leerInt("Introduzca el ID del usuario que desea modificar: ", 0);
        mostrarMenuModificarUsuarios();
        int opcion = LeerDatosTeclado.leerInt("Introduzca una opción:", 0, 2);
        switch (opcion) {
            case MODIFICAR_NOMBRE:
                String nuevoNombre = LeerDatosTeclado.leerString("Introduzca el nuevo nombre: ");
                if (USUARIO_DAO.modificarUsuario("nombre", nuevoNombre, idUsuarioUpdate)) {
                    Usuario usuarioActualizado = usuarios.get(idUsuarioUpdate);
                    usuarioActualizado.setNombre(nuevoNombre);
                    System.out.println("Nombre actualizado correctamente.");
                } else {
                    System.out.println("Error al actualizar el nombre.");
                }
                break;
            case MODIFICAR_APELLIDOS:
                String nuevosApellidos = LeerDatosTeclado.leerString("Introduzca los nuevos apellidos: ");
                if (USUARIO_DAO.modificarUsuario("apellidos", nuevosApellidos, idUsuarioUpdate)) {
                    Usuario usuarioActualizado = usuarios.get(idUsuarioUpdate);
                    usuarioActualizado.setApellidos(nuevosApellidos);
                    System.out.println("Apellidos actualizados correctamente.");
                } else {
                    System.out.println("Error al actualizar los apellidos.");
                }
                break;
        }
    }

    public static void mostrarMenuModificarUsuarios() {
        System.out.println("1. Modificar nombre\n2. Modificar apellidos");
    }

    public static void gestionUsuarios() {
        boolean salir = false;
        do {
            mostrarMenuGestionUsuarios();
            int opcion = LeerDatosTeclado.leerInt("Introduzca una opción:", 0, 4);
            switch (opcion) {
                case INSERTAR_USUARIO:
                    insertarUsuario();
                    break;
                case CONSULTAR_USUARIOS:
                    consultarUsuarios();
                    break;
                case MODIFICAR_USUARIO:
                    modificarUsuario();
                    break;
                case ELIMINAR_USUARIO:
                    eliminarUsuario();
                    break;
                case SALIR:
                    salir = true;
                    break;
            }
        } while (!salir);
    }

    public static void mostrarMenuGestionLibros() {
        System.out.println("0. Salir \n1. Insertar nuevo libro\n2. Consultar libros\n3. Modificar un libro\n4. Eliminar un libro");
    }

    public static void gestionLibros() {
        boolean salir = false;
        do {
            mostrarMenuGestionLibros();
            int opcion = LeerDatosTeclado.leerInt("Introduzca una opción", 0, 4);
            switch (opcion) {
                case INSERTAR_LIBRO:
                    insertarLibro();
                    break;
                case CONSULTAR_LIBROS:
                    consultarLibros();
                    break;
                case MODIFICAR_LIBRO:
                    modificarLibro();
                    break;
                case ELIMINAR_LIBRO:
                    eliminarLibro();
                    break;
                case SALIR:
                    salir = true;
                    break;
            }
        } while (!salir);
    }

    public static void mostrarMenuModificarLibros() {
        System.out.println("1. Modificar título\n2. Modificar autor\n3. Modificar editorial\n4. Modificar categoría");
    }

    public static void modificarLibro() {
        consultarLibros();
        int idLibroUpdate = LeerDatosTeclado.leerInt("Introduzca el ID del libro que desea modificar: ", 0);

        mostrarMenuModificarLibros();
        int opcion = LeerDatosTeclado.leerInt("Introduzca una opción:", 0, 4);
        String nuevoDato;
        switch (opcion) {
            case MODIFICAR_TITULO:
                nuevoDato = LeerDatosTeclado.leerString("Introduzca el nuevo título: ");
                if (LIBRO_DAO.modificarLibro("titulo", nuevoDato, idLibroUpdate)) {
                    Libro libroActualizado = libros.get(idLibroUpdate);
                    libroActualizado.setNombre(nuevoDato);
                    System.out.println("Título actualizado correctamente.");
                } else {
                    System.out.println("Error al actualizar el título.");
                }
                break;
            case MODIFICAR_AUTOR:
                nuevoDato = LeerDatosTeclado.leerString("Introduzca el nuevo autor: ");
                if (LIBRO_DAO.modificarLibro("autor", nuevoDato, idLibroUpdate)) {
                    Libro libroActualizado = libros.get(idLibroUpdate);
                    libroActualizado.setAutor(nuevoDato);
                    System.out.println("Autor actualizado correctamente.");
                } else {
                    System.out.println("Error al actualizar el autor.");
                }
                break;
            case MODIFICAR_EDITORIAL:
                nuevoDato = LeerDatosTeclado.leerString("Introduzca la nueva editorial: ");
                if (LIBRO_DAO.modificarLibro("editorial", nuevoDato, idLibroUpdate)) {
                    Libro libroActualizado = libros.get(idLibroUpdate);
                    libroActualizado.setEditorial(nuevoDato);
                    System.out.println("Editorial actualizada correctamente.");
                } else {
                    System.out.println("Error al actualizar la editorial.");
                }
                break;
            case MODIFICAR_CATEGORIA:
                mostrarCategorias();
                int categoria = LeerDatosTeclado.leerInt("Selecciona la nueva categoría", 1, categorias.size());
                if (LIBRO_DAO.modificarLibro("categoria", Integer.toString(categoria), idLibroUpdate)) {
                    Libro libroActualizado = libros.get(idLibroUpdate);
                    libroActualizado.setCategoria(categoria);
                    System.out.println("Categoría actualizada correctamente.");
                } else {
                    System.out.println("Error al actualizar la categoría.");
                }
                break;
        }
    }

    public static void eliminarLibro() {
        consultarLibros();
        int idLibroDelete = LeerDatosTeclado.leerInt("Introduzca el ID del libro que desea eliminar: ", 0);
        boolean delete = LIBRO_DAO.eliminarLibro(idLibroDelete);
        if (delete) {
            libros.remove(idLibroDelete);
            System.out.println("El libro se ha eliminado");
        } else {
            System.out.println("No existe ningún libro con ese ID");
        }
    }

    public static void insertarLibro() {
        String nombre = LeerDatosTeclado.leerString("Introduzca el título del libro");
        String autor = LeerDatosTeclado.leerString("Introduzca el nombre del autor");
        String editorial = LeerDatosTeclado.leerString("Introduzca el nombre de la editorial");
        mostrarCategorias();
        int categoria = LeerDatosTeclado.leerInt("Selecciona la categoría del libro", 1, categorias.size());
        Libro libroInsert = new Libro(nombre, autor, editorial, categoria);
        boolean insert = LIBRO_DAO.insertarLibro(libroInsert);
        if (insert) {
            int nuevoId = LIBRO_DAO.obtenerUltimoId();
            libros.put(nuevoId, libroInsert);
            System.out.println("Se ha insertado el libro");
        } else {
            System.out.println("Error al insertar el libro");
        }
    }

    public static void consultarLibros() {
        for (Map.Entry<Integer, Libro> entry : libros.entrySet()) {
            Integer id = entry.getKey();
            Libro libro = entry.getValue();
            System.out.println("ID: " + id + ", " + libro);
        }
    }

    public static void mostrarMenuGestionCategorias() {
        System.out.println("0. Salir \n1. Insertar nueva categoría\n2. Consultar categorías\n3. Eliminar una categoría");
    }

    public static void gestionCategorias() {
        boolean salir = false;
        do {
            mostrarMenuGestionCategorias();
            int opcion = LeerDatosTeclado.leerInt("Introduzca una opción: ", 0, 3);
            switch (opcion) {
                case INSERTAR_CATEGORIA:
                    insertarCategoria();
                    break;
                case MOSTRAR_CATEGORIAS:
                    mostrarCategorias();
                    break;
                case ELIMINAR_CATEGORIA:
                    eliminarCategoria();
                    break;
                case SALIR:
                    salir = true;
            }
        } while (!salir);
    }

    public static void insertarCategoria() {
        String categoriaInsert = LeerDatosTeclado.leerString("Introduzca una nueva categoría:");
        if (CATEGORIA_DAO.existeCategoria(categoriaInsert)) {
            System.out.println("Esa categoría ya existe, no se puede insertar.");
        } else {
            boolean insert = CATEGORIA_DAO.insertarCategoria(categoriaInsert);
            if (insert) {
                categorias.add(categoriaInsert);
                System.out.println("Se ha insertado la categoría.");
            } else {
                System.out.println("Ocurrió un error al insertar la categoría.");
            }
        }
    }

    public static void eliminarCategoria() {
        mostrarCategorias();
        String categoriaDelete = LeerDatosTeclado.leerString("Introduzca el nombre de la categoría para borrar:");
        if (CATEGORIA_DAO.existeCategoria(categoriaDelete)) {
            boolean delete = CATEGORIA_DAO.eliminarCategoria(categoriaDelete);
            if (delete) {
                categorias.remove(categoriaDelete);
                System.out.println("Se ha borrado la categoría.");
            } else {
                System.out.println("Ocurrió un error al intentar borrar la categoría.");
            }
        } else {
            System.out.println("La categoría introducida no existe.");
        }
    }

}
