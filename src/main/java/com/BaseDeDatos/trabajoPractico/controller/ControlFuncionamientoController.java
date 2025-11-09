package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.ControlFuncionamientoDto;
import com.BaseDeDatos.trabajoPractico.dto.ControlFuncionamientoRequest;
import com.BaseDeDatos.trabajoPractico.service.ControlFuncionamientoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/controles-funcionamiento")
public class ControlFuncionamientoController {

    private final ControlFuncionamientoService controlService;

    public ControlFuncionamientoController(ControlFuncionamientoService controlService) {
        this.controlService = controlService;
    }

    @GetMapping
    public List<ControlFuncionamientoDto> listar() {
        return controlService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ControlFuncionamientoDto> obtener(@PathVariable String id) {
        ControlFuncionamientoDto dto = controlService.obtener(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/sensor/{sensorId}")
    public List<ControlFuncionamientoDto> listarPorSensor(@PathVariable String sensorId) {
        return controlService.listarPorSensor(sensorId);
    }

    @GetMapping("/estado/{estadoSensor}")
    public List<ControlFuncionamientoDto> listarPorEstado(@PathVariable String estadoSensor) {
        return controlService.listarPorEstado(estadoSensor);
    }

    @PostMapping
    public ResponseEntity<ControlFuncionamientoDto> crear(@Valid @RequestBody ControlFuncionamientoRequest request) {
        ControlFuncionamientoDto creado = controlService.crear(request);
        return ResponseEntity.created(URI.create("/api/controles-funcionamiento/" + creado.id)).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ControlFuncionamientoDto> actualizar(
            @PathVariable String id,
            @Valid @RequestBody ControlFuncionamientoRequest request) {
        ControlFuncionamientoDto actualizado = controlService.actualizar(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        controlService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

