package com.example.demo.repository;

import com.example.demo.entidad.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {


    @Query("SELECT a FROM Archivo a WHERE LOWER(a.tipoArchivo) LIKE LOWER(CONCAT('%', :tipo, '%')) ORDER BY a.fechaSubida DESC")
    List<Archivo> buscarPorTipoLikeOrderByFechaDesc(String tipo);

    @Query("SELECT a FROM Archivo a WHERE LOWER(a.tipoArchivo) LIKE LOWER(CONCAT('%', :tipo, '%')) ORDER BY a.fechaSubida ASC")
    List<Archivo> buscarPorTipoLikeOrderByFechaAsc(String tipo);
}