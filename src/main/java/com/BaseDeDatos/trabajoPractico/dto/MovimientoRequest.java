package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class MovimientoRequest {
    @NotNull(message = "El ID de usuario es requerido")
    public Long usuarioId;
    
    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    public BigDecimal monto;
    
    @NotNull(message = "El tipo de movimiento es requerido")
    public String tipo; // "ACREDITACION" o "DEBITO"
    
    public String descripcion;
}

