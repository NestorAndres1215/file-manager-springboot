package com.example.demo.repository;

import com.example.demo.entidad.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {


    List<Archivo> findByTipoArchivoIgnoreCase(String tipo);

    List<Archivo> findByTipoArchivoIgnoreCaseOrderByFechaSubidaAsc(String tipo);

    List<Archivo> findByTipoArchivoIgnoreCaseOrderByFechaSubidaDesc(String tipo);

    List<Archivo> findTop10ByOrderByFechaSubidaDesc();

    List<Archivo> findByTamanoGreaterThan(long tamano);

    List<Archivo> findByFechaSubidaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Archivo> findAllByOrderByTamanoAsc();

    List<Archivo> findAllByOrderByTamanoDesc();

    // Listar todos los archivos ordenados por nombre
    List<Archivo> findAllByOrderByNombreArchivoAsc();
    List<Archivo> findAllByOrderByNombreArchivoDesc();

    // Buscar archivos por nombre (contiene, ignorando mayúsculas)
    List<Archivo> findByNombreArchivoContainingIgnoreCase(String nombre);
}