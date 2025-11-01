package com.example.demo.utils;

import java.util.List;
import java.util.Map;

public class ArchivoConstants {

    // --------------------------
    // Tipos amigables
    // --------------------------
    public static final String TIPO_DESCONOCIDO = "Desconocido";
    public static final String TIPO_PDF = "PDF";
    public static final String TIPO_IMAGEN_PNG = "Imagen PNG";
    public static final String TIPO_IMAGEN_JPEG = "Imagen JPEG";
    public static final String TIPO_WORD = "Word";
    public static final String TIPO_EXCEL = "Excel";
    public static final String TIPO_POWERPOINT = "PowerPoint";

    // --------------------------
    // Colores por tipo MIME
    // --------------------------
    public static final String COLOR_DEFAULT = "#6c757d";
    public static final String COLOR_PDF = "#e74c3c";
    public static final String COLOR_IMAGEN = "#3498db";
    public static final String COLOR_WORD = "#2ecc71";
    public static final String COLOR_EXCEL = "#f39c12";
    public static final String COLOR_POWERPOINT = "#9b59b6";

    public static final Map<String, String> TIPO_COLOR_MAP = Map.ofEntries(
            Map.entry("application/pdf", COLOR_PDF),
            Map.entry("image/png", COLOR_IMAGEN),
            Map.entry("image/jpeg", COLOR_IMAGEN),
            Map.entry("application/msword", COLOR_WORD),
            Map.entry("application/vnd.openxmlformats-officedocument.wordprocessingml.document", COLOR_WORD),
            Map.entry("application/vnd.ms-excel", COLOR_EXCEL),
            Map.entry("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", COLOR_EXCEL),
            Map.entry("application/vnd.ms-powerpoint", COLOR_POWERPOINT),
            Map.entry("application/vnd.openxmlformats-officedocument.presentationml.presentation", COLOR_POWERPOINT)
    );

    public static final long MAX_SIZE = 10 * 1024 * 1024; // 10 MB

    public static final List<String> ALLOWED_TYPES = List.of(
            "image/png",
            "image/jpeg",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    );
    public static final String ARCHIVO_NO_ENCONTRADO = "Archivo no encontrado con ID: %d";
}