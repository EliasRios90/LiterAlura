package com.alura.literalura.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToMany
    private List<Autor> autores;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer numeroDeDescargas;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idioma = Idioma.fromOriginal(datosLibro.idiomas().toString().split(",")[0].trim());
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public List<Autor> getAutores() {
        return autores;
    }
    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }
    public Idioma getIdioma() {
        return idioma;
    }
    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }
    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }
    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        String nombreAutores = "";
        if(autores.size() > 1){
            for(int i=0; i<autores.size(); i++){
                nombreAutores += autores.get(i).getNombre() + " | ";
            }
        }else nombreAutores = autores.get(0).getNombre();
        
        return String.format("-------------------- Libro --------------------\n"+
            "Título: %s\n" +
            "Autor: %s\n" +
            "Idioma: %s\n" +
            "Número de descargas: %s\n",
            titulo, nombreAutores, idioma, numeroDeDescargas);
    }    
}
