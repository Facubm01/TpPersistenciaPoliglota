package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.service.ReporteService; // El Service que ya existe
import com.BaseDeDatos.trabajoPractico.service.HistorialEjecucionService;
import com.BaseDeDatos.trabajoPractico.dto.HistorialEjecucionDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

@ShellComponent
public class ReporteCommands {

    private final ReporteService reporteService;
    private final HistorialEjecucionService historialEjecucionService;
    private final AuthCommands authCommands;

    public ReporteCommands(ReporteService reporteService, 
                          HistorialEjecucionService historialEjecucionService,
                          AuthCommands authCommands) {
        this.reporteService = reporteService;
        this.historialEjecucionService = historialEjecucionService;
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

    /**
     * Comando para ver el historial de ejecución de una solicitud.
     * Cumple el requisito: "Historial de ejecución de procesos".
     */
    @ShellMethod(value = "Muestra el historial de ejecución de una solicitud", key = "ver-historial")
    public String verHistorial(
            @ShellOption(help = "ID de la solicitud") Long solicitudId
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión primero.";
        }

        try {
            List<HistorialEjecucionDto> historial = historialEjecucionService.obtenerHistorialPorSolicitud(solicitudId);
            
            if (historial.isEmpty()) {
                return "No hay historial de ejecución para la solicitud ID: " + solicitudId;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("📋 Historial de ejecución para Solicitud ID: ").append(solicitudId).append("\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            for (HistorialEjecucionDto h : historial) {
                sb.append(String.format("ID: %d | Fecha: %s | Estado: %s\n", 
                    h.getId(), 
                    h.getFechaEjecucion(), 
                    h.getEstado()));
                sb.append("Resultado: ").append(h.getResultado()).append("\n");
                sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            }
            
            return sb.toString();

        } catch (Exception e) {
            return "Error al obtener historial: " + e.getMessage();
        }
    }

    /**
     * Comando para generar el informe de promedios.
     * Similar a reporte-maxmin pero calcula promedios en lugar de máximos/mínimos.
     */
    @ShellMethod(value = "Genera informe de promedios para una solicitud", key = "reporte-promedios")
    public String generarReportePromedios(
            @ShellOption(help = "ID de la solicitud (obtenido con 'solicitar-proceso')") Long solicitudId,
            @ShellOption(help = "Zona, Ciudad o País para el reporte") String zona,
            @ShellOption(help = "Fecha de inicio (Formato ISO: YYYY-MM-DDTHH:MM:SS)") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @ShellOption(help = "Fecha de fin (Formato ISO: YYYY-MM-DDTHH:MM:SS)") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión primero.";
        }

        try {
            // Llamar al Service con todos los parámetros
            // Este servicio ejecutará la lógica políglota:
            // MongoDB (buscar sensores) -> Cassandra (buscar mediciones) -> MySQL (guardar historial)
            reporteService.generarInformePromedios(solicitudId, zona, fechaInicio, fechaFin);
            
            return "✅ Reporte de promedios para Solicitud ID: " + solicitudId + " generado y guardado en el historial.";

        } catch (Exception e) {
            return "Error al generar reporte: " + e.getMessage();
        }
    }
}