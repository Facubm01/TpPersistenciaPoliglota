package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class PagoRequest {
    @NotNull(message = "El ID de factura es requerido")
    public Long facturaId;
    
    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    public BigDecimal montoPagado;
    
    @NotNull(message = "El método de pago es requerido")
    public String metodoPago;
}

