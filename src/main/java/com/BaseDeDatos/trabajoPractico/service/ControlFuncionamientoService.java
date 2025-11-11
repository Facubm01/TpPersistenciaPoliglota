package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.ControlFuncionamientoDto;
import com.BaseDeDatos.trabajoPractico.dto.ControlFuncionamientoRequest;
import com.BaseDeDatos.trabajoPractico.model.mongo.ControlFuncionamiento;
import com.BaseDeDatos.trabajoPractico.repository.mongo.ControlFuncionamientoRepository;
import com.BaseDeDatos.trabajoPractico.repository.mongo.SensorRepository;
import lombok.RequiredArgsConstructor; // <-- Importación para el constructor automático
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // <-- AÑADIDO: Genera el constructor para los campos 'final'
public class ControlFuncionamientoService {

    // Deben ser 'final' para que Lombok los inyecte en el constructor
    private final ControlFuncionamientoRepository controlRepository;
    private final SensorRepository sensorRepository;

    // EL CONSTRUCTOR MANUAL SE ELIMINA GRACIAS A @RequiredArgsConstructor

    private ControlFuncionamientoDto toDto(ControlFuncionamiento c) {
        ControlFuncionamientoDto dto = new ControlFuncionamientoDto();
        
        dto.id = c.getId();
        dto.sensorId = c.getSensorId();
        dto.fechaRevision = c.getFechaRevision();
        dto.estadoSensor = c.getEstadoSensor();
        dto.observaciones = c.getObservaciones();
        return dto;
    }

    public List<ControlFuncionamientoDto> listar() {
        return controlRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ControlFuncionamientoDto obtener(String id) {
        return controlRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public List<ControlFuncionamientoDto> listarPorSensor(String sensorId) {
        return controlRepository.findBySensorId(sensorId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ControlFuncionamientoDto> listarPorEstado(String estadoSensor) {
        return controlRepository.findByEstadoSensor(estadoSensor).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ControlFuncionamientoDto crear(ControlFuncionamientoRequest req) {
        // Validar que el sensor existe (USO DEL GETTER CORREGIDO)
        if (!sensorRepository.existsById(req.getSensorId())) {
            throw new IllegalArgumentException("Sensor no encontrado con ID: " + req.getSensorId());
        }

        ControlFuncionamiento control = new ControlFuncionamiento();
        
        
        control.setSensorId(req.getSensorId());
        control.setFechaRevision(req.getFechaRevision());
        control.setEstadoSensor(req.getEstadoSensor());
        control.setObservaciones(req.getObservaciones());

        return toDto(controlRepository.save(control));
    }

    public ControlFuncionamientoDto actualizar(String id, ControlFuncionamientoRequest req) {
        ControlFuncionamiento control = controlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Control de funcionamiento no encontrado con ID: " + id));

        // Validar que el sensor existe (USO DEL GETTER CORREGIDO)
        if (!sensorRepository.existsById(req.getSensorId())) {
            throw new IllegalArgumentException("Sensor no encontrado con ID: " + req.getSensorId());
        }

        // Actualizar todos los campos (USO DE GETTERS CORREGIDO)
        control.setSensorId(req.getSensorId());
        control.setFechaRevision(req.getFechaRevision());
        control.setEstadoSensor(req.getEstadoSensor());
        control.setObservaciones(req.getObservaciones());

        return toDto(controlRepository.save(control));
    }

    public void eliminar(String id) {
        if (!controlRepository.existsById(id)) {
            throw new IllegalArgumentException("Control de funcionamiento no encontrado con ID: " + id);
        }
        controlRepository.deleteById(id);
    }
}