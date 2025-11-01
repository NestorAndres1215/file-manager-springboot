package com.example.demo.controller;


import com.example.demo.entidad.Archivo;
import com.example.demo.service.ArchivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
@Tag(name = "Archivos", description = "Operaciones CRUD para archivos")
public class ArchivoRestController {

    private final ArchivoService archivoService;

    @Operation(summary = "Obtener todos los archivos")
    @GetMapping
    public List<Archivo> obtenerTodos() {
        return archivoService.obtenerTodos();
    }

    @Operation(summary = "Obtener un archivo por ID")
    @GetMapping("/{id}")
    public Archivo obtenerPorId(@PathVariable Long id) {
        return archivoService.obtenerArchivo(id);
    }

    @Operation(summary = "Tipo Listar")
    @GetMapping("/tipo")
    public ResponseEntity<List<Archivo>> listarPorTipo(@RequestParam(required = false) String tipo) {
        List<Archivo> archivos = archivoService.listarPorTipo(tipo);
        return ResponseEntity.ok(archivos);
    }

    @Operation(summary = "Subir un nuevo archivo")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Archivo subirArchivo(@RequestParam("archivo") MultipartFile archivo,
                                @RequestParam(value = "descripcion", required = false) String descripcion) throws IOException {
        return archivoService.guardarArchivo(archivo, descripcion);
    }


    @Operation(summary = "Descargar un archivo por ID")
    @GetMapping("/descargar/{id}")
    public ResponseEntity<ByteArrayResource> descargarArchivo(@PathVariable Long id) {
        Archivo archivo = archivoService.obtenerArchivo(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getNombreArchivo() + "\"")
                .contentType(MediaType.parseMediaType(archivo.getTipoArchivo()))
                .body(new ByteArrayResource(archivo.getArchivo()));
    }

    @Operation(summary = "Actualizar un archivo existente")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Archivo actualizarArchivo(@PathVariable Long id,
                                     @RequestParam(value = "archivo", required = false) MultipartFile archivoNuevo,
                                     @RequestParam(value = "descripcion", required = false) String descripcion) throws IOException {
        return archivoService.actualizarArchivo(id, archivoNuevo, descripcion);
    }

    @Operation(summary = "Eliminar un archivo por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArchivo(@PathVariable Long id) {
        archivoService.eliminarArchivo(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar archivos por tipo (fecha ascendente)")
    @GetMapping("/tipo/{tipo}/asc")
    public ResponseEntity<List<Archivo>> listarPorTipoAsc(@PathVariable String tipo) {
        List<Archivo> archivos = archivoService.listarPorTipoFechaAsc(tipo);
        return ResponseEntity.ok(archivos);
    }

    @Operation(summary = "Listar archivos por tipo (fecha descendente)")
    @GetMapping("/tipo/{tipo}/desc")
    public ResponseEntity<List<Archivo>> listarPorTipoDesc(@PathVariable String tipo) {
        List<Archivo> archivos = archivoService.listarPorTipoFechaDesc(tipo);
        return ResponseEntity.ok(archivos);
    }

    @Operation(summary = "Obtener los últimos 10 archivos subidos")
    @GetMapping("/ultimos")
    public List<Archivo> ultimos10() {
        return archivoService.ultimos10Archivos();
    }

    @Operation(summary = "Listar archivos con tamaño mayor al indicado")
    @GetMapping("/tamano")
    public List<Archivo> archivosPorTamano(@RequestParam("min") long tamanoMinimo) {
        return archivoService.archivosPorTamano(tamanoMinimo);
    }

    @Operation(summary = "Listar archivos subidos entre fechas")
    @GetMapping("/fechas")
    public List<Archivo> archivosEntreFechas(@RequestParam("inicio") String inicioStr,
                                             @RequestParam("fin") String finStr) {
        LocalDateTime inicio = LocalDateTime.parse(inicioStr);
        LocalDateTime fin = LocalDateTime.parse(finStr);
        return archivoService.archivosEntreFechas(inicio, fin);
    }

    @Operation(summary = "Listar todos los archivos por tamaño ascendente")
    @GetMapping("/tamano/asc")
    public ResponseEntity<List<Archivo>> listarPorTamanoAsc() {
        List<Archivo> archivos = archivoService.listarPorTamanoAsc();
        return ResponseEntity.ok(archivos);
    }

    @Operation(summary = "Listar todos los archivos por tamaño descendente")
    @GetMapping("/tamano/desc")
    public ResponseEntity<List<Archivo>> listarPorTamanoDesc() {
        List<Archivo> archivos = archivoService.listarPorTamanoDesc();
        return ResponseEntity.ok(archivos);
    }
}

