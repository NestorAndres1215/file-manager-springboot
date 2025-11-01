package com.example.demo.entidad;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
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
    public String getColorTipo() {
        if (tipoArchivo == null) return "#6c757d";

        return switch (tipoArchivo) {
            case "application/pdf" -> "#e74c3c";
            case "image/png", "image/jpeg" -> "#3498db";
            case "application/msword",
                 "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "#2ecc71";
            case "application/vnd.ms-excel",
                 "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "#f39c12";
            case "application/vnd.ms-powerpoint",
                 "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> "#9b59b6";
            default -> "#6c757d";
        };
    }


    @Transient
    public String getTamanoLegible() {
        if (tamano < 1024) return tamano + " B";
        if (tamano < 1024 * 1024) return String.format("%.2f KB", tamano / 1024.0);
        return String.format("%.2f MB", tamano / (1024.0 * 1024.0));
    }
}