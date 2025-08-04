package com.mycompany.sistemadegestiondelibrosbibliioteca.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    static {
        try {
            inicializarSessionFactory();
        } catch (Exception e) {
            System.err.println("Error inicializando SessionFactory: " + e.getMessage());
            throw new ExceptionInInitializerError("Error inicializando Hibernate: " + e.getMessage());
        }
    }

    private static void inicializarSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            // AGREGAR: Asegurar que se escanee la entidad
            configuration.addAnnotatedClass(com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro.class);

            registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(registry);

            // Verificar conexión
            sessionFactory.openSession().close();

        } catch (Exception e) {
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            throw new RuntimeException("No se pudo inicializar Hibernate: " + e.getMessage(), e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            inicializarSessionFactory();
        }
        return sessionFactory;
    }

    public static void shutdown() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        } catch (Exception e) {
            System.err.println("Error al cerrar Hibernate: " + e.getMessage());
        }
    }

    public static boolean isActive() {
        return sessionFactory != null && !sessionFactory.isClosed();
    }
}