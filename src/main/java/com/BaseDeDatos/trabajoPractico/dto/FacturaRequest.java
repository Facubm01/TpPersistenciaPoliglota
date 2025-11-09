package com.BaseDeDatos.trabajoPractico.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Usado para crear una factura a partir de una lista de solicitudes de proceso pendientes.
 */
public class FacturaRequest {
    @NotNull
    public Long usuarioId;
    
    // IDs de las SolicitudesDeProceso a incluir en esta factura
    @NotNull
    @Size(min = 1, message = "Debe incluir al menos una solicitud de proceso para facturar.")
    public List<Long> solicitudesProcesoIds;
}
