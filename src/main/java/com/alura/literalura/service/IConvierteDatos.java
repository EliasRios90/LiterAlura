package com.alura.literalura.service;

public interface IConvierteDatos {
    // método generico
    <T> T obtenerDatos(String json, Class<T> clase);
}
