package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.HistorialEjecucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEjecucionRepository extends JpaRepository<HistorialEjecucion, Long> {

    /**
     * Método requerido por HistorialEjecucionService.
     * Busca todas las entradas de historial asociadas a un ID de solicitud específico.
     * Usa la sintaxis correcta para relaciones @ManyToOne: findBySolicitud_Id
     */
    List<HistorialEjecucion> findBySolicitud_Id(Long solicitudId);
    
}