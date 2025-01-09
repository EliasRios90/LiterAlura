package com.alura.literalura.model;

public enum Idioma {
    es("[es]", "Español"),
    en("[en]", "Ingles"),
    fr("[fr]", "Frances"),
    pt("[pt]", "Portugues");

    private String idiomaOriginal;
    private String idiomaEspanol;

    Idioma(String original, String espanol){
        this.idiomaOriginal = original;
        this.idiomaEspanol = espanol;
    }

    public static Idioma fromOriginal(String text){
        for(Idioma idioma : Idioma.values()){
            if(idioma.idiomaOriginal.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("No se ha encontrado el idioma: " + text);
    }

    public static Idioma fromEspanol(String text){
        for(Idioma idioma : Idioma.values()){
            if(idioma.idiomaEspanol.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("No se ha encontrado el idioma: " + text);
    }
}
