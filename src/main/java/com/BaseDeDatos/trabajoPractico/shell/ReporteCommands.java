package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.service.ReporteService; // El Service que ya existe
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@ShellComponent
public class ReporteCommands {

    private final ReporteService reporteService;
    private final AuthCommands authCommands;

    public ReporteCommands(ReporteService reporteService, AuthCommands authCommands) {
        this.reporteService = reporteService;
        this.authCommands = authCommands;
    }

    /**
     * Comando para generar el informe de máximas y mínimas.
     * Esto cumple con el requisito del TPO[cite: 204].
     */
    @ShellMethod(value = "Genera informe de max/min para una solicitud", key = "reporte-maxmin")
    public String generarReporteMaxMin(
            @ShellOption(help = "ID de la solicitud (obtenido con 'solicitar-proceso')") Long solicitudId,
            @ShellOption(help = "Zona, Ciudad o País para el reporte") String zona,
            @ShellOption(help = "Fecha de inicio (Formato ISO: YYYY-MM-DDTHH:MM:SS)") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @ShellOption(help = "Fecha de fin (Formato ISO: YYYY-MM-DDTHH:MM:SS)") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión primero.";
        }

        try {
            // 2. Llamar al Service con todos los parámetros
            // Este servicio ejecutará la lógica políglota:
            // MongoDB (buscar sensores) -> Cassandra (buscar mediciones) -> MySQL (guardar historial) [cite: 376-382]
            reporteService.generarInformeMaxMin(solicitudId, zona, fechaInicio, fechaFin);
            
            return "✅ Reporte para Solicitud ID: " + solicitudId + " generado y guardado en el historial.";

        } catch (Exception e) {
            return "Error al generar reporte: " + e.getMessage();
        }
    }
}