package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.FacturaDto;
import com.BaseDeDatos.trabajoPractico.dto.FacturaRequest;
import com.BaseDeDatos.trabajoPractico.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public List<FacturaDto> listar() {
        return facturaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaDto> obtener(@PathVariable Long id) {
        FacturaDto dto = facturaService.obtener(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FacturaDto> crear(@Valid @RequestBody FacturaRequest request) {
        FacturaDto creada = facturaService.generarFactura(request);
        return ResponseEntity.created(URI.create("/api/facturas/" + creada.id)).body(creada);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<FacturaDto> actualizarEstado(
            @PathVariable Long id, 
            @RequestParam String estado) {
        FacturaDto actualizado = facturaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(actualizado);
    }
}
