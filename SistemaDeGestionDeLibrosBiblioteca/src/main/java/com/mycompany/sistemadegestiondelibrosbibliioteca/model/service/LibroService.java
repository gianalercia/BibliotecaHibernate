package com.mycompany.sistemadegestiondelibrosbibliioteca.model.service;

import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.ILibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAOImpl;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;

/**
 * LibroService - Capa de lógica de negocio simplificada
 * Solo se enfoca en crear libros con validaciones básicas
 */
public class LibroService {

    private ILibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAOImpl();
    }

    /**
     * Agregar nuevo libro - Método principal para POST
     */
    public Libro agregarLibro(String titulo, String autor, Integer anoPublicacion) {
        // Validaciones de negocio
        validarTitulo(titulo);
        validarAutor(autor);
        validarAnoPublicacion(anoPublicacion);

        try {
            // Crear entidad
            Libro nuevoLibro = new Libro(titulo.trim(), autor.trim(), anoPublicacion);

            // Guardar usando DAO (que maneja duplicados)
            return libroDAO.create(nuevoLibro);

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error guardando libro: " + e.getMessage());
        }
    }

    // ========================================================================
    // VALIDACIONES SIMPLIFICADAS
    // ========================================================================

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (titulo.trim().length() < 2) {
            throw new IllegalArgumentException("El título debe tener al menos 2 caracteres");
        }
    }

    private void validarAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        if (autor.trim().length() < 2) {
            throw new IllegalArgumentException("El autor debe tener al menos 2 caracteres");
        }
    }

    private void validarAnoPublicacion(Integer anoPublicacion) {
        if (anoPublicacion == null) {
            throw new IllegalArgumentException("El año de publicación no puede ser nulo");
        }
        if (anoPublicacion <= 0) {
            throw new IllegalArgumentException("El año de publicación debe ser válido");
        }
        int anoActual = java.time.Year.now().getValue();
        if (anoPublicacion > anoActual) {
            throw new IllegalArgumentException("El año de publicación no puede ser futuro");
        }
    }
}