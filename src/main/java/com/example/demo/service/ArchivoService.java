package com.example.demo.service;

import com.example.demo.entidad.Archivo;
import com.example.demo.exception.ArchivoNoEncontradoException;
import com.example.demo.exception.ArchivoNoValidoException;
import com.example.demo.repository.ArchivoRepository;
import com.example.demo.utils.ArchivoConstants;
import com.example.demo.utils.Mensajes;
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
                .orElseThrow(() -> new ArchivoNoEncontradoException(Mensajes.ARCHIVO_NO_ENCONTRADO + id));
    }


    public List<Archivo> obtenerTodos() {
        return archivoRepository.findAll();
    }

    public List<Archivo> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ArchivoNoValidoException(Mensajes.TIPO_VACIO);
        }

        List<Archivo> archivos = archivoRepository.findByTipoArchivoIgnoreCase(tipo);

        if (archivos.isEmpty()) {
            throw new ArchivoNoEncontradoException(Mensajes.ARCHIVOS_NO_ENCONTRADOS_POR_TIPO + tipo);
        }

        return archivos;
    }


    public List<Archivo> ultimos10Archivos() {
        return archivoRepository.findTop10ByOrderByFechaSubidaDesc();
    }

    public List<Archivo> archivosPorTamano(long tamanoMinimo) {
        if (tamanoMinimo < 0) {
            throw new ArchivoNoValidoException(Mensajes.TAMANO_NEGATIVO);
        }

        List<Archivo> archivos = archivoRepository.findByTamanoGreaterThan(tamanoMinimo);

        if (archivos.isEmpty()) {
            throw new ArchivoNoEncontradoException(Mensajes.ARCHIVOS_NO_ENCONTRADOS_POR_TAMANO + tamanoMinimo);
        }

        return archivos;
    }

    public List<Archivo> archivosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            throw new ArchivoNoValidoException(Mensajes.FECHAS_NULAS);
        }
        if (fin.isBefore(inicio)) {
            throw new ArchivoNoValidoException(Mensajes.FIN_ANTES_INICIO);
        }

        List<Archivo> archivos = archivoRepository.findByFechaSubidaBetween(inicio, fin);

        if (archivos.isEmpty()) {
            throw new ArchivoNoEncontradoException(String.format(Mensajes.ARCHIVOS_NO_ENCONTRADOS_ENTRE_FECHAS, inicio, fin));
        }

        return archivos;
    }


    public Archivo actualizarArchivo(Long id, MultipartFile archivoNuevo, String descripcion) throws IOException {
        Archivo archivoExistente = archivoRepository.findById(id)
                .orElseThrow(() -> new ArchivoNoEncontradoException(
                        String.format(ArchivoConstants.ARCHIVO_NO_ENCONTRADO, id)
                ));

        if (archivoNuevo != null && !archivoNuevo.isEmpty()) {
            validarArchivo(archivoNuevo); // Aquí ya lanza excepción si no cumple
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
            throw new ArchivoNoEncontradoException(String.format(Mensajes.ARCHIVO_NO_ENCONTRADO_POR_ID, id));
        }
        archivoRepository.deleteById(id);
    }


    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ArchivoNoValidoException(Mensajes.ARCHIVO_VACIO);
        }

        if (archivo.getSize() > ArchivoConstants.MAX_SIZE) {
            throw new ArchivoNoValidoException(
                    String.format(Mensajes.ARCHIVO_EXCEDE_TAMANO,
                            archivo.getOriginalFilename(),
                            ArchivoConstants.MAX_SIZE / (1024 * 1024))
            );
        }

        if (!ArchivoConstants.ALLOWED_TYPES.contains(archivo.getContentType())) {
            throw new ArchivoNoValidoException(
                    String.format(Mensajes.TIPO_ARCHIVO_NO_PERMITIDO, archivo.getContentType())
            );
        }
    }

    public List<Archivo> listarPorTipoFechaAsc(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ArchivoNoValidoException(Mensajes.TIPO_ARCHIVO_VACIO);
        }

        List<Archivo> archivos = archivoRepository.findByTipoArchivoIgnoreCaseOrderByFechaSubidaAsc(tipo);
        if (archivos.isEmpty()) {
            throw new ArchivoNoEncontradoException(String.format(Mensajes.ARCHIVOS_NO_ENCONTRADOS_POR_TIPO, tipo));
        }

        return archivos;
    }

    public List<Archivo> listarPorTipoFechaDesc(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ArchivoNoValidoException(Mensajes.TIPO_ARCHIVO_VACIO);
        }

        List<Archivo> archivos = archivoRepository.findByTipoArchivoIgnoreCaseOrderByFechaSubidaDesc(tipo);
        if (archivos.isEmpty()) {
            throw new ArchivoNoEncontradoException(String.format(Mensajes.ARCHIVOS_NO_ENCONTRADOS_POR_TIPO, tipo));
        }

        return archivos;
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
            throw new ArchivoNoValidoException(Mensajes.NOMBRE_BUSQUEDA_VACIO);
        }

        List<Archivo> archivos = archivoRepository.findByNombreArchivoContainingIgnoreCase(nombre);
        if (archivos.isEmpty()) {
            throw new ArchivoNoEncontradoException(
                    String.format(Mensajes.ARCHIVOS_NO_ENCONTRADOS_POR_NOMBRE, nombre)
            );
        }

        return archivos;
    }


}