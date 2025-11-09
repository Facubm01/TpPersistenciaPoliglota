package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.SolicitudDeProcesoDto;
import com.BaseDeDatos.trabajoPractico.dto.SolicitudDeProcesoRequest;
import com.BaseDeDatos.trabajoPractico.service.SolicitudProcesoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudProcesoController {

    private final SolicitudProcesoService solicitudProcesoService;

    public SolicitudProcesoController(SolicitudProcesoService solicitudProcesoService) {
        this.solicitudProcesoService = solicitudProcesoService;
    }

    @GetMapping
    public List<SolicitudDeProcesoDto> listar() {
        return solicitudProcesoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDeProcesoDto> obtener(@PathVariable Long id) {
        SolicitudDeProcesoDto dto = solicitudProcesoService.obtener(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SolicitudDeProcesoDto> crear(@Valid @RequestBody SolicitudDeProcesoRequest request) {
        // Al crear, solo se necesita el usuarioId y procesoId. El estado es PENDIENTE por defecto.
        SolicitudDeProcesoDto creado = solicitudProcesoService.crear(request);
        return ResponseEntity.created(URI.create("/api/solicitudes/" + creado.id)).body(creado);
    }

    // Endpoint para actualizar el estado, ya que es el cambio más probable.
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudDeProcesoDto> actualizarEstado(
            @PathVariable Long id, 
            @RequestParam String estado) {
        SolicitudDeProcesoDto actualizado = solicitudProcesoService.actualizarEstado(id, estado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudProcesoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}