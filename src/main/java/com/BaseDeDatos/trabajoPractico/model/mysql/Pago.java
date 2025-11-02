package com.BaseDeDatos.trabajoPractico.model.mysql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    // AÑADIDO: Mapeo explícito
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    // AÑADIDO: Mapeo explícito
    @Column(name = "monto_pagado", nullable = false)
    private BigDecimal montoPagado;

    // AÑADIDO: Mapeo explícito
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;
}