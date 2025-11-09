package com.BaseDeDatos.trabajoPractico.dto;

import java.time.LocalDateTime;

public class SolicitudDeProcesoDto {
    public Long id;
    public Long usuarioId;
    public String usuarioEmail; // Para referencia fácil
    public Long procesoId;
    public String procesoNombre; // Para referencia fácil
    public Long facturaId; // Puede ser nulo
    public LocalDateTime fechaSolicitud;
    public String estado;
}