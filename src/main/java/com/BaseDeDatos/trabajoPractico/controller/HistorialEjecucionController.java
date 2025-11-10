package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.HistorialEjecucionDto;
import com.BaseDeDatos.trabajoPractico.service.HistorialEjecucionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialEjecucionController {

    private final HistorialEjecucionService historialService;

    public HistorialEjecucionController(HistorialEjecucionService historialService) {
        this.historialService = historialService;
    }

    /**
     * Endpoint para consultar el historial de una solicitud de proceso.
     */
    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<HistorialEjecucionDto>> obtenerHistorialPorSolicitud(@PathVariable Long solicitudId) {
        List<HistorialEjecucionDto> historial = historialService.obtenerHistorialPorSolicitud(solicitudId);
        if (historial.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(historial);
    }
}