package com.example.demo.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public  class Mensajes {
    public static final String ARCHIVO_NO_ENCONTRADO = "Archivo no encontrado con id: ";
    public static final String TIPO_VACIO = "El tipo de archivo no puede estar vacío";
    public static final String ARCHIVOS_NO_ENCONTRADOS_POR_TIPO = "No se encontraron archivos del tipo: ";
    public static final String TAMANO_NEGATIVO = "El tamaño mínimo no puede ser negativo";
    public static final String ARCHIVOS_NO_ENCONTRADOS_POR_TAMANO = "No se encontraron archivos con tamaño mayor a ";
    public static final String FECHAS_NULAS = "Las fechas de inicio y fin no pueden ser nulas";
    public static final String FIN_ANTES_INICIO = "La fecha de fin no puede ser anterior a la fecha de inicio";
    public static final String ARCHIVOS_NO_ENCONTRADOS_ENTRE_FECHAS = "No se encontraron archivos entre %s y %s";
    public static final String ARCHIVO_NO_ENCONTRADO_POR_ID = "Archivo no encontrado con id: %d";
    public static final String ARCHIVO_VACIO = "El archivo no puede estar vacío";
    public static final String ARCHIVO_EXCEDE_TAMANO = "El archivo '%s' excede el tamaño máximo permitido (%d MB)";
    public static final String TIPO_ARCHIVO_NO_PERMITIDO = "Tipo de archivo no permitido: %s";
    public static final String TIPO_ARCHIVO_VACIO = "El tipo de archivo no puede estar vacío";
    public static final String NOMBRE_BUSQUEDA_VACIO = "El nombre de búsqueda no puede estar vacío";
    public static final String ARCHIVOS_NO_ENCONTRADOS_POR_NOMBRE = "No se encontraron archivos que contengan: %s";



}
