package com.example.demo.service;

import com.example.demo.entidad.Archivo;
import com.example.demo.exception.ArchivoNoEncontradoException;
import com.example.demo.exception.ArchivoNoValidoException;
import com.example.demo.repository.ArchivoRepository;
import com.example.demo.utils.ArchivoConstants;
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


    public Archivo obtenerArchivo(Long id) {
        return archivoRepository.findById(id)
                .orElseThrow(() -> new ArchivoNoEncontradoException("Archivo no encontrado con id: " + id));
    }

    public List<Archivo> obtenerTodos() {
        return archivoRepository.findAll();
    }

    public List<Archivo> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return archivoRepository.findAll();
        }
        return archivoRepository.findByTipoArchivoIgnoreCase(tipo);
    }


    public List<Archivo> ultimos10Archivos() {
        return archivoRepository.findTop10ByOrderByFechaSubidaDesc();
    }

    public List<Archivo> archivosPorTamano(long tamanoMinimo) {
        return archivoRepository.findByTamanoGreaterThan(tamanoMinimo);
    }

    public List<Archivo> archivosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return archivoRepository.findByFechaSubidaBetween(inicio, fin);
    }

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

    public void eliminarArchivo(Long id) {
        if (!archivoRepository.existsById(id)) {
            throw new ArchivoNoEncontradoException("Archivo no encontrado con id: " + id);
        }
        archivoRepository.deleteById(id);
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ArchivoNoValidoException("El archivo no puede estar vacío");
        }

        if (archivo.getSize() > ArchivoConstants.MAX_SIZE) {
            throw new ArchivoNoValidoException(
                    String.format("El archivo '%s' excede el tamaño máximo permitido (%d MB)",
                            archivo.getOriginalFilename(), ArchivoConstants.MAX_SIZE / (1024 * 1024))
            );
        }

        if (!ArchivoConstants.ALLOWED_TYPES.contains(archivo.getContentType())) {
            throw new ArchivoNoValidoException(
                    String.format("Tipo de archivo no permitido: %s", archivo.getContentType())
            );
        }
    }

    public List<Archivo> listarPorTipoFechaAsc(String tipo) {
        return archivoRepository.findByTipoArchivoIgnoreCaseOrderByFechaSubidaAsc(tipo);
    }

    public List<Archivo> listarPorTipoFechaDesc(String tipo) {
        return archivoRepository.findByTipoArchivoIgnoreCaseOrderByFechaSubidaDesc(tipo);
    }

    public List<Archivo> listarPorTamanoAsc() {
        return archivoRepository.findAllByOrderByTamanoAsc();
    }

    public List<Archivo> listarPorTamanoDesc() {
        return archivoRepository.findAllByOrderByTamanoDesc();
    }

    public List<Archivo> listarPorNombreAsc() {
        return archivoRepository.findAllByOrderByNombreArchivoAsc();
    }

    public List<Archivo> listarPorNombreDesc() {
        return archivoRepository.findAllByOrderByNombreArchivoDesc();
    }

    public List<Archivo> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return archivoRepository.findAll();
        }
        return archivoRepository.findByNombreArchivoContainingIgnoreCase(nombre);
    }
}