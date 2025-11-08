package com.BaseDeDatos.trabajoPractico.shell;

import java.util.Date;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.BaseDeDatos.trabajoPractico.model.mongo.Sensor;
import com.BaseDeDatos.trabajoPractico.repository.mongo.SensorRepository;

@ShellComponent // 1. Le dice a Spring que esta clase contiene comandos
public class SensorCommands {

    // 2. Inyectamos el Repositorio de Mongo que ya creamos
    private final SensorRepository sensorRepository;

    public SensorCommands(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    // 3. ¡Definimos nuestro primer comando!
    @ShellMethod(value = "Agrega un nuevo sensor a MongoDB", key = "add-sensor")
    public String addSensor(
            @ShellOption(help = "Nombre o código del sensor") String nombre,
            @ShellOption(help = "Tipo de sensor, ej: temperatura") String tipo,
            @ShellOption(help = "Ciudad donde está ubicado") String ciudad,
            @ShellOption(help = "Longitud, ej: -60.6393") double lon,
            @ShellOption(help = "Latitud, ej: -32.9468") double lat
    ) {
        
        System.out.println("Recibida solicitud para crear sensor: " + nombre);

        // 4. Creamos el objeto Sensor
        Sensor sensor = new Sensor();
        sensor.setNombreCodigo(nombre);
        sensor.setTipoSensor(tipo);
        sensor.setCiudad(ciudad);
        sensor.setEstadoSensor("activo");
        sensor.setFechaInicioEmision(new Date()); // Fecha y hora actual

        // 5. Creamos el objeto GeoJsonPoint (¡Importante: es Longitud, Latitud!)
        GeoJsonPoint ubicacion = new GeoJsonPoint(lon, lat);
        sensor.setUbicacion(ubicacion);

        // 6. ¡Lo guardamos en MongoDB!
        Sensor sensorGuardado = sensorRepository.save(sensor);

        // 7. Devolvemos una respuesta a la consola
        return "✅ Sensor guardado en MongoDB con ID: " + sensorGuardado.getId();
    }
}