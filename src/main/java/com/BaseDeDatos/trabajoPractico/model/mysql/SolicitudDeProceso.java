package com.BaseDeDatos.trabajoPractico.model.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitudes_de_proceso")
public class SolicitudDeProceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proceso_id", nullable = false)
    private Proceso proceso;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(nullable = false)
    private String estado; // "pendiente", "completado"

    // Este campo se llenará cuando la solicitud se incluya en una factura.
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;
}