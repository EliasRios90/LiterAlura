package com.alura.literalura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long>{
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
    
    List<Libro> findByIdioma(Idioma idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDeDescargas DESC LIMIT 10")
    List<Libro> top10Descargas();
}
