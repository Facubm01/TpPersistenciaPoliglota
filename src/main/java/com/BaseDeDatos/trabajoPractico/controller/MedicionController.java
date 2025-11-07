package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.MedicionDto;
import com.BaseDeDatos.trabajoPractico.service.MedicionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/mediciones")
public class MedicionController {

    private final MedicionService medicionService;

    public MedicionController(MedicionService medicionService) {
        this.medicionService = medicionService;
    }

    @GetMapping
    public List<MedicionDto> porSensorYRango(
            @RequestParam String sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fin) {
        return medicionService.buscarPorSensorYRango(sensorId, inicio, fin);
    }

    @PostMapping
    public ResponseEntity<MedicionDto> crear(@RequestBody MedicionDto req) {
        MedicionDto creado = medicionService.crear(req);
        return ResponseEntity.created(URI.create("/api/mediciones?sensorId=" + creado.sensorId + "&inicio=" + creado.fechaHora + "&fin=" + creado.fechaHora)).body(creado);
    }

    @GetMapping("/ultimas")
    public List<MedicionDto> ultimas(@RequestParam String sensorId, @RequestParam(defaultValue = "100") int limit) {
        return medicionService.ultimas(sensorId, limit);
    }

    @GetMapping("/por-dia")
    public List<MedicionDto> porDia(
            @RequestParam String sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant diaUTC) {
        return medicionService.porDia(sensorId, diaUTC);
    }
}


