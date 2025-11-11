package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.dto.MedicionDto;
import com.BaseDeDatos.trabajoPractico.service.MedicionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.time.Instant;

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
}