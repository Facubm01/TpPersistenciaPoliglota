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
     * Endpoint para "Servicios de consultas en línea..." 
     * Este es un ejemplo. La lógica de negocio real debería estar en ReporteService.
     */
    @GetMapping("/consulta-en-linea")
    public ResponseEntity<String> consultaEnLinea(
            @RequestParam String zona,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        // Esta es una implementación SIMPLIFICADA.
        // Lo ideal es que ReporteService tenga un método "consultarMaxMin"
        // que DEVUELVA el resultado en lugar de guardarlo en el historial.
        
        // Aquí simulamos una llamada:
        // ReporteResultadoDto resultado = reporteService.consultarMaxMinEnLinea(zona, fechaInicio, fechaFin);
        // return ResponseEntity.ok(resultado);

        String simulaResultado = String.format(
            "Consulta en línea para %s desde %s hasta %s (simulado)",
            zona, fechaInicio, fechaFin
        );
        return ResponseEntity.ok(simulaResultado);
    }
}