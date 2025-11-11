package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.HistorialEjecucionDto;
import com.BaseDeDatos.trabajoPractico.model.mysql.HistorialEjecucion;
import com.BaseDeDatos.trabajoPractico.model.mysql.SolicitudDeProceso;
import com.BaseDeDatos.trabajoPractico.repository.mysql.HistorialEjecucionRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.SolicitudDeProcesoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialEjecucionService {

    private final HistorialEjecucionRepository historialRepository;
    private final SolicitudDeProcesoRepository solicitudRepository;

    public HistorialEjecucionService(HistorialEjecucionRepository historialRepository,
                                     SolicitudDeProcesoRepository solicitudRepository) {
        this.historialRepository = historialRepository;
        this.solicitudRepository = solicitudRepository;
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
        
        // Obtener la solicitud para establecer la relación
        SolicitudDeProceso solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + solicitudId));
        
        registro.setSolicitud(solicitud);
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
        return historialRepository.findBySolicitud_Id(solicitudId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
