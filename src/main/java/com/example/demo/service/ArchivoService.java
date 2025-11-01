package com.example.demo.service;

import com.example.demo.entidad.Archivo;
import com.example.demo.exception.ArchivoNoValidoException;
import com.example.demo.repository.ArchivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArchivoService {

    private final ArchivoRepository archivoRepository;

    public ArchivoService(ArchivoRepository archivoRepository) {
        this.archivoRepository = archivoRepository;
    }

    private static final long MAX_SIZE = 10 * 1024 * 1024; // 10 MB

    private static final List<String> ALLOWED_TYPES = List.of(
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

    // Guardar archivo
    public void guardarArchivo(MultipartFile archivo, String descripcion) throws IOException {
        validarArchivo(archivo);

        Archivo archivoEntity = Archivo.builder()
                .nombreArchivo(archivo.getOriginalFilename())
                .tipoArchivo(archivo.getContentType())
                .archivo(archivo.getBytes())
                .tamano(archivo.getSize())
                .descripcion(descripcion)
                .fechaSubida(LocalDateTime.now())
                .build();

        archivoRepository.save(archivoEntity);
    }

    // Obtener archivo por id
    public Archivo obtenerArchivo(Long id) {
        return archivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con id: " + id));
    }

    // Obtener todos los archivos
    public List<Archivo> obtenerTodos() {
        return archivoRepository.findAll();
    }

    // Obtener archivos filtrados por tipo amigable
    public List<Archivo> listarPorTipoAmigable(String tipoBuscado) {
        if (tipoBuscado == null || tipoBuscado.isEmpty()) {
            return archivoRepository.findAll();
        }
        String tipoLower = tipoBuscado.toLowerCase();
        return archivoRepository.findAll().stream()
                .filter(a -> a.getTipoAmigable().toLowerCase().contains(tipoLower))
                .collect(Collectors.toList());
    }

    // Validar archivo
    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ArchivoNoValidoException("El archivo no puede estar vacío");
        }

        if (archivo.getSize() > MAX_SIZE) {
            throw new ArchivoNoValidoException(
                    String.format("El archivo '%s' excede el tamaño máximo permitido (%d MB)",
                            archivo.getOriginalFilename(), MAX_SIZE / (1024 * 1024))
            );
        }

        if (!ALLOWED_TYPES.contains(archivo.getContentType())) {
            throw new ArchivoNoValidoException(
                    String.format("Tipo de archivo no permitido: %s", archivo.getContentType())
            );
        }
    }

    public List<Archivo> listarPorTipoYFechaRepository(String tipo, boolean ascendente) {
        if (tipo == null || tipo.isEmpty()) {
            return ascendente
                    ? archivoRepository.findAll().stream()
                    .sorted(Comparator.comparing(Archivo::getFechaSubida))
                    .collect(Collectors.toList())
                    : archivoRepository.findAll().stream()
                    .sorted(Comparator.comparing(Archivo::getFechaSubida).reversed())
                    .collect(Collectors.toList());
        }

        return ascendente
                ? archivoRepository.buscarPorTipoLikeOrderByFechaAsc(tipo)
                : archivoRepository.buscarPorTipoLikeOrderByFechaDesc(tipo);
    }


}
