package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

import com.mycompany.sistemadegestiondelibrosbibliioteca.config.HibernateUtil;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class LibroDAOImpl implements ILibroDAO {

    /**
     * CREATE - Insertar nuevo libro con Hibernate
     */
    @Override
    public Libro create(Libro libro) {
        // Sanitizar datos
        String tituloSanitizado = sanitizarTexto(libro.getTitulo());
        String autorSanitizado = sanitizarTexto(libro.getAutor());

        // Validar duplicados
        if (exists(tituloSanitizado, autorSanitizado)) {
            throw new RuntimeException("El libro '" + tituloSanitizado + "' de " + autorSanitizado + " ya existe");
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Establecer datos sanitizados
            libro.setTitulo(tituloSanitizado);
            libro.setAutor(autorSanitizado);
            if (libro.getDisponible() == null) {
                libro.setDisponible(true);
            }

            // Hibernate maneja el ID automáticamente
            session.persist(libro);
            transaction.commit();

            System.out.println("✅ Libro creado con Hibernate - ID: " + libro.getId());
            return libro;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al crear libro: " + e.getMessage());
        }
    }

    /**
     * READ - Buscar libro por ID con Hibernate
     */
    @Override
    public Optional<Libro> read(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }

        System.out.println("🔍 DAO READ: Buscando libro ID " + id + " con Hibernate");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Libro libro = session.get(Libro.class, id);

            if (libro != null) {
                System.out.println("✅ READ exitoso - Libro encontrado: " + libro.getTitulo());
                return Optional.of(libro);
            } else {
                System.out.println("❌ READ - Libro no encontrado");
                return Optional.empty();
            }

        } catch (Exception e) {
            System.err.println("❌ Error en READ: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * UPDATE - Actualizar libro existente con Hibernate
     */
    @Override
    public Libro update(Libro libro) {
        if (libro.getId() == null) {
            throw new IllegalArgumentException("ID requerido para actualizar");
        }

        // Sanitizar datos
        String tituloSanitizado = sanitizarTexto(libro.getTitulo());
        String autorSanitizado = sanitizarTexto(libro.getAutor());

        Transaction transaction = null;
        System.out.println("📝 DAO UPDATE: Actualizando libro ID " + libro.getId() + " con Hibernate");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Buscar libro existente
            Libro libroExistente = session.get(Libro.class, libro.getId());
            if (libroExistente == null) {
                throw new RuntimeException("Libro no encontrado para actualizar");
            }

            // Actualizar campos
            libroExistente.setTitulo(tituloSanitizado);
            libroExistente.setAutor(autorSanitizado);
            libroExistente.setAnoPublicacion(libro.getAnoPublicacion());
            if (libro.getDisponible() != null) {
                libroExistente.setDisponible(libro.getDisponible());
            }

            // Hibernate detecta cambios automáticamente
            session.merge(libroExistente);
            transaction.commit();

            System.out.println("✅ UPDATE exitoso - Libro actualizado");
            return libroExistente;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Error en UPDATE: " + e.getMessage());
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage());
        }
    }

    /**
     * DELETE - Eliminar libro por ID con Hibernate
     */
    @Override
    public boolean delete(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        Transaction transaction = null;
        System.out.println("🗑️ DAO DELETE: Eliminando libro ID " + id + " con Hibernate");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Libro libro = session.get(Libro.class, id);
            if (libro != null) {
                session.remove(libro);
                transaction.commit();
                System.out.println("✅ DELETE exitoso - Libro eliminado");
                return true;
            } else {
                System.out.println("❌ DELETE - Libro no encontrado");
                return false;
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Error en DELETE: " + e.getMessage());
            return false;
        }
    }

    /**
     * READ ALL - Obtener todos los libros con Hibernate
     */
    @Override
    public List<Libro> readAll() {
        System.out.println("📋 DAO READ ALL: Obteniendo todos los libros con Hibernate");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Libro> query = session.createQuery("FROM Libro ORDER BY id", Libro.class);
            List<Libro> libros = query.getResultList();

            System.out.println("✅ READ ALL exitoso - " + libros.size() + " libros encontrados");
            return libros;

        } catch (Exception e) {
            System.err.println("❌ Error en READ ALL: " + e.getMessage());
            return List.of(); // Lista vacía en caso de error
        }
    }

    /**
     * EXISTS - Verificar si existe libro con título y autor usando HQL
     */
    @Override
    public boolean exists(String titulo, String autor) {
        String tituloSanitizado = sanitizarTexto(titulo);
        String autorSanitizado = sanitizarTexto(autor);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(l) FROM Libro l WHERE LOWER(TRIM(l.titulo)) = LOWER(TRIM(:titulo)) " +
                            "AND LOWER(TRIM(l.autor)) = LOWER(TRIM(:autor))", Long.class);

            query.setParameter("titulo", tituloSanitizado);
            query.setParameter("autor", autorSanitizado);

            Long count = query.getSingleResult();
            boolean existe = count > 0;

            if (existe) {
                System.out.println("DUPLICADO DETECTADO: " + tituloSanitizado + " - " + autorSanitizado);
            }
            return existe;

        } catch (Exception e) {
            System.err.println("Error verificando duplicados: " + e.getMessage());
            return false;
        }
    }

    // ========================================================================
    // MÉTODOS AUXILIARES (sin cambios)
    // ========================================================================

    private String sanitizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }

        return texto.trim()
                .replaceAll("\\s+", " ")              // Espacios múltiples → uno solo
                .replaceAll("[<>\"'&]", "")           // Caracteres peligrosos
                .replaceAll("(?i)(script|select|insert|update|delete|drop|create|alter)", ""); // SQL keywords
    }
}