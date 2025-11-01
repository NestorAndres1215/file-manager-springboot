package com.example.demo.entidad;

import com.example.demo.utils.ArchivoConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Column(name = "codigo", nullable = false)
    private Long id;

    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @NotBlank(message = "El tipo de archivo es obligatorio")
    @Column(name = "tipo_archivo", nullable = false)
    private String tipoArchivo;

    @NotNull(message = "El archivo no puede estar vacío")
    @Lob
    @Column(name = "archivo", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] archivo;

    @Min(value = 1, message = "El tamaño del archivo debe ser mayor a 0")
    @Column(nullable = false)
    private long tamano;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Column(length = 500, nullable = false)
    private String descripcion;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;


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