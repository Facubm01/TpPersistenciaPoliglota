package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.ControlFuncionamientoDto;
import com.BaseDeDatos.trabajoPractico.dto.ControlFuncionamientoRequest;
import com.BaseDeDatos.trabajoPractico.model.mongo.ControlFuncionamiento;
import com.BaseDeDatos.trabajoPractico.repository.mongo.ControlFuncionamientoRepository;
import com.BaseDeDatos.trabajoPractico.repository.mongo.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ControlFuncionamientoService {

    private final ControlFuncionamientoRepository controlRepository;
    private final SensorRepository sensorRepository;

    public ControlFuncionamientoService(ControlFuncionamientoRepository controlRepository,
                                       SensorRepository sensorRepository) {
        this.controlRepository = controlRepository;
        this.sensorRepository = sensorRepository;
    }

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
        // Validar que el sensor existe
        if (!sensorRepository.existsById(req.sensorId)) {
            throw new IllegalArgumentException("Sensor no encontrado con ID: " + req.sensorId);
        }

        ControlFuncionamiento control = new ControlFuncionamiento();
        control.setSensorId(req.sensorId);
        control.setFechaRevision(req.fechaRevision);
        control.setEstadoSensor(req.estadoSensor);
        control.setObservaciones(req.observaciones);

        return toDto(controlRepository.save(control));
    }

    public ControlFuncionamientoDto actualizar(String id, ControlFuncionamientoRequest req) {
        ControlFuncionamiento control = controlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Control de funcionamiento no encontrado con ID: " + id));

        // Validar que el sensor existe
        if (!sensorRepository.existsById(req.sensorId)) {
            throw new IllegalArgumentException("Sensor no encontrado con ID: " + req.sensorId);
        }

        // Actualizar todos los campos (consistente con ProcesoService)
        control.setSensorId(req.sensorId);
        control.setFechaRevision(req.fechaRevision);
        control.setEstadoSensor(req.estadoSensor);
        control.setObservaciones(req.observaciones);

        return toDto(controlRepository.save(control));
    }

    public void eliminar(String id) {
        if (!controlRepository.existsById(id)) {
            throw new IllegalArgumentException("Control de funcionamiento no encontrado con ID: " + id);
        }
        controlRepository.deleteById(id);
    }
}

