package com.BaseDeDatos.trabajoPractico.model.mysql;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sesiones")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con el Usuario que inició sesión
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación con el Rol que usó en esa sesión
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_cierre")
    private LocalDateTime fechaHoraCierre; // Puede ser nulo si la sesión está activa

    @Column(nullable = false, length = 20)
    private String estado; // Ej: "ACTIVA", "INACTIVA"

    // --- CAMPOS TRANSITORIOS (no se guardan en la base) ---
    @Transient
    private boolean exitoso; // indica si el login fue exitoso

    @Transient
    private String mensajeError; // guarda mensaje de error si el login falla

    // --- Getters y Setters manuales para estos campos (por claridad) ---

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}
