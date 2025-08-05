package com.mycompany.sistemadegestiondelibrosbiblioteca;

import com.mycompany.sistemadegestiondelibrosbibliioteca.config.HibernateUtil;
import com.mycompany.sistemadegestiondelibrosbibliioteca.controller.LibroController;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;

/**
 * BibliotecaApp - Aplicación principal con arquitectura MVC simplificada
 * Demuestra POST con Hibernate usando Controller → Service → DAO → Hibernate → SQLite
 *
 * Flujo completo:
 * Main → Controller → Service → DAO → Hibernate → SQLite
 *         ↓
 *       View (presenta resultados)
 */
public class BibliotecaApp {

    public static void main(String[] args) {
        try {
            // Inicializar Hibernate
            HibernateUtil.inicializar();
            System.out.println("🚀 Sistema de Gestión de Libros - POST con Hibernate");
            System.out.println("=".repeat(65));
            System.out.println("📋 Arquitectura: Main → Controller → Service → DAO → Hibernate → SQLite");

            // Crear Controller (que coordina Service y View internamente)
            LibroController controller = new LibroController();

            // Agregar libros de ejemplo usando arquitectura MVC completa
            System.out.println("\n📚 Iniciando operaciones POST a través del Controller MVC...\n");

            // Libro 1
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #1:");
            try {
                Libro libro1 = controller.agregarLibro("Colorado Kid", "Stephen King", 1974);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro1);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            // Libro 2
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #2:");
            try {
                Libro libro2 = controller.agregarLibro("Choque de reyes", "George R.R. Martin", 1998);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro2);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            // Libro 3
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #3:");
            try {
                Libro libro3 = controller.agregarLibro("El resplandor", "Stephen King", 1977);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro3);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            System.out.println("\n" + "=".repeat(65));
            System.out.println("✅ Proceso de POST completado");

        } catch (Exception e) {
            System.err.println("❌ Error crítico en la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar Hibernate
            HibernateUtil.cerrar();
            System.out.println("👋 Sistema finalizado correctamente");
        }
    }
}