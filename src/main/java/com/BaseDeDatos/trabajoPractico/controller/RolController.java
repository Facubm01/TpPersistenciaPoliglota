package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.RolDto;
import com.BaseDeDatos.trabajoPractico.dto.RolRequest;
import com.BaseDeDatos.trabajoPractico.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<RolDto> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDto> obtener(@PathVariable Long id) {
        RolDto dto = rolService.obtener(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RolDto> crear(@Valid @RequestBody RolRequest request) {
        RolDto creado = rolService.crear(request);
        return ResponseEntity.created(URI.create("/api/roles/" + creado.id)).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDto> actualizar(@PathVariable Long id, @Valid @RequestBody RolRequest request) {
        RolDto actualizado = rolService.actualizar(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}


