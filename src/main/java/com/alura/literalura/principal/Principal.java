package com.alura.literalura.principal;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosBusqueda;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Idioma;
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
    private List<Libro> libros = null;
    private List<Autor> autores = null;

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
                    6 - Generar estadísticas
                    7 - Top 10 libros más descargados
                    8 - Buscar autor por nombre
                    9 - Otras consultas con autores
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1: 
                    buscarLibroPorTitulo(); 
                    break;
                case 2: 
                    mostrarLibrosRegistrados(); 
                    break;
                case 3: 
                    mostrarAutoresRegistrados(); 
                    break;
                case 4: 
                    mostrarAutoresVivosPorAnio(); 
                    break;
                case 5: 
                    mostrarLibrosPorIdioma(); 
                    break;
                case 6: 
                    generarEstadisticas(); 
                    break;
                case 7: 
                    top10LibrosMasDescargados(); 
                    break;
                case 8: 
                    buscarAutorPorNombre(); 
                    break;
                case 9: 
                    otrasConsultasConAutores(); 
                    break;
                case 0: 
                    System.out.println("Cerrando la aplicación..."); 
                    break;
                default: 
                    System.out.println("Opción inválida"); 
                    break;
            }
        }
    }
    
    private void buscarLibroPorTitulo(){
        System.out.println("Ingrese el nombre del libro de desea buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos("https://gutendex.com/books/?search="+nombreLibro.replace(" ", "+"));
        
        DatosBusqueda datos = conversor.obtenerDatos(json, DatosBusqueda.class);
        DatosLibro datosLibro = null;

        if(!datos.resultados().isEmpty()){
            datosLibro = datos.resultados().get(0);
            String nombreAutor = datosLibro.autores().get(0).nombre();
            Libro libro = new Libro(datosLibro);

            Optional<Libro> libroRegistrado = libroRepository.findByTituloContainsIgnoreCase(libro.getTitulo());
            
            if(libroRegistrado.isPresent()){
                System.out.println("El libro ya exite.");
            }else{
                Optional<Autor> existeAutor = autorRepository.findByNombreContainsIgnoreCase(nombreAutor);

                if(existeAutor.isPresent()){
                    System.out.println(existeAutor.get());
                    libro.setAutor(existeAutor.get());
                } else{
                    DatosAutor datosAutor = datosLibro.autores().get(0);
                    Autor autor = new Autor(datosAutor);
                    autorRepository.save(autor);
                }

                libroRepository.save(libro);

                System.out.println(libro);
            }
        }else{
            System.out.println("No se encontró el libro.");
        }
    }

    private void mostrarLibrosRegistrados(){
        libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void mostrarAutoresRegistrados(){
        autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void mostrarAutoresVivosPorAnio(){
        System.out.println("Ingrese el año para la búsqueda");
        Integer anio = teclado.nextInt();
        teclado.nextLine();
        autores = autorRepository.listaAutoresVivos(anio);
        autores.forEach(System.out::println);
    }

    private void mostrarLibrosPorIdioma(){
        var opcion = -1;
        while (opcion != 0) {
            System.out.println("Ingrese el idioma para buscar los libros");
            var opciones = """
                1) en - Inglés
                2) es - Español
                3) pt - Portugués
                4) fr - Francés

                0) Volver atrás.
                """;
            System.out.println(opciones);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1: 
                    libros = libroRepository.findByIdioma(Idioma.en); 
                    break;
                case 2: 
                    libros = libroRepository.findByIdioma(Idioma.es); 
                    break;
                case 3: 
                    libros = libroRepository.findByIdioma(Idioma.pt); 
                    break;
                case 4: 
                    libros = libroRepository.findByIdioma(Idioma.fr); 
                    break;
                case 0: 
                    opcion = 0;
                    break;
                default: 
                    System.out.println("Opción incorrecta"); 
                    break;
            }

            if(libros.size() != 0){
                libros.forEach(System.out::println);
            }else{
                System.out.println("No hay libros para mostrar");
            }
        }
    }

    private void generarEstadisticas(){
        libros = libroRepository.findAll();
        IntSummaryStatistics estadisticas = libros.stream()
            .filter(l -> l.getNumeroDeDescargas() > 0)
            .collect(Collectors.summarizingInt(Libro::getNumeroDeDescargas));
        System.out.println("Total descargas: " + estadisticas.getCount());
        System.out.println("Media de descargas: " + estadisticas.getAverage());
        System.out.println("Libro más descargado: " + estadisticas.getMax());
        System.out.println("Libro menos descargado: " + estadisticas.getMin());
    }

    private void top10LibrosMasDescargados(){
        libros = libroRepository.top10Descargas();
        libros.forEach(l -> System.out.println("\nTítulo: " + l.getTitulo() + "\nAutor: " + l.getAutor().getNombre() + "\nDescargas: " + l.getNumeroDeDescargas()));
    }

    private void buscarAutorPorNombre(){
        System.out.println("Ingrese el nombre del autor");
        var nombreAutor = teclado.nextLine();
        Optional<Autor> autor = autorRepository.findByNombreContainsIgnoreCase(nombreAutor);

        if(autor.isPresent()){
            System.out.println(autor.get());
        }else{
            System.out.println("No está presente el autor.");
        }
    }

    private void otrasConsultasConAutores(){
        var opcion = -1;
        var anio = 0;

        while(opcion != 0){
            var opciones = """
                    1 - Buscar autores por fecha de nacimiento
                    2 - Buscar autores por fecha de fallecimiento

                    0 - Volver atrás
                    """;
            System.out.println(opciones);
            opcion = teclado.nextInt();
            teclado.nextLine();

            if(opcion != 0){
                System.out.println("Ingrese el año para la búsqueda");
                anio = teclado.nextInt();
                teclado.nextLine();
            }
            
            switch(opcion){
                case 1:
                    autores = autorRepository.listaAutoresPorNacimiento(anio);
                    break;
                case 2:
                    autores = autorRepository.listaAutoresPorFallecimiento(anio);
                    break;
                case 0: 
                    opcion = 0;
                    break;
                default: System.out.println("Opción incorrecta");
            }

            if(autores.size() > 0){
                autores.forEach(System.out::println);
            }else{
                System.out.println("No hay autores para mostrar");
            }
        }
    }
}
