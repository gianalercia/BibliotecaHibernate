package com.mycompany.sistemadegestiondelibrosbibliioteca.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * HibernateUtil - Gestión del SessionFactory de Hibernate
 * Reemplaza la configuración JDBC manual
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = null;
    private static boolean inicializado = false;

    /**
     * Inicializar Hibernate SessionFactory
     */
    public static void inicializar() {
        if (inicializado && sessionFactory != null && !sessionFactory.isClosed()) {
            return;
        }

        try {
            // Crear SessionFactory desde hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            sessionFactory = configuration.buildSessionFactory();
            inicializado = true;

            System.out.println("✅ Hibernate inicializado correctamente");
            System.out.println("📂 Base de datos: biblioteca.db");

        } catch (Exception e) {
            System.err.println("❌ Error inicializando Hibernate: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Obtener SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (!inicializado || sessionFactory == null || sessionFactory.isClosed()) {
            inicializar();
        }
        return sessionFactory;
    }

    /**
     * Cerrar SessionFactory
     */
    public static void cerrarSessionFactory() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
                inicializado = false;
                System.out.println("✅ Hibernate cerrado correctamente");
            }
        } catch (Exception e) {
            System.err.println("❌ Error cerrando Hibernate: " + e.getMessage());
        }
    }
}