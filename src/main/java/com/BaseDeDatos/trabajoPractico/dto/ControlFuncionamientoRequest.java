package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class ControlFuncionamientoRequest {
    @NotBlank(message = "El ID del sensor es requerido")
    public String sensorId;

    @NotNull(message = "La fecha de revisión es requerida")
    public Date fechaRevision;

    @NotBlank(message = "El estado del sensor es requerido")
    @Size(max = 50)
    public String estadoSensor; // "activo", "inactivo", "falla", "mantenimiento", etc.

    public String observaciones; // Opcional
}

