package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.HistorialEjecucionDto;
import com.BaseDeDatos.trabajoPractico.model.mysql.HistorialEjecucion;
import com.BaseDeDatos.trabajoPractico.repository.mysql.HistorialEjecucionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialEjecucionService {

    private final HistorialEjecucionRepository historialRepository;

    public HistorialEjecucionService(HistorialEjecucionRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    // --- Método para convertir Entidad a DTO ---
    private HistorialEjecucionDto toDto(HistorialEjecucion h) {
        HistorialEjecucionDto dto = new HistorialEjecucionDto();
        dto.setId(h.getId());
        dto.setSolicitudId(h.getSolicitud() != null ? h.getSolicitud().getId() : null);
        dto.setFechaEjecucion(h.getFechaEjecucion());
        dto.setResultado(h.getResultado());
        dto.setEstado(h.getEstado());
        return dto;
    }

    /**
     * Registra una nueva entrada en el historial.
     * Este método será llamado por otros servicios (como ReporteService).
     */
    @Transactional
    public HistorialEjecucionDto registrarEjecucion(Long solicitudId, String resultado, String estado) {
        
        HistorialEjecucion registro = new HistorialEjecucion();
        
        // Aquí asumimos que tienes la entidad SolicitudProceso y su repositorio
        // y que el modelo HistorialEjecucion tiene una relación @ManyToOne con SolicitudProceso.
        // Si no tienes la relación, puedes guardar solo el ID.
        // Por simplicidad, este código asume que el repositorio de Solicitud no se inyecta aquí
        // y que el modelo HistorialEjecucion puede guardar el ID o la entidad.
        
        // --- Simplificación: Asumimos que el modelo guarda la entidad SolicitudProceso ---
        // SolicitudProceso solicitud = solicitudRepository.findById(solicitudId)
        //     .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        // registro.setSolicitud(solicitud);

        // --- Alternativa: Si tu modelo guarda solo el ID (ajusta tu entidad si es necesario) ---
        // registro.setSolicitudId(solicitudId); 
        // Por ahora, lo dejo comentado. Debes ajustar esto a tu modelo `HistorialEjecucion.java`.
        
        registro.setFechaEjecucion(LocalDateTime.now());
        registro.setResultado(resultado);
        registro.setEstado(estado);
        
        HistorialEjecucion guardado = historialRepository.save(registro);
        return toDto(guardado);
    }

    /**
     * Consulta el historial de una solicitud específica.
     * Cumple la consigna "Historial de ejecución de procesos".
     */
    public List<HistorialEjecucionDto> obtenerHistorialPorSolicitud(Long solicitudId) {
        // Necesitarás este método en tu HistorialEjecucionRepository:
        // List<HistorialEjecucion> findBySolicitudId(Long solicitudId);
        
        return historialRepository.findBySolicitudId(solicitudId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
