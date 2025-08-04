package com.mycompany.sistemadegestiondelibrosbiblioteca;

import com.mycompany.sistemadegestiondelibrosbibliioteca.config.HibernateUtil;
import com.mycompany.sistemadegestiondelibrosbibliioteca.controller.LibroController;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.ILibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAOHibernateImpl;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import com.mycompany.sistemadegestiondelibrosbibliioteca.view.BibliotecaView;

public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Sistema de Gestión de Biblioteca con Hibernate");

        try {
            inicializarSistema();
            cargarDatosEjemplo();
            ejecutarInterfazUsuario();

        } catch (Exception e) {
            System.err.println("Error fatal en la aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            cerrarSistema();
        }
    }

    private static void inicializarSistema() {
        try {
            System.out.println("🔧 Inicializando Hibernate...");

            if (!HibernateUtil.isActive()) {
                throw new RuntimeException("No se pudo inicializar Hibernate");
            }

            System.out.println("✅ Hibernate inicializado correctamente");

        } catch (Exception e) {
            System.err.println("Error inicializando sistema: " + e.getMessage());
            throw new RuntimeException("Error al inicializar el sistema: " + e.getMessage(), e);
        }
    }

    private static void cargarDatosEjemplo() {
        System.out.println("📚 Cargando datos de ejemplo...");

        try {
            ILibroDAO dao = new LibroDAOHibernateImpl();

            Libro[] librosEjemplo = {
                    new Libro("It", "Stephen King", 1986),
                    new Libro("Choque de reyes", "George R.R. Martin", 1998),
                    new Libro("Colorado Kid", "Stephen King", 2005)
            };

            int librosCreados = 0;

            for (Libro libro : librosEjemplo) {
                try {
                    if (!dao.exists(libro.getTitulo(), libro.getAutor())) {
                        dao.create(libro);
                        librosCreados++;
                        System.out.println("✅ Libro creado: " + libro.getTitulo() + " - " + libro.getAutor());
                    } else {
                        System.out.println("⚠️ Libro ya existe: " + libro.getTitulo() + " - " + libro.getAutor());
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Error creando libro '" + libro.getTitulo() + "': " + e.getMessage());
                }
            }

            if (librosCreados > 0) {
                System.out.println("✅ " + librosCreados + " nuevos libros de ejemplo agregados");
            } else {
                System.out.println("ℹ️ Todos los libros de ejemplo ya existían");
            }

            int totalLibros = dao.readAll().size();
            System.out.println("📊 Total de libros en la base de datos: " + totalLibros);

        } catch (Exception e) {
            System.err.println("Error cargando datos de ejemplo: " + e.getMessage());
        }
    }

    private static void ejecutarInterfazUsuario() {
        System.out.println("🖥️ Iniciando interfaz de usuario...");

        try {
            BibliotecaView view = new BibliotecaView();
            LibroController controller = new LibroController(view);

            mostrarBienvenida();
            view.ejecutarSistemaInteractivo(controller);

            System.out.println("👋 Interfaz de usuario finalizada");

        } catch (Exception e) {
            System.err.println("Error en interfaz de usuario: " + e.getMessage());
            throw new RuntimeException("Error en la interfaz de usuario: " + e.getMessage(), e);
        }
    }

    private static void mostrarBienvenida() {
        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("    🏛️  SISTEMA DE GESTIÓN DE BIBLIOTECA - HIBERNATE  🏛️    ");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("✨ Características principales:");
        System.out.println("   🔹 Gestión completa de libros");
        System.out.println("   🔹 Persistencia con Hibernate ORM");
        System.out.println("   🔹 Base de datos SQLite");
        System.out.println("   🔹 Arquitectura MVC");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println();
    }

    private static void cerrarSistema() {
        try {
            System.out.println("🔒 Cerrando sistema...");
            HibernateUtil.shutdown();
            System.out.println("✅ Sistema cerrado correctamente");
            System.out.println("\n👋 ¡Hasta luego! Sistema cerrado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cerrar el sistema: " + e.getMessage());
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("🛑 Cerrando sistema...");
            HibernateUtil.shutdown();
        }));
    }
}