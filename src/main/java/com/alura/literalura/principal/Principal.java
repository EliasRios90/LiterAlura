package com.alura.literalura.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.alura.literalura.model.DatosBusqueda;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void verMenu(){
        var opcion = -1;
        while(opcion != 0){
            var menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1: buscarLibroPorTitulo(); break;
                case 2: mostrarLibrosRegistrados(); break;
                case 0: System.out.println("Cerrando la aplicación..."); break;
                default: System.out.println("Opción inválida"); break;
            }
        }
    }

    private void mostrarLibro(DatosLibro libro){
        System.out.println("############### LIBRO ################");
        System.out.println("Título: " + libro.titulo());
        System.out.print("Autor/es: ");
        libro.autores().forEach(a -> {
            if(libro.autores().size() > 1){
                System.out.print(a.nombre() + " | ");
            }else{
                System.out.print(a.nombre());
            }
        });
        System.out.print("\nIdioma: ");
        libro.idiomas().forEach(i -> {
            if(libro.idiomas().size() > 1){
                System.out.print(i + ", ");
            }else{
                System.out.print(i);
            }
        });
        System.out.println("\nCantidad de descargas: " + libro.numeroDeDescargas());
        System.out.println("######################################");
    }
    
    private void buscarLibroPorTitulo(){
        System.out.println("Ingrese el nombre del libro de desea buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos("https://gutendex.com/books/?search="+nombreLibro.replace(" ", "+"));
        
        DatosBusqueda datos = conversor.obtenerDatos(json, DatosBusqueda.class);
        DatosLibro datosLibro = null;

        if(!datos.resultados().isEmpty()){
            datosLibro = datos.resultados().get(0);
            Libro libro = new Libro(datosLibro);
            libroRepository.save(libro);
        }else{
            System.out.println("No se encontró el libro.");
        }
    }

    private void mostrarLibrosRegistrados(){
        //
    }
}
