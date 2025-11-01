package com.example.demo.service;

import com.example.demo.entidad.Archivo;
import com.example.demo.exception.ArchivoNoValidoException;
import com.example.demo.repository.ArchivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ArchivoService {

    private final ArchivoRepository archivoRepository;

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

    // ------------------------------
    // Guardar archivo
    // ------------------------------
    public Archivo guardarArchivo(MultipartFile archivo, String descripcion) throws IOException {
        validarArchivo(archivo);

        Archivo nuevoArchivo = Archivo.builder()
                .nombreArchivo(archivo.getOriginalFilename())
                .tipoArchivo(archivo.getContentType())
                .archivo(archivo.getBytes())
                .tamano(archivo.getSize())
                .descripcion(descripcion)
                .fechaSubida(LocalDateTime.now())
                .build();

        return archivoRepository.save(nuevoArchivo);
    }

    // ------------------------------
    // Obtener archivo por ID
    // ------------------------------
    public Archivo obtenerArchivo(Long id) {
        return archivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con id: " + id));
    }

    // ------------------------------
    // Listar todos los archivos
    // ------------------------------
    public List<Archivo> obtenerTodos() {
        return archivoRepository.findAll();
    }

    // ------------------------------
    // Listar archivos por tipo (sin importar mayúsculas/minúsculas)
    // ------------------------------
    public List<Archivo> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return archivoRepository.findAll();
        }
        return archivoRepository.findByTipoArchivoIgnoreCase(tipo);
    }

    // ------------------------------
    // Listar por tipo y ordenar por fecha
    // ------------------------------
    public List<Archivo> listarPorTipoYFecha(String tipo, boolean ascendente) {
        if (tipo == null || tipo.isBlank()) {
            return ascendente
                    ? archivoRepository.findAll().stream()
                    .sorted(Comparator.comparing(Archivo::getFechaSubida))
                    .toList()
                    : archivoRepository.findAll().stream()
                    .sorted(Comparator.comparing(Archivo::getFechaSubida).reversed())
                    .toList();
        }

        return ascendente
                ? archivoRepository.findByTipoArchivoIgnoreCaseOrderByFechaSubidaAsc(tipo)
                : archivoRepository.findByTipoArchivoIgnoreCaseOrderByFechaSubidaDesc(tipo);
    }

    // ------------------------------
    // Obtener últimos 10 archivos
    // ------------------------------
    public List<Archivo> ultimos10Archivos() {
        return archivoRepository.findTop10ByOrderByFechaSubidaDesc();
    }

    // ------------------------------
    // Buscar archivos por tamaño mínimo
    // ------------------------------
    public List<Archivo> archivosPorTamano(long tamanoMinimo) {
        return archivoRepository.findByTamanoGreaterThan(tamanoMinimo);
    }

    // ------------------------------
    // Buscar archivos entre fechas
    // ------------------------------
    public List<Archivo> archivosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return archivoRepository.findByFechaSubidaBetween(inicio, fin);
    }

    // ------------------------------
    // Actualizar archivo existente
    // ------------------------------
    public Archivo actualizarArchivo(Long id, MultipartFile archivoNuevo, String descripcion) throws IOException {
        Archivo archivoExistente = obtenerArchivo(id);

        if (archivoNuevo != null && !archivoNuevo.isEmpty()) {
            validarArchivo(archivoNuevo);
            archivoExistente.setNombreArchivo(archivoNuevo.getOriginalFilename());
            archivoExistente.setTipoArchivo(archivoNuevo.getContentType());
            archivoExistente.setArchivo(archivoNuevo.getBytes());
            archivoExistente.setTamano(archivoNuevo.getSize());
            archivoExistente.setFechaSubida(LocalDateTime.now());
        }

        archivoExistente.setDescripcion(descripcion);

        return archivoRepository.save(archivoExistente);
    }

    // ------------------------------
    // Eliminar archivo
    // ------------------------------
    public void eliminarArchivo(Long id) {
        if (!archivoRepository.existsById(id)) {
            throw new RuntimeException("Archivo no encontrado con id: " + id);
        }
        archivoRepository.deleteById(id);
    }

    // ------------------------------
    // Validar archivo
    // ------------------------------
    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo no puede estar vacío");
        }

        if (archivo.getSize() > MAX_SIZE) {
            throw new RuntimeException(
                    String.format("El archivo '%s' excede el tamaño máximo permitido (%d MB)",
                            archivo.getOriginalFilename(), MAX_SIZE / (1024 * 1024))
            );
        }

        if (!ALLOWED_TYPES.contains(archivo.getContentType())) {
            throw new RuntimeException(
                    String.format("Tipo de archivo no permitido: %s", archivo.getContentType())
            );
        }
    }
}