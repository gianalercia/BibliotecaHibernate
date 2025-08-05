/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.controller;

import com.mycompany.sistemadegestiondelibrosbibliioteca.model.service.LibroService;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import com.mycompany.sistemadegestiondelibrosbibliioteca.view.BibliotecaView;

/**
 * LibroController - Controlador simplificado para POST
 * Coordina entre Service (Model) y View
 */
public class LibroController {
    private LibroService libroService;
    private BibliotecaView view;

    public LibroController() {
        this.libroService = new LibroService();
        this.view = new BibliotecaView();
    }

    /**
     * Endpoint: POST /libros
     * Maneja la petición de creación de nuevo libro
     * Coordina Model y View
     */
    public Libro agregarLibro(String titulo, String autor, Integer anoPublicacion) {
        try {
            System.out.println("🎯 Controller: Procesando POST /libros");

            // Coordina con el SERVICE para crear el libro
            Libro nuevoLibro = libroService.agregarLibro(titulo, autor, anoPublicacion);

            // Coordina con la VIEW para mostrar resultado exitoso
            view.mostrarLibroCreado(nuevoLibro);

            System.out.println("🎯 Controller: POST exitoso");
            return nuevoLibro;

        } catch (IllegalArgumentException e) {
            // Error de validación - mostrar en view y propagar
            view.mostrarError("Datos inválidos: " + e.getMessage());
            System.out.println("🎯 Controller: Error de validación");
            throw e;

        } catch (Exception e) {
            // Error interno - mostrar en view y propagar
            view.mostrarError("Error interno: " + e.getMessage());
            System.out.println("🎯 Controller: Error interno");
            throw new RuntimeException("Error interno del servidor: " + e.getMessage());
        }
    }
}