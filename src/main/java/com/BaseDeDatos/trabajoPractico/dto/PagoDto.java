package com.BaseDeDatos.trabajoPractico.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoDto {
    public Long id;
    public Long facturaId;
    public Long usuarioId;
    public String usuarioEmail;
    public LocalDateTime fechaPago;
    public BigDecimal montoPagado;
    public String metodoPago;
}

