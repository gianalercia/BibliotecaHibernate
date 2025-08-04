/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "libros")
@NamedQueries({
        @NamedQuery(
                name = "Libro.findAll",
                query = "SELECT l FROM Libro l ORDER BY l.id"
        ),
        @NamedQuery(
                name = "Libro.findByTituloAndAutor",
                query = "SELECT l FROM Libro l WHERE LOWER(TRIM(l.titulo)) = LOWER(TRIM(:titulo)) AND LOWER(TRIM(l.autor)) = LOWER(TRIM(:autor))"
        )
})
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // CAMBIAR ESTO
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @Column(name = "ano_publicacion", nullable = false)
    private Integer anoPublicacion;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;
    // Constructores
    public Libro() {}

    public Libro(String titulo, String autor, Integer anoPublicacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.disponible = true;
    }

    public Libro(Long id, String titulo, String autor, Integer anoPublicacion, Boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.disponible = disponible;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo != null ? titulo.trim() : null; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor != null ? autor.trim() : null; }

    public Integer getAnoPublicacion() { return anoPublicacion; }
    public void setAnoPublicacion(Integer anoPublicacion) { this.anoPublicacion = anoPublicacion; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible != null ? disponible : true; }

    @PrePersist
    protected void onCreate() {
        if (disponible == null) disponible = true;
        if (titulo != null) titulo = titulo.trim();
        if (autor != null) autor = autor.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(titulo, libro.titulo) &&
                Objects.equals(autor, libro.autor) &&
                Objects.equals(anoPublicacion, libro.anoPublicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, autor, anoPublicacion);
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacion=" + anoPublicacion +
                ", disponible=" + disponible +
                '}';
    }
}