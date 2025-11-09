package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.ProcesoDto;
import com.BaseDeDatos.trabajoPractico.dto.ProcesoRequest;
import com.BaseDeDatos.trabajoPractico.service.ProcesoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/procesos")
public class ProcesoController {

    private final ProcesoService procesoService;

    public ProcesoController(ProcesoService procesoService) {
        this.procesoService = procesoService;
    }

    @GetMapping
    public List<ProcesoDto> listar() {
        return procesoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcesoDto> obtener(@PathVariable Long id) {
        ProcesoDto dto = procesoService.obtener(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProcesoDto> crear(@Valid @RequestBody ProcesoRequest request) {
        ProcesoDto creado = procesoService.crear(request);
        return ResponseEntity.created(URI.create("/api/procesos/" + creado.id)).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcesoDto> actualizar(@PathVariable Long id, @Valid @RequestBody ProcesoRequest request) {
        ProcesoDto actualizado = procesoService.actualizar(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        procesoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}