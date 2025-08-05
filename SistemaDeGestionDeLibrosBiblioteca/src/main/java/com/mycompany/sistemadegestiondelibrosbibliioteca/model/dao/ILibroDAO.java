package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;

/**
 * ILibroDAO - Interfaz simplificada para operaciones básicas
 * Solo mantiene los métodos que realmente usamos
 */
public interface ILibroDAO {

    /**
     * CREATE - Crear nuevo libro
     */
    Libro create(Libro libro);

    /**
     * EXISTS - Verificar si existe libro con título y autor
     */
    boolean exists(String titulo, String autor);
}