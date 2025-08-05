package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

import com.mycompany.sistemadegestiondelibrosbibliioteca.config.HibernateUtil;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class LibroDAOImpl implements ILibroDAO {

    /**
     * CREATE - Insertar nuevo libro
     */
    @Override
    public Libro create(Libro libro) {
        // Verificar duplicados
        if (exists(libro.getTitulo(), libro.getAutor())) {
            throw new RuntimeException("❌ El libro '" + libro.getTitulo() + "' de " + libro.getAutor() + " ya existe");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(libro);
            transaction.commit();

            System.out.println("✅ Libro creado: " + libro);
            return libro;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al crear libro: " + e.getMessage());
        }
    }

    /**
     * EXISTS - Verificar si existe libro con título y autor
     */
    @Override
    public boolean exists(String titulo, String autor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(l) FROM Libro l WHERE LOWER(l.titulo) = LOWER(:titulo) AND LOWER(l.autor) = LOWER(:autor)",
                    Long.class);

            query.setParameter("titulo", titulo.trim());
            query.setParameter("autor", autor.trim());

            Long count = query.getSingleResult();
            return count > 0;

        } catch (Exception e) {
            System.err.println("Error verificando duplicados: " + e.getMessage());
            return false;
        }
    }
}