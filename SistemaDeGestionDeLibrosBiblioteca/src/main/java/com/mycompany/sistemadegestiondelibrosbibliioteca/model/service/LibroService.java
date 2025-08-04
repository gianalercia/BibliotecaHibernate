package com.mycompany.sistemadegestiondelibrosbibliioteca.model.service;

import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.ILibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAOHibernateImpl;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dto.LibroDTO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibroService {

    private final ILibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAOHibernateImpl();
    }

    public LibroService(ILibroDAO libroDAO) {
        this.libroDAO = libroDAO;
    }

    public LibroDTO obtenerLibroPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }

        try {
            Optional<Libro> libroOpt = libroDAO.read(id);

            if (!libroOpt.isPresent()) {
                throw new RuntimeException("Libro no encontrado con ID: " + id + " (Error 404)");
            }

            return convertirADTO(libroOpt.get());

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error accediendo a la base de datos: " + e.getMessage());
        }
    }

    public LibroDTO agregarLibro(String titulo, String autor, String anoPublicacionStr) {
        validarTitulo(titulo);
        validarAutor(autor);
        Integer anoPublicacion = validarAnoPublicacion(anoPublicacionStr);

        try {
            Libro nuevoLibro = new Libro();
            nuevoLibro.setTitulo(titulo.trim());
            nuevoLibro.setAutor(autor.trim());
            nuevoLibro.setAnoPublicacion(anoPublicacion);
            nuevoLibro.setDisponible(true);

            Libro libroGuardado = libroDAO.create(nuevoLibro);

            return convertirADTO(libroGuardado);

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error guardando libro: " + e.getMessage());
        }
    }

    public LibroDTO actualizarLibro(Long id, String titulo, String autor, String anoPublicacionStr) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para actualizar");
        }

        validarTitulo(titulo);
        validarAutor(autor);
        Integer anoPublicacion = validarAnoPublicacion(anoPublicacionStr);

        try {
            Optional<Libro> existente = libroDAO.read(id);
            if (!existente.isPresent()) {
                throw new RuntimeException("Libro no encontrado para actualizar");
            }

            Libro libro = existente.get();
            libro.setTitulo(titulo.trim());
            libro.setAutor(autor.trim());
            libro.setAnoPublicacion(anoPublicacion);

            Libro actualizado = libroDAO.update(libro);

            return convertirADTO(actualizado);

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error actualizando libro: " + e.getMessage());
        }
    }

    public boolean eliminarLibro(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para eliminar");
        }

        try {
            return libroDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error eliminando libro: " + e.getMessage());
        }
    }

    public List<LibroDTO> obtenerTodosLosLibros() {
        try {
            List<Libro> libros = libroDAO.readAll();

            return libros.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error listando libros: " + e.getMessage());
        }
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        if (titulo.trim().length() < 2) {
            throw new IllegalArgumentException("El título debe tener al menos 2 caracteres");
        }

        if (titulo.trim().length() > 200) {
            throw new IllegalArgumentException("El título no puede exceder 200 caracteres");
        }
    }

    private void validarAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }

        if (autor.trim().length() < 2) {
            throw new IllegalArgumentException("El autor debe tener al menos 2 caracteres");
        }

        if (autor.trim().length() > 100) {
            throw new IllegalArgumentException("El autor no puede exceder 100 caracteres");
        }
    }

    private Integer validarAnoPublicacion(String anoPublicacionStr) {
        if (anoPublicacionStr == null || anoPublicacionStr.trim().isEmpty()) {
            throw new IllegalArgumentException("El año de publicación no puede estar vacío");
        }

        Integer anoPublicacion;
        try {
            anoPublicacion = Integer.parseInt(anoPublicacionStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El año de publicación debe ser un número válido");
        }

        if (anoPublicacion <= 0) {
            throw new IllegalArgumentException("El año de publicación debe ser válido");
        }

        int anoActual = Year.now().getValue();
        if (anoPublicacion > anoActual) {
            throw new IllegalArgumentException("El año de publicación no puede ser futuro");
        }

        if (anoPublicacion < 1000) {
            throw new IllegalArgumentException("El año de publicación debe ser desde el año 1000");
        }

        return anoPublicacion;
    }

    private LibroDTO convertirADTO(Libro libro) {
        return new LibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAnoPublicacion()
        );
    }
}