package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.model.mongo.Alerta;
import com.BaseDeDatos.trabajoPractico.repository.mongo.AlertaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaRepository alertaRepository;

    public AlertaController(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @GetMapping
    public List<Alerta> listar() { return alertaRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> obtener(@PathVariable String id) {
        return alertaRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Alerta> crear(@RequestBody Alerta a) {
        if (a.getFechaHora() == null) a.setFechaHora(new Date());
        Alerta creada = alertaRepository.save(a);
        return ResponseEntity.created(URI.create("/api/alertas/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alerta> actualizar(@PathVariable String id, @RequestBody Alerta req) {
        return alertaRepository.findById(id)
                .map(a -> {
                    a.setTipo(req.getTipo());
                    a.setSensorId(req.getSensorId());
                    a.setFechaHora(req.getFechaHora() != null ? req.getFechaHora() : a.getFechaHora());
                    a.setDescripcion(req.getDescripcion());
                    a.setEstado(req.getEstado());
                    return ResponseEntity.ok(alertaRepository.save(a));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!alertaRepository.existsById(id)) return ResponseEntity.notFound().build();
        alertaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


