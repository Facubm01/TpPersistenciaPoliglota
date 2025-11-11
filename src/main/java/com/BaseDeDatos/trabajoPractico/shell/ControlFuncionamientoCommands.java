package com.BaseDeDatos.trabajoPractico.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import com.BaseDeDatos.trabajoPractico.service.ControlFuncionamientoService;
import com.BaseDeDatos.trabajoPractico.dto.ControlFuncionamientoRequest; // <-- Necesitas esta importación
import java.time.LocalDateTime; // <-- Necesitas esta importación

@ShellComponent
public class ControlFuncionamientoCommands {

    private final ControlFuncionamientoService controlService;

    // Constructor para inyección (como corregimos anteriormente)
    public ControlFuncionamientoCommands(ControlFuncionamientoService controlService) {
        this.controlService = controlService;
    }

    @ShellMethod("Registra un evento de control o mantenimiento de un sensor.")
    public String registrarControl(String sensorId, String estado, String observaciones) {
        try {
            // 1. CONSTRUIR el objeto Request que el servicio espera
            ControlFuncionamientoRequest request = new ControlFuncionamientoRequest();
            request.setSensorId(sensorId);
            request.setEstadoSensor(estado);
            request.setObservaciones(observaciones);
            request.setFechaRevision(LocalDateTime.now()); // Asignar fecha actual

            // 2. Llamar al servicio con el objeto Request
            controlService.crear(request); 
            
            return "Control de Funcionamiento registrado para el Sensor ID: " + sensorId;
        } catch (Exception e) {
            // Manejar excepciones lanzadas por el Service (ej. Sensor no encontrado)
            return "ERROR al registrar control: " + e.getMessage();
        }
    }
}