package com.example.demo.controller;

import com.example.demo.entidad.Archivo;
import com.example.demo.service.ArchivoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/archivos")
public class ArchivoController {

    private final ArchivoService archivoService;

    public ArchivoController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    // Página principal
    @GetMapping("/")
    public String verPaginaPrincipal() {
        return "index";
    }

    // Listado de archivos
    @GetMapping("/listado")
    public String verListado(Model model) {
        List<Archivo> archivos = archivoService.obtenerTodos();
        model.addAttribute("archivos", archivos);
        return "listado";
    }

    // Formulario para subir archivo
    @GetMapping("/registrar")
    public String mostrarFormularioRegistro() {
        return "registrar";
    }

    // Subir archivo
    @PostMapping("/subir")
    public String subirArchivo(@RequestParam("archivo") MultipartFile archivo,
                               @RequestParam("descripcion") String descripcion,
                               Model model) {
        try {
            archivoService.guardarArchivo(archivo, descripcion);
        } catch (IOException e) {
            model.addAttribute("error", "Error al subir el archivo: " + e.getMessage());
            return "registrar";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registrar";
        }
        return "redirect:/archivos/listado";
    }

    // Descargar archivo
    @GetMapping("/descargar/{id}")
    public ResponseEntity<ByteArrayResource> descargarArchivo(@PathVariable Long id) {
        Archivo archivo = archivoService.obtenerArchivo(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getNombreArchivo() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, archivo.getTipoArchivo())
                .body(new ByteArrayResource(archivo.getArchivo()));
    }
}
