package com.mycompany.sistemadegestiondelibrosbiblioteca;

import com.mycompany.sistemadegestiondelibrosbibliioteca.config.HibernateUtil;
import com.mycompany.sistemadegestiondelibrosbibliioteca.controller.LibroController;
import com.mycompany.sistemadegestiondelibrosbibliioteca.view.BibliotecaView;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.ILibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAOImpl;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;

/**
 * BibliotecaApp - Aplicación principal migrada a Hibernate
 * Inicializa datos de ejemplo y ejecuta sistema interactivo
 */
public class BibliotecaApp {

    public static void main(String[] args) {
        try {
            // Inicializar Hibernate (reemplaza DatabaseConfig)
            HibernateUtil.inicializar();
            System.out.println("🚀 Sistema de Gestión de Libros - Hibernate ORM");
            System.out.println("=" .repeat(50));

            // Insertar libros de ejemplo
            insertarLibrosEjemplo();

            // Ejecutar sistema interactivo MVC
           // ejecutarSistemaInteractivo();

        } catch (Exception e) {
            System.err.println("❌ Error en la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar Hibernate
            HibernateUtil.cerrarSessionFactory();
            System.out.println("👋 Sistema finalizado");
        }
    }

    /**
     * Insertar libros de ejemplo usando Hibernate
     */
    private static void insertarLibrosEjemplo() {
        System.out.println("\n📚 Insertando libros de ejemplo...");

        ILibroDAO dao = new LibroDAOImpl();

        // Libro 1
        Libro libro1 = new Libro();
        libro1.setTitulo("Carrie");
        libro1.setAutor("Stephen King");
        libro1.setAnoPublicacion(1986);
        libro1.setDisponible(true);

        try {
            dao.create(libro1);
            System.out.println("✅ Libro 1 insertado: " + libro1.getTitulo());
        } catch (Exception e) {
            System.err.println("⚠️ Libro 1 ya existe o error: " + e.getMessage());
        }

        // Libro 2
        Libro libro2 = new Libro();
        libro2.setTitulo("Choque de reyes");
        libro2.setAutor("R.R. Martin");
        libro2.setAnoPublicacion(1998);
        libro2.setDisponible(true);

        try {
            dao.create(libro2);
            System.out.println("✅ Libro 2 insertado: " + libro2.getTitulo());
        } catch (Exception e) {
            System.err.println("⚠️ Libro 2 ya existe o error: " + e.getMessage());
        }

        // Libro 3
        Libro libro3 = new Libro();
        libro3.setTitulo("Colorado Kid");
        libro3.setAutor("Stephen King");
        libro3.setAnoPublicacion(2002);
        libro3.setDisponible(true);

        try {
            dao.create(libro3);
            System.out.println("✅ Libro 3 insertado: " + libro3.getTitulo());
        } catch (Exception e) {
            System.err.println("⚠️ Libro 3 ya existe o error: " + e.getMessage());
        }

        System.out.println("📚 Inserción de libros completada\n");
    }

    /**
     * Ejecutar sistema interactivo MVC
     */
    private static void ejecutarSistemaInteractivo() {
        System.out.println("🎯 Iniciando sistema interactivo...\n");

        // Inicializar componentes MVC
        BibliotecaView view = new BibliotecaView();
        LibroController controller = new LibroController(view);

        // Ejecutar sistema interactivo
        view.ejecutarSistemaInteractivo(controller);
    }
}