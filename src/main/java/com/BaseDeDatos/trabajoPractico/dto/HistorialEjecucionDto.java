package com.BaseDeDatos.trabajoPractico.dto;

import java.time.LocalDateTime;

public class HistorialEjecucionDto {

    private Long id;
    private Long solicitudId;
    private LocalDateTime fechaEjecucion;
    private String resultado;
    private String estado;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSolicitudId() {
        return solicitudId;
    }
    public void setSolicitudId(Long solicitudId) {
        this.solicitudId = solicitudId;
    }
    public LocalDateTime getFechaEjecucion() {
        return fechaEjecucion;
    }
    public void setFechaEjecucion(LocalDateTime fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }
    public String getResultado() {
        return resultado;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}