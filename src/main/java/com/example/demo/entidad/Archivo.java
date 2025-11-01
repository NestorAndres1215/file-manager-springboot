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
}
