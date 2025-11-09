package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.PagoDto;
import com.BaseDeDatos.trabajoPractico.dto.PagoRequest;
import com.BaseDeDatos.trabajoPractico.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<PagoDto> listar() {
        return pagoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDto> obtener(@PathVariable Long id) {
        PagoDto dto = pagoService.obtener(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/factura/{facturaId}")
    public List<PagoDto> listarPorFactura(@PathVariable Long facturaId) {
        return pagoService.listarPorFactura(facturaId);
    }

    @PostMapping
    public ResponseEntity<PagoDto> registrarPago(@Valid @RequestBody PagoRequest request) {
        PagoDto creado = pagoService.registrarPago(request);
        return ResponseEntity.created(URI.create("/api/pagos/" + creado.id)).body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

