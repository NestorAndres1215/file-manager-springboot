package com.example.demo.service;

import com.example.demo.entidad.Archivo;
import com.example.demo.repository.ArchivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArchivoService {


    private final ArchivoRepository archivoRepository;

    public ArchivoService(ArchivoRepository archivoRepository) {
        this.archivoRepository = archivoRepository;
    }

    private static final long MAX_SIZE = 10 * 1024 * 1024;


    private static final List<String> ALLOWED_TYPES = List.of(
            "image/png",
            "image/jpeg",
            "application/pdf",
            "application/msword",                     // .doc
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
            "application/vnd.ms-excel",               // .xls
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
            "application/vnd.ms-powerpoint",          // .ppt
            "application/vnd.openxmlformats-officedocument.presentationml.presentation" // .pptx
    );

    public void guardarArchivo(MultipartFile archivo, String descripcion) throws IOException {
        validarArchivo(archivo);

        Archivo archivoEntity = new Archivo();
        archivoEntity.setNombreArchivo(archivo.getOriginalFilename());
        archivoEntity.setTipoArchivo(archivo.getContentType());
        archivoEntity.setArchivo(archivo.getBytes());
        archivoEntity.setTamano(archivo.getSize());
        archivoEntity.setDescripcion(descripcion);
        archivoEntity.setFechaSubida(LocalDateTime.now());

        archivoRepository.save(archivoEntity);
    }


    public Archivo obtenerArchivo(Long id) {
        return archivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con id: " + id));
    }

    public List<Archivo> obtenerTodos() {
        return archivoRepository.findAll();
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }
        if (archivo.getSize() > MAX_SIZE) {
            throw new RuntimeException("El archivo excede el tamaño máximo permitido (10 MB)");
        }
        if (!ALLOWED_TYPES.contains(archivo.getContentType())) {
            throw new RuntimeException("Tipo de archivo no permitido: " + archivo.getContentType());
        }
    }



}