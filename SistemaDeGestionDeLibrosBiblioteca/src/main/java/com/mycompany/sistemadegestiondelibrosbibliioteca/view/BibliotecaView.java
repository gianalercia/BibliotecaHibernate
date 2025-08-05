/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.view;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;

/**
 * BibliotecaView - Vista simplificada para mostrar resultados POST
 * Solo maneja la presentación básica de libros creados
 */
public class BibliotecaView {

    /**
     * Muestra un libro creado exitosamente
     */
    public void mostrarLibroCreado(Libro libro) {
        System.out.println("📄 VIEW: HTTP 201 CREATED");
        System.out.println("📄 VIEW: Libro creado exitosamente:");
        System.out.println("📄 VIEW: ID: " + libro.getId());
        System.out.println("📄 VIEW: Título: " + libro.getTitulo());
        System.out.println("📄 VIEW: Autor: " + libro.getAutor());
        System.out.println("📄 VIEW: Año: " + libro.getAnoPublicacion());
        System.out.println();
    }

    /**
     * Muestra errores de creación
     */
    public void mostrarError(String mensaje) {
        System.out.println("📄 VIEW: ❌ Error: " + mensaje);
        System.out.println();
    }

    /**
     * Muestra mensaje de operación exitosa
     */
    public void mostrarExito(String mensaje) {
        System.out.println("📄 VIEW: ✅ " + mensaje);
        System.out.println();
    }
}