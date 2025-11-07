package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RolRequest {
    @NotBlank
    @Size(max = 50)
    public String descripcion;
}


