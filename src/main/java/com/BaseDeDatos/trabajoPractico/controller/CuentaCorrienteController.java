package com.BaseDeDatos.trabajoPractico.controller;

import com.BaseDeDatos.trabajoPractico.dto.CuentaCorrienteDto;
import com.BaseDeDatos.trabajoPractico.dto.MovimientoDto;
import com.BaseDeDatos.trabajoPractico.dto.MovimientoRequest;
import com.BaseDeDatos.trabajoPractico.service.CuentaCorrienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas-corrientes")
public class CuentaCorrienteController {

    private final CuentaCorrienteService cuentaCorrienteService;

    public CuentaCorrienteController(CuentaCorrienteService cuentaCorrienteService) {
        this.cuentaCorrienteService = cuentaCorrienteService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CuentaCorrienteDto> obtenerPorUsuario(@PathVariable Long usuarioId) {
        CuentaCorrienteDto dto = cuentaCorrienteService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/usuario/{usuarioId}/saldo")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable Long usuarioId) {
        BigDecimal saldo = cuentaCorrienteService.consultarSaldo(usuarioId);
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/usuario/{usuarioId}/historial")
    public List<MovimientoDto> obtenerHistorial(@PathVariable Long usuarioId) {
        return cuentaCorrienteService.obtenerHistorial(usuarioId);
    }

    @PostMapping("/movimientos")
    public ResponseEntity<MovimientoDto> registrarMovimiento(@Valid @RequestBody MovimientoRequest request) {
        MovimientoDto creado = cuentaCorrienteService.registrarMovimiento(request);
        return ResponseEntity.ok(creado);
    }

    @PostMapping("/acreditar")
    public ResponseEntity<MovimientoDto> acreditar(
            @RequestParam Long usuarioId,
            @RequestParam BigDecimal monto,
            @RequestParam(required = false) String descripcion) {
        MovimientoDto creado = cuentaCorrienteService.acreditar(
                usuarioId,
                monto,
                descripcion != null ? descripcion : "Acreditación manual"
        );
        return ResponseEntity.ok(creado);
    }

    @PostMapping("/debitar")
    public ResponseEntity<MovimientoDto> debitar(
            @RequestParam Long usuarioId,
            @RequestParam BigDecimal monto,
            @RequestParam(required = false) String descripcion) {
        MovimientoDto creado = cuentaCorrienteService.debitar(
                usuarioId,
                monto,
                descripcion != null ? descripcion : "Débito manual"
        );
        return ResponseEntity.ok(creado);
    }
}

