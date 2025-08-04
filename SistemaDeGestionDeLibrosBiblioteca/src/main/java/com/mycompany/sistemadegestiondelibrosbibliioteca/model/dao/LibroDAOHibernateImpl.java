package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

import com.mycompany.sistemadegestiondelibrosbibliioteca.config.HibernateUtil;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LibroDAOHibernateImpl implements ILibroDAO {

    @Override
    public Libro create(Libro libro) {
        sanitizarLibro(libro);

        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            if (existsInSession(session, libro.getTitulo(), libro.getAutor())) {
                throw new RuntimeException("El libro '" + libro.getTitulo() + "' de " + libro.getAutor() + " ya existe");
            }

            session.save(libro);
            transaction.commit();

            System.out.println("Registros insertados");
            return libro;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al crear libro: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Libro> read(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Libro libro = session.get(Libro.class, id);
            return libro != null ? Optional.of(libro) : Optional.empty();
        } catch (Exception e) {
            System.err.println("Error en READ: " + e.getMessage());
            return Optional.empty();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Libro update(Libro libro) {
        if (libro.getId() == null) {
            throw new IllegalArgumentException("ID requerido para actualizar");
        }

        sanitizarLibro(libro);

        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Libro existente = session.get(Libro.class, libro.getId());
            if (existente == null) {
                throw new RuntimeException("Libro no encontrado para actualizar con ID: " + libro.getId());
            }

            Query<Libro> query = session.createNamedQuery("Libro.findByTituloAndAutor", Libro.class);
            query.setParameter("titulo", libro.getTitulo());
            query.setParameter("autor", libro.getAutor());

            List<Libro> duplicados = query.getResultList();
            boolean hayDuplicado = duplicados.stream()
                    .anyMatch(l -> !l.getId().equals(libro.getId()));

            if (hayDuplicado) {
                throw new RuntimeException("Ya existe otro libro con el mismo título y autor");
            }

            existente.setTitulo(libro.getTitulo());
            existente.setAutor(libro.getAutor());
            existente.setAnoPublicacion(libro.getAnoPublicacion());
            existente.setDisponible(libro.getDisponible());

            session.update(existente);
            transaction.commit();

            return existente;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Libro libro = session.get(Libro.class, id);

            if (libro != null) {
                session.delete(libro);
                transaction.commit();
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Libro> readAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Libro> query = session.createNamedQuery("Libro.findAll", Libro.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener lista de libros: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean exists(String titulo, String autor) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return existsInSession(session, titulo, autor);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private boolean existsInSession(Session session, String titulo, String autor) {
        try {
            Query<Libro> query = session.createNamedQuery("Libro.findByTituloAndAutor", Libro.class);
            query.setParameter("titulo", sanitizarTexto(titulo));
            query.setParameter("autor", sanitizarTexto(autor));
            query.setMaxResults(1);

            boolean existe = !query.getResultList().isEmpty();

            if (existe) {
                System.out.println("DUPLICADO DETECTADO: " + titulo + " - " + autor);
            }

            return existe;

        } catch (Exception e) {
            System.err.println("Error verificando existencia: " + e.getMessage());
            return false;
        }
    }

    private void sanitizarLibro(Libro libro) {
        if (libro.getTitulo() != null) {
            libro.setTitulo(sanitizarTexto(libro.getTitulo()));
        }
        if (libro.getAutor() != null) {
            libro.setAutor(sanitizarTexto(libro.getAutor()));
        }
        if (libro.getDisponible() == null) {
            libro.setDisponible(true);
        }
    }

    private String sanitizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }

        return texto.trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[<>\"'&]", "")
                .replaceAll("(?i)(script|select|insert|update|delete|drop|create|alter)", "");
    }
}