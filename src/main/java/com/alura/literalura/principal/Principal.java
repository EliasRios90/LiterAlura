package com.alura.literalura.principal;

import java.util.Scanner;

import com.alura.literalura.model.DatosBusqueda;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    private void verMenu(){
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
            }
        }
    }

    public DatosLibro getDatosLibro(){
        System.out.println("Ingrese el nombre del libro de desea buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos("https://gutendex.com/books/?search="+nombreLibro.replace(" ", "+"));
        DatosBusqueda datos = conversor.obtenerDatos(json, DatosBusqueda.class);
        DatosLibro libro = datos.resultados().get(0);
        //System.out.println(json);
        return libro;
    }
    
    private void buscarLibroPorTitulo(){
        DatosLibro datos = getDatosLibro();
    }
}
