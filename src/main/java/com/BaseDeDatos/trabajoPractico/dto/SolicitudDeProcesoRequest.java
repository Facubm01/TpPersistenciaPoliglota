package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.NotNull;

public class SolicitudDeProcesoRequest {
    @NotNull
    public Long usuarioId;

    @NotNull
    public Long procesoId;

    // Opcional para la creación, pero útil para endpoints de actualización
    public String estado; 
}