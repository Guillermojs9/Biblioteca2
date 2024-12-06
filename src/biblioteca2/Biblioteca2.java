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
    private static final CategoriaDAOc CATEGORIA_DAO = new CategoriaDAOc(CON);
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
                    break;
                case GESTION_PRESTAMOS:
                    break;
                case GESTION_USUARIOS:
                    break;
                case SALIR:
                    salir = true;
            }
        } while (!salir);
    }

    public static void mostrarMenuGestionCategorias() {
        System.out.println("0. Salir \n1. Insertar nueva categoría\n2. Mostrar caegorías\n3. Eliminar una categoría");
    }

    public static void gestionCategorias() {
        boolean salir = false;
        do {
            mostrarMenuGestionCategorias();
            int opcion = LeerDatosTeclado.leerInt("Introduzca una opción: ", 0, 3);
            switch (opcion) {
                case INSERTAR_CATEGORIA:
                    String categoriaInsert = LeerDatosTeclado.leerString("Introduzca una nueva categoría:");
                    boolean insert = CATEGORIA_DAO.insertarCategoria(categoriaInsert);
                    if (CATEGORIA_DAO.existeCategoria(categoriaInsert)) {
                        if (insert) {
                            categorias.add(categoriaInsert);
                            System.out.println("Se ha insertado la categoría");
                        }
                    } else {
                        System.out.println("Esa categoría ya existe, no se puede insertar");
                    }
                    break;
                case MOSTRAR_CATEGORIAS:
                    mostrarCategorias();
                    break;
                case ELIMINAR_CATEGORIA:
                    mostrarCategorias();
                    String categoriaDelete = LeerDatosTeclado.leerString("Introduzca el nombre de la categoría para borrar:");
                    boolean delete = CATEGORIA_DAO.eliminarCategoria(categoriaDelete);
                    if (!CATEGORIA_DAO.existeCategoria(categoriaDelete)) {
                        if (delete) {
                            categorias.remove(categoriaDelete);
                            System.out.println("Se ha borrado la la categoría");
                        }
                    } else {
                        System.out.println("La categoría introducida no existe");
                    }
                    break;
                case SALIR:
                    salir = true;
            }
        } while (!salir);
    }

}
