package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCreateRequest {
    @NotBlank
    @Size(max = 100)
    public String nombreCompleto;

    @NotBlank @Email
    @Size(max = 120)
    public String email;

    @NotBlank @Size(min = 6, max = 64)
    public String password;

    @NotBlank
    @Size(max = 20)
    public String estado;
}


