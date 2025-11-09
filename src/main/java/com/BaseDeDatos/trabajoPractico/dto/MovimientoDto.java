package com.BaseDeDatos.trabajoPractico.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoDto {
    public Long id;
    public Long cuentaCorrienteId;
    public Long usuarioId;
    public String tipo; // "ACREDITACION" o "DEBITO"
    public BigDecimal monto;
    public LocalDateTime fecha;
    public String descripcion;
}

