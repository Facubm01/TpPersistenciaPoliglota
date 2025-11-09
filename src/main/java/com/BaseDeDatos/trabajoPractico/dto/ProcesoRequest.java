package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProcesoRequest {
    @NotBlank
    @Size(max = 255)
    public String nombre;

    public String descripcion;

    @NotBlank
    @Size(max = 100)
    public String tipoProceso; // Ej: "REPORTE", "CONSULTA", "MANTENIMIENTO"

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    public BigDecimal costo;
}