package com.example.demo.entidad;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "archivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "tipo_archivo", nullable = false)
    private String tipoArchivo;

    @Lob
    @Column(name = "archivo", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] archivo;

    @Column(nullable = false)
    private long tamano;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    @Transient
    public String getTipoAmigable() {
        if (tipoArchivo == null || tipoArchivo.isBlank()) {
            return "Desconocido";
        }

        String tipo = tipoArchivo.toLowerCase();

        if (tipo.contains("pdf")) return "PDF";
        if (tipo.contains("png")) return "Imagen PNG";
        if (tipo.contains("jpeg") || tipo.contains("jpg")) return "Imagen JPEG";
        if (tipo.contains("word")) return "Word";
        if (tipo.contains("excel")) return "Excel";
        if (tipo.contains("powerpoint")) return "PowerPoint";

        return tipoArchivo;
    }

    @Transient
    public String getTipoAmigable() {
        if (tipoArchivo == null || tipoArchivo.isBlank()) return ArchivoConstants.TIPO_DESCONOCIDO;

        String tipo = tipoArchivo.toLowerCase();

        if (tipo.contains("pdf")) return ArchivoConstants.TIPO_PDF;
        if (tipo.contains("png")) return ArchivoConstants.TIPO_IMAGEN_PNG;
        if (tipo.contains("jpeg") || tipo.contains("jpg")) return ArchivoConstants.TIPO_IMAGEN_JPEG;
        if (tipo.contains("word")) return ArchivoConstants.TIPO_WORD;
        if (tipo.contains("excel")) return ArchivoConstants.TIPO_EXCEL;
        if (tipo.contains("powerpoint")) return ArchivoConstants.TIPO_POWERPOINT;

        return tipoArchivo;
    }

    @Transient
    public String getColorTipo() {
        return ArchivoConstants.TIPO_COLOR_MAP.getOrDefault(tipoArchivo, ArchivoConstants.COLOR_DEFAULT);
    }

    @Transient
    public String getTamanoLegible() {
        if (tamano < 1024) return tamano + " B";
        if (tamano < 1024 * 1024) return String.format("%.2f KB", tamano / 1024.0);
        return String.format("%.2f MB", tamano / (1024.0 * 1024.0));
    }
}