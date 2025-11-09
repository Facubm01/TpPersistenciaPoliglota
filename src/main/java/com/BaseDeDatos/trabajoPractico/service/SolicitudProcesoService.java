package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.SolicitudDeProcesoDto;
import com.BaseDeDatos.trabajoPractico.dto.SolicitudDeProcesoRequest;
import com.BaseDeDatos.trabajoPractico.model.mysql.Proceso;
import com.BaseDeDatos.trabajoPractico.model.mysql.SolicitudDeProceso;
import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import com.BaseDeDatos.trabajoPractico.repository.mysql.ProcesoRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.SolicitudDeProcesoRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudProcesoService {

    private final SolicitudDeProcesoRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcesoRepository procesoRepository;

    public SolicitudProcesoService(SolicitudDeProcesoRepository solicitudRepository,
                                   UsuarioRepository usuarioRepository,
                                   ProcesoRepository procesoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.procesoRepository = procesoRepository;
    }

    private SolicitudDeProcesoDto toDto(SolicitudDeProceso s) {
    SolicitudDeProcesoDto dto = new SolicitudDeProcesoDto();
    dto.id = s.getId();
    dto.usuarioId = s.getUsuario().getId();
    dto.usuarioEmail = s.getUsuario().getEmail();
    dto.procesoId = s.getProceso().getId();
    dto.procesoNombre = s.getProceso().getNombre();
    dto.facturaId = s.getFactura() != null ? s.getFactura().getId() : null;
    dto.fechaSolicitud = s.getFechaSolicitud();
    dto.estado = s.getEstado();
    return dto;
    }

    public List<SolicitudDeProcesoDto> listar() {
    // ESTA LÍNEA DEBE FUNCIONAR SI toDto es un método de instancia
        return solicitudRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public SolicitudDeProcesoDto obtener(Long id) {
        return solicitudRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    public SolicitudDeProcesoDto crear(SolicitudDeProcesoRequest req) {
        // 1. Validar la existencia de Usuario y Proceso
        Usuario usuario = usuarioRepository.findById(req.usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + req.usuarioId));
        
        Proceso proceso = procesoRepository.findById(req.procesoId)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado con ID: " + req.procesoId));

        // 2. Crear la Solicitud
        SolicitudDeProceso s = new SolicitudDeProceso();
        s.setUsuario(usuario);
        s.setProceso(proceso);
        s.setFechaSolicitud(LocalDateTime.now());
        s.setEstado("PENDIENTE"); // Estado por defecto

        return toDto(solicitudRepository.save(s));
    }

    @Transactional
    public SolicitudDeProcesoDto actualizarEstado(Long id, String nuevoEstado) {
        SolicitudDeProceso s = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));

        // Validación simple del nuevo estado
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío.");
        }
        
        // No se puede modificar si ya está facturado (opcional)
        // if (s.getFactura() != null) { throw new IllegalStateException("No se puede modificar una solicitud ya facturada."); }
        
        s.setEstado(nuevoEstado);
        
        return toDto(solicitudRepository.save(s));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!solicitudRepository.existsById(id)) {
             throw new IllegalArgumentException("Solicitud no encontrada con ID: " + id);
        }
        solicitudRepository.deleteById(id);
    }
}