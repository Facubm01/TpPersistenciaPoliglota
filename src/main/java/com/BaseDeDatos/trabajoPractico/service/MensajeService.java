package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.model.mongo.Mensaje;
import com.BaseDeDatos.trabajoPractico.model.mongo.Grupo;
import com.BaseDeDatos.trabajoPractico.repository.mongo.MensajeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final GrupoService grupoService; // Necesitamos esto para validar el grupo

    public MensajeService(MensajeRepository mensajeRepository, GrupoService grupoService) {
        this.mensajeRepository = mensajeRepository;
        this.grupoService = grupoService;
    }

    /**
     * Envía un mensaje privado a otro usuario.
     */
    public Mensaje enviarMensajePrivado(Long remitenteId, Long destinatarioId, String contenido) {
        // En una implementación real se validaría que el destinatarioId existe en MySQL
        
        Mensaje mensaje = new Mensaje();
        mensaje.setRemitenteUsuarioId(remitenteId);
        mensaje.setDestinatarioUsuarioId(destinatarioId);
        mensaje.setContenido(contenido);
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setTipo("PRIVADO");

        return mensajeRepository.save(mensaje);
    }

    /**
     * Envía un mensaje a un grupo existente.
     */
    public Mensaje enviarMensajeGrupal(Long remitenteId, String nombreGrupo, String contenido) {
        // 1. Buscar el Grupo por nombre. Usamos el Service de Grupo.
        Grupo grupo = grupoService.listarTodos().stream()
                .filter(g -> g.getNombre().equalsIgnoreCase(nombreGrupo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado: " + nombreGrupo));

        // 2. Opcional: Validar que el remitente es miembro del grupo
        if (!grupo.getMiembrosUsuarioId().contains(remitenteId)) {
            throw new IllegalArgumentException("El usuario no es miembro del grupo: " + nombreGrupo);
        }
        
        Mensaje mensaje = new Mensaje();
        mensaje.setRemitenteUsuarioId(remitenteId);
        mensaje.setDestinatarioGrupoId(grupo.getId()); // Usamos el ID de MongoDB del grupo
        mensaje.setContenido(contenido);
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setTipo("GRUPAL");

        return mensajeRepository.save(mensaje);
    }
    
    /**
     * Recupera el historial de mensajes privados entre el usuario activo y otro usuario.
     */
    public List<Mensaje> verHistorialPrivado(Long usuarioA, Long usuarioB) {
        return mensajeRepository.findMensajesPrivadosEntreUsuarios(usuarioA, usuarioB);
    }
    
    /**
     * Recupera el historial de mensajes de un grupo específico.
     */
    public List<Mensaje> verHistorialGrupal(String grupoId) {
        return mensajeRepository.findByDestinatarioGrupoIdOrderByFechaHoraAsc(grupoId);
    }
}