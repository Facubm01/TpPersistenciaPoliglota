package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.model.mongo.Sensor;
import com.BaseDeDatos.trabajoPractico.repository.mongo.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sensores")
public class SensorController {

    private final SensorRepository sensorRepository;

    public SensorController(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @GetMapping
    public List<Sensor> listar() { return sensorRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> obtener(@PathVariable String id) {
        return sensorRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sensor> crear(@RequestBody Sensor s) {
        Sensor creado = sensorRepository.save(s);
        return ResponseEntity.created(URI.create("/api/sensores/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> actualizar(@PathVariable String id, @RequestBody Sensor req) {
        return sensorRepository.findById(id)
                .map(s -> {
                    s.setNombreCodigo(req.getNombreCodigo());
                    s.setTipoSensor(req.getTipoSensor());
                    s.setUbicacion(req.getUbicacion());
                    s.setCiudad(req.getCiudad());
                    s.setPais(req.getPais());
                    s.setEstadoSensor(req.getEstadoSensor());
                    s.setFechaInicioEmision(req.getFechaInicioEmision());
                    return ResponseEntity.ok(sensorRepository.save(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!sensorRepository.existsById(id)) return ResponseEntity.notFound().build();
        sensorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


