package com.BaseDeDatos.trabajoPractico.dto;

import java.time.LocalDate;
import java.util.List;

public class FacturaDto {
    public Long id;
    public Long usuarioId;
    public String usuarioEmail;
    public LocalDate fechaEmision;
    public String estado;
    
    // Lista de IDs de las solicitudes de proceso incluidas en esta factura
    public List<Long> procesosFacturadosIds; 
}