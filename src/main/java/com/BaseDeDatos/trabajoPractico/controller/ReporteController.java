package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * Endpoint para generar informe de máximas y mínimas.
     * Requiere una solicitud de proceso existente.
     */
    @PostMapping("/maxmin")
    public ResponseEntity<String> generarReporteMaxMin(
            @RequestParam Long solicitudId,
            @RequestParam String zona,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        try {
            reporteService.generarInformeMaxMin(solicitudId, zona, fechaInicio, fechaFin);
            return ResponseEntity.ok("Reporte de máximas y mínimas generado exitosamente para la solicitud ID: " + solicitudId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar reporte: " + e.getMessage());
        }
    }

    /**
     * Endpoint para generar informe de promedios.
     * Requiere una solicitud de proceso existente.
     */
    @PostMapping("/promedios")
    public ResponseEntity<String> generarReportePromedios(
            @RequestParam Long solicitudId,
            @RequestParam String zona,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        try {
            reporteService.generarInformePromedios(solicitudId, zona, fechaInicio, fechaFin);
            return ResponseEntity.ok("Reporte de promedios generado exitosamente para la solicitud ID: " + solicitudId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar reporte: " + e.getMessage());
        }
    }

    /**
     * Endpoint para "Servicios de consultas en línea..."
     * Nota: Este endpoint está simplificado ya que los métodos del servicio
     * requieren solicitudId y guardan en historial. Para una consulta en línea
     * pura, se necesitaría un método adicional en ReporteService que devuelva
     * el resultado sin guardarlo.
     */
    @GetMapping("/consulta-en-linea")
    public ResponseEntity<String> consultaEnLinea(
            @RequestParam String zona,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        // Nota: Para una consulta en línea real, se necesitaría un método en ReporteService
        // que devuelva el resultado sin guardarlo en historial. Por ahora, este endpoint
        // informa que se debe usar el endpoint de mediciones directamente.
        
        String mensaje = String.format(
            "Para consultas en línea directas, use el endpoint de mediciones: /api/mediciones/buscar?sensorId=...&inicio=...&fin=...\n" +
            "Consulta solicitada para zona: %s, desde %s hasta %s",
            zona, fechaInicio, fechaFin
        );
        return ResponseEntity.ok(mensaje);
    }
}