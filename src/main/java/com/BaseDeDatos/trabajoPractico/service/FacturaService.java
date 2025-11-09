package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.FacturaDto;
import com.BaseDeDatos.trabajoPractico.dto.FacturaRequest;
import com.BaseDeDatos.trabajoPractico.model.mysql.Factura;
import com.BaseDeDatos.trabajoPractico.model.mysql.SolicitudDeProceso;
import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import com.BaseDeDatos.trabajoPractico.repository.mysql.FacturaRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.SolicitudDeProcesoRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudDeProcesoRepository solicitudRepository;

    public FacturaService(FacturaRepository facturaRepository,
                          UsuarioRepository usuarioRepository,
                          SolicitudDeProcesoRepository solicitudRepository) {
        this.facturaRepository = facturaRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudRepository = solicitudRepository;
    }

    private FacturaDto toDto(Factura f) {
        FacturaDto dto = new FacturaDto();
        dto.id = f.getId();
        dto.usuarioId = f.getUsuario().getId();
        dto.usuarioEmail = f.getUsuario().getEmail();
        dto.fechaEmision = f.getFechaEmision();
        dto.estado = f.getEstado();
        // Mapea las solicitudes a solo sus IDs
        dto.procesosFacturadosIds = f.getProcesosFacturados().stream()
                .map(SolicitudDeProceso::getId)
                .collect(Collectors.toList());
        return dto;
    }

    public List<FacturaDto> listar() {
        // Al traer todas, Spring cargará las colecciones 'procesosFacturados' por defecto (Lazy/Eager)
        return facturaRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public FacturaDto obtener(Long id) {
        return facturaRepository.findById(id).map(this::toDto).orElse(null);
    }
    
    @Transactional
    public FacturaDto generarFactura(FacturaRequest req) {
        // 1. Validar Usuario
        Usuario usuario = usuarioRepository.findById(req.usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + req.usuarioId));

        // 2. Obtener y validar Solicitudes de Proceso
        List<SolicitudDeProceso> solicitudes = solicitudRepository.findAllById(req.solicitudesProcesoIds);
        
        if (solicitudes.isEmpty() || solicitudes.size() != req.solicitudesProcesoIds.size()) {
            throw new IllegalArgumentException("Una o más solicitudes no fueron encontradas.");
        }
        
        // 3. Crear la Factura
        Factura nuevaFactura = new Factura();
        nuevaFactura.setUsuario(usuario);
        nuevaFactura.setFechaEmision(LocalDate.now());
        nuevaFactura.setEstado("PENDIENTE");
        
        // El campo @OneToMany (procesosFacturados) se setea después del save.
        Factura facturaGuardada = facturaRepository.save(nuevaFactura);

        // 4. Actualizar las Solicitudes con el ID de la Factura y cambiar su estado (esto se guarda en cascada)
        for (SolicitudDeProceso s : solicitudes) {
            if (s.getFactura() != null) {
                throw new IllegalStateException("La solicitud ID " + s.getId() + " ya está asociada a la factura ID " + s.getFactura().getId());
            }
            s.setFactura(facturaGuardada);
            s.setEstado("FACTURADO"); // Nuevo estado para la solicitud
        }
        
        // Spring JPA guardará los cambios en las Solicitudes al finalizar la transacción. 
        // Actualizamos la lista en la Factura para que el DTO sea correcto al devolver.
        facturaGuardada.setProcesosFacturados(solicitudes);

        return toDto(facturaGuardada);
    }
    
    @Transactional
    public FacturaDto actualizarEstado(Long id, String nuevoEstado) {
        Factura f = facturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + id));

        f.setEstado(nuevoEstado);
        
        return toDto(facturaRepository.save(f));
    }

    // No se implementa eliminar factura por ser una acción delicada en contabilidad.
}