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
import java.util.List;

@RestController
@RequestMapping("/rest/archivos")
@RequiredArgsConstructor
@Tag(name = "Archivos", description = "Operaciones relacionadas con archivos")
public class ArchivoRestController {

    private final ArchivoService archivoService;


    @Operation(summary = "Listado de todos los archivos")
    @GetMapping("/listar")
    public List<Archivo> listarTodos() {
        return archivoService.obtenerTodos();
    }


    @Operation(summary = "Listar archivos filtrando por tipo")
    @GetMapping("/listar/tipo")
    public List<Archivo> listarPorTipo(@RequestParam("tipo") String tipo) {
        return archivoService.listarPorTipoArchivo(tipo);
    }

    @Operation(summary = "Obtener archivo por ID")
    @GetMapping("/listar/{id}")
    public Archivo obtenerArchivoPorId(@PathVariable Long id) {
        return archivoService.obtenerArchivo(id);
    }

    @Operation(summary = "Listar archivos filtrando por tipo amigable")
    @GetMapping("/tipo")
    public List<Archivo> listarPorTipoAmigable(@RequestParam("tipo") String tipoBuscado) {
        return archivoService.listarPorTipoAmigable(tipoBuscado);
    }

    @Operation(summary = "Listar archivos filtrando por tipo de archivo")
    @GetMapping("/filtrar")
    public List<Archivo> listarPorTipoArchivo(@RequestParam("tipo") String tipoBuscado) {
        return archivoService.listarPorTipoArchivo(tipoBuscado);
    }

    @Operation(summary = "Subir un nuevo archivo")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Archivo subirArchivo(@RequestParam("archivo") MultipartFile archivo,
                                @RequestParam(value = "descripcion", required = false) String descripcion) throws IOException {
        return archivoService.guardarArchivo(archivo, descripcion);
    }

    @Operation(summary = "Actualizar archivo existente")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Archivo actualizarArchivo(@PathVariable Long id,
                                     @RequestParam(value = "archivo", required = false) MultipartFile archivoNuevo,
                                     @RequestParam(value = "descripcion", required = false) String descripcion) throws IOException {
        return archivoService.actualizarArchivo(id, archivoNuevo, descripcion);
    }

    @Operation(summary = "Eliminar archivo por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArchivo(@PathVariable Long id) {
        archivoService.eliminarArchivo(id);
        return ResponseEntity.noContent().build();
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

}
