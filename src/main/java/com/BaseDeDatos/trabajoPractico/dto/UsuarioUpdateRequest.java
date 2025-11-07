package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioUpdateRequest {
    @NotBlank
    @Size(max = 100)
    public String nombreCompleto;

    @NotBlank @Email
    @Size(max = 120)
    public String email;

    @Size(min = 6, max = 64)
    public String password; // opcional actualizar

    @NotBlank
    @Size(max = 20)
    public String estado;
}


