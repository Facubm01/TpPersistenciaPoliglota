package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.model.mongo.Mensaje;
import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import com.BaseDeDatos.trabajoPractico.service.MensajeService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.format.DateTimeFormatter;
import java.util.List;

@ShellComponent
public class MensajeCommands {

    private final MensajeService mensajeService;
    private final AuthCommands authCommands;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public MensajeCommands(MensajeService mensajeService, AuthCommands authCommands) {
        this.mensajeService = mensajeService;
        this.authCommands = authCommands;
    }

    private Long getUsuarioIdActivo() {
        Sesion sesion = authCommands.getSesionActiva();
        if (sesion == null || !"ACTIVA".equalsIgnoreCase(sesion.getEstado()) || sesion.getUsuario() == null) {
            throw new IllegalStateException("Debes iniciar sesión primero para usar comandos de Mensajería.");
        }
        return sesion.getUsuario().getId();
    }
    
    // --- COMANDOS DE ENVÍO ---

    @ShellMethod(value = "Envía un mensaje privado a otro usuario", key = "send-private-msg")
    public String enviarMensajePrivado(
            @ShellOption(help = "ID del usuario MySQL destinatario") Long destinatarioId,
            @ShellOption(help = "Contenido del mensaje") String contenido
    ) {
        try {
            Long remitenteId = getUsuarioIdActivo();
            if (remitenteId.equals(destinatarioId)) {
                return "No puedes enviarte un mensaje privado a ti mismo.";
            }
            
            Mensaje mensaje = mensajeService.enviarMensajePrivado(remitenteId, destinatarioId, contenido);
            return String.format("Mensaje privado enviado a Usuario ID %d. Contenido: '%s'",
                    destinatarioId, mensaje.getContenido());
        } catch (Exception e) {
            return "Error al enviar mensaje: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Envía un mensaje a un grupo existente", key = "send-group-msg")
    public String enviarMensajeGrupal(
            @ShellOption(help = "Nombre del grupo (ej: 'Mantenimiento')") String nombreGrupo,
            @ShellOption(help = "Contenido del mensaje") String contenido
    ) {
        try {
            Long remitenteId = getUsuarioIdActivo();
            mensajeService.enviarMensajeGrupal(remitenteId, nombreGrupo, contenido);
            return String.format("Mensaje grupal enviado a '%s' por Usuario ID %d.",
                    nombreGrupo, remitenteId);
        } catch (Exception e) {
            return "Error al enviar mensaje grupal: " + e.getMessage();
        }
    }
    
    // --- COMANDOS DE LECTURA ---

    @ShellMethod(value = "Muestra el historial de mensajes privados con otro usuario", key = "view-private-history")
    public String verHistorialPrivado(
            @ShellOption(help = "ID del usuario MySQL cuyo historial quieres ver") Long otroUsuarioId
    ) {
        try {
            Long usuarioActivoId = getUsuarioIdActivo();
            List<Mensaje> historial = mensajeService.verHistorialPrivado(usuarioActivoId, otroUsuarioId);
            
            return formatHistorial(historial, usuarioActivoId, "Historial privado con Usuario ID: " + otroUsuarioId);
        } catch (Exception e) {
            return "Error al ver historial: " + e.getMessage();
        }
    }
    
    @ShellMethod(value = "Muestra el historial de mensajes de un grupo (requiere el ID de MongoDB del grupo)", key = "view-group-history")
    public String verHistorialGrupal(
            @ShellOption(help = "ID de MongoDB del grupo (obtenido con list-grupos)") String grupoId
    ) {
        try {
            Long usuarioActivoId = getUsuarioIdActivo();
            // Lógica para verificar si es miembro (omitida por simplicidad, pero debería ir aquí)
            
            List<Mensaje> historial = mensajeService.verHistorialGrupal(grupoId);
            
            return formatHistorial(historial, usuarioActivoId, "Historial del Grupo ID: " + grupoId);
        } catch (Exception e) {
            return "Error al ver historial: " + e.getMessage();
        }
    }
    
    private String formatHistorial(List<Mensaje> historial, Long usuarioActivoId, String titulo) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- ").append(titulo).append(" ---\n");
        if (historial.isEmpty()) {
            sb.append("No hay mensajes en este historial.");
            return sb.toString();
        }
        
        for (Mensaje m : historial) {
            String remitente = m.getRemitenteUsuarioId().equals(usuarioActivoId) ? "Yo" : "Usuario " + m.getRemitenteUsuarioId();
            sb.append(String.format("[%s] %s: %s\n", 
                    m.getFechaHora().format(formatter), 
                    remitente, 
                    m.getContenido()));
        }
        return sb.toString();
    }
}