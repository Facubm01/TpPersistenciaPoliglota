package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.dto.MedicionDto;
import com.BaseDeDatos.trabajoPractico.service.MedicionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@ShellComponent
public class MedicionCommands {

    private final MedicionService medicionService;
    private final AuthCommands authCommands;

    public MedicionCommands(MedicionService medicionService, AuthCommands authCommands) {
        this.medicionService = medicionService;
        this.authCommands = authCommands;
    }

    @ShellMethod(value = "Carga una nueva medición de un sensor en Cassandra", key = "cargar-medicion")
    public String cargarMedicion(
            @ShellOption(help = "ID del sensor (de MongoDB)") String sensorId,
            @ShellOption(help = "Valor de temperatura") Double temperatura,
            @ShellOption(help = "Valor de humedad") Double humedad
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión primero.";
        }

        try {
            // Preparamos el DTO para el servicio
            MedicionDto dto = new MedicionDto();
            dto.sensorId = sensorId;
            dto.temperatura = temperatura;
            dto.humedad = humedad;
            // El servicio se encarga de poner la fecha actual
            
            MedicionDto creado = medicionService.crear(dto);

            return String.format("✅ Medición guardada en Cassandra. Sensor: %s, Temp: %.1f, Hum: %.1f",
                    creado.sensorId, creado.temperatura, creado.humedad);

        } catch (Exception e) {
            return "Error al cargar medición: " + e.getMessage();
        }
    }

    /**
     * Comando para consultas en línea de mediciones.
     * Cumple el requisito: "Servicios de consultas en línea".
     * Trae las mediciones directamente de Cassandra.
     */
    @ShellMethod(value = "Consulta mediciones en línea por sensor y rango de fechas", key = "consulta-en-linea")
    public String consultaEnLinea(
            @ShellOption(help = "ID del sensor (de MongoDB)") String sensorId,
            @ShellOption(help = "Fecha de inicio (Formato ISO: YYYY-MM-DDTHH:MM:SS)") String fechaInicioStr,
            @ShellOption(help = "Fecha de fin (Formato ISO: YYYY-MM-DDTHH:MM:SS)") String fechaFinStr
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión primero.";
        }

        try {
            // Convertir strings a Instant
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr);
            Instant inicio = fechaInicio.toInstant(ZoneOffset.UTC);
            Instant fin = fechaFin.toInstant(ZoneOffset.UTC);

            // Consultar directamente a Cassandra
            List<MedicionDto> mediciones = medicionService.buscarPorSensorYRango(sensorId, inicio, fin);
            
            if (mediciones.isEmpty()) {
                return String.format("No se encontraron mediciones para el sensor %s en el rango especificado.", sensorId);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("📊 Consulta en Línea - Mediciones de Cassandra\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            sb.append(String.format("Sensor ID: %s | Total de mediciones: %d\n", sensorId, mediciones.size()));
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            for (MedicionDto m : mediciones) {
                sb.append(String.format("Fecha: %s | Temp: %.2f°C | Hum: %.2f%%\n", 
                    m.fechaHora != null ? m.fechaHora.toString() : "N/A",
                    m.temperatura != null ? m.temperatura : 0.0,
                    m.humedad != null ? m.humedad : 0.0));
            }
            
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            
            return sb.toString();

        } catch (Exception e) {
            return "Error al consultar mediciones: " + e.getMessage();
        }
    }
}