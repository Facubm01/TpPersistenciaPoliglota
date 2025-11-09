package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.CuentaCorrienteDto;
import com.BaseDeDatos.trabajoPractico.dto.MovimientoDto;
import com.BaseDeDatos.trabajoPractico.dto.MovimientoRequest;
import com.BaseDeDatos.trabajoPractico.model.mysql.CuentaCorriente;
import com.BaseDeDatos.trabajoPractico.model.mysql.MovimientoCuentaCorriente;
import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import com.BaseDeDatos.trabajoPractico.repository.mysql.CuentaCorrienteRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.MovimientoCuentaCorrienteRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaCorrienteService {

    private final CuentaCorrienteRepository cuentaCorrienteRepository;
    private final MovimientoCuentaCorrienteRepository movimientoRepository;
    private final UsuarioRepository usuarioRepository;

    public CuentaCorrienteService(CuentaCorrienteRepository cuentaCorrienteRepository,
                                  MovimientoCuentaCorrienteRepository movimientoRepository,
                                  UsuarioRepository usuarioRepository) {
        this.cuentaCorrienteRepository = cuentaCorrienteRepository;
        this.movimientoRepository = movimientoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private CuentaCorrienteDto toDto(CuentaCorriente cc) {
        CuentaCorrienteDto dto = new CuentaCorrienteDto();
        dto.id = cc.getId();
        dto.usuarioId = cc.getUsuario().getId();
        dto.usuarioEmail = cc.getUsuario().getEmail();
        dto.saldoActual = cc.getSaldoActual();
        return dto;
    }

    private MovimientoDto toMovimientoDto(MovimientoCuentaCorriente m) {
        MovimientoDto dto = new MovimientoDto();
        dto.id = m.getId();
        dto.cuentaCorrienteId = m.getCuentaCorriente().getId();
        dto.usuarioId = m.getCuentaCorriente().getUsuario().getId();
        dto.tipo = m.getTipo();
        dto.monto = m.getMonto();
        dto.fecha = m.getFecha();
        dto.descripcion = m.getDescripcion();
        return dto;
    }

    /**
     * Obtiene o crea la cuenta corriente de un usuario
     */
    private CuentaCorriente obtenerOCrearCuentaCorriente(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        return cuentaCorrienteRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    CuentaCorriente nueva = new CuentaCorriente();
                    nueva.setUsuario(usuario);
                    nueva.setSaldoActual(BigDecimal.ZERO);
                    return cuentaCorrienteRepository.save(nueva);
                });
    }

    public CuentaCorrienteDto obtenerPorUsuario(Long usuarioId) {
        CuentaCorriente cc = obtenerOCrearCuentaCorriente(usuarioId);
        return toDto(cc);
    }

    public BigDecimal consultarSaldo(Long usuarioId) {
        CuentaCorriente cc = obtenerOCrearCuentaCorriente(usuarioId);
        return cc.getSaldoActual();
    }

    public List<MovimientoDto> obtenerHistorial(Long usuarioId) {
        CuentaCorriente cc = obtenerOCrearCuentaCorriente(usuarioId);
        return movimientoRepository.findByCuentaCorrienteIdOrderByFechaDesc(cc.getId())
                .stream()
                .map(this::toMovimientoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovimientoDto registrarMovimiento(MovimientoRequest req) {
        CuentaCorriente cc = obtenerOCrearCuentaCorriente(req.usuarioId);

        // Validar tipo
        if (!"ACREDITACION".equals(req.tipo) && !"DEBITO".equals(req.tipo)) {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Debe ser ACREDITACION o DEBITO");
        }

        // Crear movimiento
        MovimientoCuentaCorriente movimiento = new MovimientoCuentaCorriente();
        movimiento.setCuentaCorriente(cc);
        movimiento.setTipo(req.tipo);
        movimiento.setMonto(req.monto);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion(req.descripcion != null ? req.descripcion : "");

        // Actualizar saldo
        if ("ACREDITACION".equals(req.tipo)) {
            cc.setSaldoActual(cc.getSaldoActual().add(req.monto));
        } else { // DEBITO
            if (cc.getSaldoActual().compareTo(req.monto) < 0) {
                throw new IllegalStateException("Saldo insuficiente. Saldo actual: " + cc.getSaldoActual() + ", intento de débito: " + req.monto);
            }
            cc.setSaldoActual(cc.getSaldoActual().subtract(req.monto));
        }

        cuentaCorrienteRepository.save(cc);
        MovimientoCuentaCorriente movimientoGuardado = movimientoRepository.save(movimiento);

        return toMovimientoDto(movimientoGuardado);
    }

    @Transactional
    public MovimientoDto acreditar(Long usuarioId, BigDecimal monto, String descripcion) {
        MovimientoRequest req = new MovimientoRequest();
        req.usuarioId = usuarioId;
        req.monto = monto;
        req.tipo = "ACREDITACION";
        req.descripcion = descripcion;
        return registrarMovimiento(req);
    }

    @Transactional
    public MovimientoDto debitar(Long usuarioId, BigDecimal monto, String descripcion) {
        MovimientoRequest req = new MovimientoRequest();
        req.usuarioId = usuarioId;
        req.monto = monto;
        req.tipo = "DEBITO";
        req.descripcion = descripcion;
        return registrarMovimiento(req);
    }
}

