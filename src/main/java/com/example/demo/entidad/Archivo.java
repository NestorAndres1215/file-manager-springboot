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

    @Column(nullable = false)
    private String nombreArchivo;

    @Column(nullable = false)
    private String tipoArchivo;

    @Lob
    @Column(name = "archivo", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] archivo;

    @Column(nullable = false)
    private long tamano;

    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaSubida;

    // ------------------------------
    // Método para mostrar tipo de archivo amigable
    // ------------------------------
    @Transient
    public String getTipoAmigable() {
        if (tipoArchivo == null) return "Desconocido";
        String tipo = tipoArchivo.toLowerCase();

        if (tipo.contains("pdf")) return "PDF";
        if (tipo.contains("png")) return "Imagen PNG";
        if (tipo.contains("jpeg") || tipo.contains("jpg")) return "Imagen JPEG";
        if (tipo.contains("word")) return "Word";
        if (tipo.contains("excel")) return "Excel";
        if (tipo.contains("powerpoint")) return "PowerPoint";

        return tipoArchivo; // Si no coincide con nada
    }
    public String getColorTipo() {
        if (tipoArchivo == null) return "#6c757d"; // gris por defecto
        return switch (tipoArchivo) {
            case "application/pdf" -> "#e74c3c"; // rojo para PDFs
            case "image/png", "image/jpeg" -> "#3498db"; // azul para imágenes
            case "application/msword",
                 "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "#2ecc71"; // verde para Word
            case "application/vnd.ms-excel",
                 "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "#f39c12"; // naranja para Excel
            case "application/vnd.ms-powerpoint",
                 "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> "#9b59b6"; // morado para PowerPoint
            default -> "#6c757d"; // gris para otros
        };
    }
}
