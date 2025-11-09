package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.ProcesoDto;
import com.BaseDeDatos.trabajoPractico.dto.ProcesoRequest;
import com.BaseDeDatos.trabajoPractico.model.mysql.Proceso;
import com.BaseDeDatos.trabajoPractico.repository.mysql.ProcesoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcesoService {

    private final ProcesoRepository procesoRepository;

    public ProcesoService(ProcesoRepository procesoRepository) {
        this.procesoRepository = procesoRepository;
    }

    private ProcesoDto toDto(Proceso p) {
        ProcesoDto dto = new ProcesoDto();
        dto.id = p.getId();
        dto.nombre = p.getNombre();
        dto.descripcion = p.getDescripcion();
        dto.tipoProceso = p.getTipoProceso();
        dto.costo = p.getCosto();
        return dto;
    }

    public List<ProcesoDto> listar() {
        return procesoRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProcesoDto obtener(Long id) {
        return procesoRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    public ProcesoDto crear(ProcesoRequest req) {
        Proceso p = new Proceso();
        p.setNombre(req.nombre);
        p.setDescripcion(req.descripcion);
        p.setTipoProceso(req.tipoProceso);
        p.setCosto(req.costo);
        return toDto(procesoRepository.save(p));
    }

    @Transactional
    public ProcesoDto actualizar(Long id, ProcesoRequest req) {
        Proceso p = procesoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado con ID: " + id));
        
        p.setNombre(req.nombre);
        p.setDescripcion(req.descripcion);
        p.setTipoProceso(req.tipoProceso);
        p.setCosto(req.costo);
        
        return toDto(procesoRepository.save(p));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!procesoRepository.existsById(id)) {
             throw new IllegalArgumentException("Proceso no encontrado con ID: " + id);
        }
        // Nota: En un sistema real, antes de eliminar se debe verificar que no tenga 
        // solicitudes asociadas o eliminar las solicitudes en cascada.
        procesoRepository.deleteById(id);
    }
}