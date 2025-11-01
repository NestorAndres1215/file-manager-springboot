package com.example.demo.controller;

import com.example.demo.entidad.Archivo;
import com.example.demo.service.ArchivoService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor

public class ArchivoController {

    private final ArchivoService archivoService;

    @GetMapping("/")
    public String verPaginaPrincipal() {
        return "index";
    }


    @GetMapping("/listado")
    public String verListado(Model model) {
        List<Archivo> archivos = archivoService.obtenerTodos();
        model.addAttribute("archivos", archivos);
        return "listado";
    }


    @GetMapping("/registrar")
    public String mostrarFormularioRegistro() {
        return "registrar";
    }


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

    @GetMapping("/descargar/{id}")
    public ResponseEntity<ByteArrayResource> descargarArchivo(@PathVariable Long id) {
        Archivo archivo = archivoService.obtenerArchivo(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getNombreArchivo() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, archivo.getTipoArchivo())
                .body(new ByteArrayResource(archivo.getArchivo()));
    }
}
