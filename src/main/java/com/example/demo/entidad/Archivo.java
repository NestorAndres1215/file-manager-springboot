package com.example.demo.entidad;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "archivos")
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

    public Archivo() {
    }

    public Archivo(Long id, LocalDateTime fechaSubida, long tamano, String descripcion, byte[] archivo, String tipoArchivo, String nombreArchivo) {
        this.id = id;
        this.fechaSubida = fechaSubida;
        this.tamano = tamano;
        this.descripcion = descripcion;
        this.archivo = archivo;
        this.tipoArchivo = tipoArchivo;
        this.nombreArchivo = nombreArchivo;
    }
// ------------------------------
    // Getters y Setters
    // ------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public long getTamano() {
        return tamano;
    }

    public void setTamano(long tamano) {
        this.tamano = tamano;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    // ------------------------------
    // Método para mostrar tipo de archivo amigable
    // ------------------------------
    @Transient
    public String getTipoAmigable() {
        switch (this.tipoArchivo) {
            case "application/pdf":
                return "PDF";
            case "image/png":
                return "Imagen PNG";
            case "image/jpeg":
                return "Imagen JPEG";
            case "application/msword":
                return "Word (.doc)";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return "Word (.docx)";
            case "application/vnd.ms-excel":
                return "Excel (.xls)";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return "Excel (.xlsx)";
            case "application/vnd.ms-powerpoint":
                return "PowerPoint (.ppt)";
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return "PowerPoint (.pptx)";
            default:
                return this.tipoArchivo; // Mostrar MIME si no está mapeado
        }
    }
}
