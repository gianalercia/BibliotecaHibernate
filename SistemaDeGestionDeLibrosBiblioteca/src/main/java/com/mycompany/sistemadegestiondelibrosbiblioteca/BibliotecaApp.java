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
            System.out.println("🚀 Sistema de Gestión de Libros - POST con Hibernate + MVC");
            System.out.println("=".repeat(65));
            System.out.println("📋 Arquitectura: Main → Controller → Service → DAO → Hibernate → SQLite");
            System.out.println("📋 Funcionalidad: Solo POST con validación de duplicados");

            // Crear Controller (que coordina Service y View internamente)
            LibroController controller = new LibroController();

            // Agregar libros de ejemplo usando arquitectura MVC completa
            System.out.println("\n📚 Iniciando operaciones POST a través del Controller MVC...\n");

            // Libro 1
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #1: Agregando 'Carrie'");
            try {
                Libro libro1 = controller.agregarLibro("Carrie", "Stephen King", 1974);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro1);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            // Libro 2
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #2: Agregando 'Choque de reyes'");
            try {
                Libro libro2 = controller.agregarLibro("Choque de reyes", "George R.R. Martin", 1998);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro2);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            // Libro 3
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #3: Agregando 'El resplandor'");
            try {
                Libro libro3 = controller.agregarLibro("El resplandor", "Stephen King", 1977);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro3);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            // Libro 4 (duplicado para probar validación)
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #4: Intentando duplicado 'Carrie' (debe fallar)");
            try {
                Libro libro4 = controller.agregarLibro("Carrie", "Stephen King", 1974);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro4);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado (esperado): " + e.getMessage());
            }

            // Libro 5
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #5: Agregando '1984'");
            try {
                Libro libro5 = controller.agregarLibro("1984", "George Orwell", 1949);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro5);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            // Libro 6 (con error de validación)
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #6: Intentando año futuro (debe fallar)");
            try {
                Libro libro6 = controller.agregarLibro("Libro del futuro", "Autor X", 2030);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro6);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado (esperado): " + e.getMessage());
            }

            // Libro 7 (título vacío)
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #7: Intentando título vacío (debe fallar)");
            try {
                Libro libro7 = controller.agregarLibro("", "Autor Y", 2020);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro7);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado (esperado): " + e.getMessage());
            }

            // Libro 8 (exitoso final)
            System.out.println("=".repeat(50));
            System.out.println("📖 POST #8: Agregando 'Dune'");
            try {
                Libro libro8 = controller.agregarLibro("Dune", "Frank Herbert", 1965);
                System.out.println("✅ Main: Libro recibido del Controller: " + libro8);
            } catch (Exception e) {
                System.out.println("❌ Main: Error capturado: " + e.getMessage());
            }

            System.out.println("\n" + "=".repeat(65));
            System.out.println("✅ Proceso de POST completado");
            System.out.println("📊 Se procesaron 8 operaciones POST (algunas fallaron intencionalmente)");
            System.out.println("🔍 Revisa los logs de Hibernate para ver las operaciones SQL");

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