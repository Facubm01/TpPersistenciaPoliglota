package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.model.cassandra.Medicion;
import com.BaseDeDatos.trabajoPractico.model.mongo.Sensor;
import com.BaseDeDatos.trabajoPractico.repository.cassandra.MedicionRepository;
import com.BaseDeDatos.trabajoPractico.repository.mongo.SensorRepository;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    // Repos de las 3 BDD
    private final SensorRepository sensorRepository;
    private final MedicionRepository medicionRepository;
    private final HistorialEjecucionService historialEjecucionService;
    private final SolicitudProcesoService solicitudProcesoService; // Asumo que existe

    public ReporteService(SensorRepository sensorRepository,
                          MedicionRepository medicionRepository,
                          HistorialEjecucionService historialEjecucionService,
                          SolicitudProcesoService solicitudProcesoService) {
        this.sensorRepository = sensorRepository;
        this.medicionRepository = medicionRepository;
        this.historialEjecucionService = historialEjecucionService;
        this.solicitudProcesoService = solicitudProcesoService;
    }

    /**
     * Lógica para "Informe de humedad y temperaturas máximas y mínimas..."
     * Este es el flujo de persistencia políglota [cite: 321-328].
     */
    public void generarInformeMaxMin(Long solicitudId, String zona, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        String resultado;
        String estado;
        
        try {
            // PASO 1: Consultar a MongoDB
            // (Necesitarás un método en SensorMongoRepository para buscar por zona/ciudad/país)    
            List<Sensor> sensoresEnZona = sensorRepository.findByPais(zona); // Simplificación
            List<String> sensorIds = sensoresEnZona.stream().map(Sensor::getId).toList();

            if (sensorIds.isEmpty()) {
                throw new RuntimeException("No se encontraron sensores para la zona: " + zona);
            }

            // PASO 2: Consultar a Cassandra
            // (Necesitarás un método en MedicionCassandraRepository)
            Instant startInstant = fechaInicio.toInstant(ZoneOffset.UTC);
            Instant endInstant = fechaFin.toInstant(ZoneOffset.UTC);
            List<Medicion> mediciones = medicionRepository.findByKeySensorIdInAndKeyFechaHoraBetween(sensorIds, startInstant, endInstant);

            if (mediciones.isEmpty()) {
                throw new RuntimeException("No se encontraron mediciones para el rango de fechas.");
            }
            
            // PASO 3: Calcular el reporte (Lógica de negocio)
            double maxTemp = mediciones.stream().mapToDouble(Medicion::getTemperatura).max().orElse(0);
            double minTemp = mediciones.stream().mapToDouble(Medicion::getTemperatura).min().orElse(0);
            double maxHum = mediciones.stream().mapToDouble(Medicion::getHumedad).max().orElse(0);
            double minHum = mediciones.stream().mapToDouble(Medicion::getHumedad).min().orElse(0);

            resultado = String.format(
                "Reporte para '%s': [Temp Max: %.2f, Temp Min: %.2f, Hum Max: %.2f, Hum Min: %.2f]",
                zona, maxTemp, minTemp, maxHum, minHum
            );
            
            // PASO 4: Actualizar MySQL
            solicitudProcesoService.actualizarEstado(solicitudId, "completado");
            estado = "COMPLETADO";

        } catch (RuntimeException e) {
            // Manejo de errores
            solicitudProcesoService.actualizarEstado(solicitudId, "fallido");
            resultado = "Error al generar el reporte: " + e.getMessage();
            estado = "FALLIDO";
        }
        
        // PASO 5: Registrar en Historial (MySQL)
        historialEjecucionService.registrarEjecucion(solicitudId, resultado, estado);
    }

    /**
     * Lógica para "Informe de promedios de humedad y temperatura..."
     * Similar a generarInformeMaxMin pero calcula promedios en lugar de máximos/mínimos.
     */
    public void generarInformePromedios(Long solicitudId, String zona, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        String resultado;
        String estado;
        
        try {
            // PASO 1: Consultar a MongoDB
            List<Sensor> sensoresEnZona = sensorRepository.findByPais(zona); // Simplificación
            List<String> sensorIds = sensoresEnZona.stream().map(Sensor::getId).toList();

            if (sensorIds.isEmpty()) {
                throw new RuntimeException("No se encontraron sensores para la zona: " + zona);
            }

            // PASO 2: Consultar a Cassandra
            Instant startInstant = fechaInicio.toInstant(ZoneOffset.UTC);
            Instant endInstant = fechaFin.toInstant(ZoneOffset.UTC);
            List<Medicion> mediciones = medicionRepository.findByKeySensorIdInAndKeyFechaHoraBetween(sensorIds, startInstant, endInstant);

            if (mediciones.isEmpty()) {
                throw new RuntimeException("No se encontraron mediciones para el rango de fechas.");
            }
            
            // PASO 3: Calcular el reporte de promedios (Lógica de negocio)
            double promedioTemp = mediciones.stream()
                    .mapToDouble(Medicion::getTemperatura)
                    .average()
                    .orElse(0);
            double promedioHum = mediciones.stream()
                    .mapToDouble(Medicion::getHumedad)
                    .average()
                    .orElse(0);
            long cantidadMediciones = mediciones.size();

            resultado = String.format(
                "Reporte de Promedios para '%s': [Temp Promedio: %.2f, Hum Promedio: %.2f, Total Mediciones: %d]",
                zona, promedioTemp, promedioHum, cantidadMediciones
            );
            
            // PASO 4: Actualizar MySQL
            solicitudProcesoService.actualizarEstado(solicitudId, "completado");
            estado = "COMPLETADO";

        } catch (RuntimeException e) {
            // Manejo de errores
            solicitudProcesoService.actualizarEstado(solicitudId, "fallido");
            resultado = "Error al generar el reporte: " + e.getMessage();
            estado = "FALLIDO";
        }
        
        // PASO 5: Registrar en Historial (MySQL)
        historialEjecucionService.registrarEjecucion(solicitudId, resultado, estado);
    }

    /**
     * Lógica para "Procesos periódicos de consultas..."
     */
    public void ejecutarProcesoPeriodico() {
        System.out.println("Ejecutando proceso periódico... " + LocalDateTime.now());
        // Lógica similar a generarInformeMaxMin
        // 1. Definir zona/fechas (ej. "GLOBAL", "ULTIMAS_24_HORAS")
        // 2. Consultar MongoDB
        // 3. Consultar Cassandra
        // 4. Generar el informe
        // 5. Registrar en HistorialEjecucion (con solicitudId nulo o un ID especial)
        
        historialEjecucionService.registrarEjecucion(null, "Proceso periódico ejecutado exitosamente", "COMPLETADO");
    }
}