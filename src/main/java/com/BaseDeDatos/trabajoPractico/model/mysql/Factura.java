package com.BaseDeDatos.trabajoPractico.model.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private String estado; // "pendiente", "pagada", "vencida"

    // Una factura contiene una o más solicitudes de proceso.
    @OneToMany(mappedBy = "factura")
    private List<SolicitudDeProceso> procesosFacturados;
}