package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.PagoDto;
import com.BaseDeDatos.trabajoPractico.dto.PagoRequest;
import com.BaseDeDatos.trabajoPractico.model.mysql.Factura;
import com.BaseDeDatos.trabajoPractico.model.mysql.Pago;
import com.BaseDeDatos.trabajoPractico.repository.mysql.FacturaRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final FacturaRepository facturaRepository;
    private final CuentaCorrienteService cuentaCorrienteService;

    public PagoService(PagoRepository pagoRepository,
                       FacturaRepository facturaRepository,
                       CuentaCorrienteService cuentaCorrienteService) {
        this.pagoRepository = pagoRepository;
        this.facturaRepository = facturaRepository;
        this.cuentaCorrienteService = cuentaCorrienteService;
    }

    private PagoDto toDto(Pago p) {
        PagoDto dto = new PagoDto();
        dto.id = p.getId();
        dto.facturaId = p.getFactura().getId();
        dto.usuarioId = p.getFactura().getUsuario().getId();
        dto.usuarioEmail = p.getFactura().getUsuario().getEmail();
        dto.fechaPago = p.getFechaPago();
        dto.montoPagado = p.getMontoPagado();
        dto.metodoPago = p.getMetodoPago();
        return dto;
    }

    public List<PagoDto> listar() {
        return pagoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PagoDto obtener(Long id) {
        return pagoRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public List<PagoDto> listarPorFactura(Long facturaId) {
        return pagoRepository.findByFacturaId(facturaId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PagoDto registrarPago(PagoRequest req) {
        // 1. Validar que la factura existe
        Factura factura = facturaRepository.findById(req.facturaId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + req.facturaId));

        // 2. Validar que el monto no sea mayor al total pendiente de la factura
        // (Por simplicidad, asumimos que el monto pagado puede ser parcial o total)
        
        // 3. Crear el pago
        Pago pago = new Pago();
        pago.setFactura(factura);
        pago.setFechaPago(LocalDateTime.now());
        pago.setMontoPagado(req.montoPagado);
        pago.setMetodoPago(req.metodoPago);

        Pago pagoGuardado = pagoRepository.save(pago);

        // 4. Acreditar en cuenta corriente del usuario
        cuentaCorrienteService.acreditar(
                factura.getUsuario().getId(),
                req.montoPagado,
                "Pago de factura #" + factura.getId() + " - " + req.metodoPago
        );

        // 5. Actualizar estado de la factura si está completamente pagada
        // (Por simplicidad, asumimos que una factura puede tener múltiples pagos)
        BigDecimal totalPagado = pagoRepository.findByFacturaId(factura.getId()).stream()
                .map(Pago::getMontoPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular total de la factura (suma de costos de procesos)
        // Necesitamos recargar la factura para obtener las relaciones
        Factura facturaCompleta = facturaRepository.findById(factura.getId())
                .orElse(factura);
        BigDecimal totalFactura = facturaCompleta.getProcesosFacturados().stream()
                .map(s -> s.getProceso().getCosto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPagado.compareTo(totalFactura) >= 0) {
            factura.setEstado("PAGADA");
            facturaRepository.save(factura);
        }

        return toDto(pagoGuardado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + id);
        }
        // Nota: En un sistema real, eliminar un pago debería revertir la acreditación
        // en cuenta corriente y actualizar el estado de la factura
        pagoRepository.deleteById(id);
    }
}

