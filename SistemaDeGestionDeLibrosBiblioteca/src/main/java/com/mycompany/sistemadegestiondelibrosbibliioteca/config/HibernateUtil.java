package com.mycompany.sistemadegestiondelibrosbibliioteca.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory = null;

    public static void inicializar() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            return;
        }

        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();

            System.out.println("✅ Hibernate inicializado");

        } catch (Exception e) {
            System.err.println("❌ Error inicializando Hibernate: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            inicializar();
        }
        return sessionFactory;
    }

    public static void cerrar() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
                System.out.println("✅ Hibernate cerrado");
            }
        } catch (Exception e) {
            System.err.println("❌ Error cerrando Hibernate: " + e.getMessage());
        }
    }
}