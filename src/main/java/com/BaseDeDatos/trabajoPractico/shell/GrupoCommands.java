package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.model.mongo.Grupo;
import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import com.BaseDeDatos.trabajoPractico.service.GrupoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class GrupoCommands {

    private final GrupoService grupoService;
    
    // Necesitamos una referencia al estado de la sesión, simularemos una inyección simple
    private final AuthCommands authCommands;

    public GrupoCommands(GrupoService grupoService, AuthCommands authCommands) {
        this.grupoService = grupoService;
        this.authCommands = authCommands;
    }

    private Long getUsuarioIdActivo() {
        Sesion sesion = authCommands.getSesionActiva();
        if (sesion == null || !"ACTIVA".equalsIgnoreCase(sesion.getEstado()) || sesion.getUsuario() == null) {
            throw new IllegalStateException("Debes iniciar sesión primero para usar comandos de Grupo.");
        }
        return sesion.getUsuario().getId();
    }

    @ShellMethod(value = "Crea un nuevo grupo de mensajería", key = "create-grupo")
    public String crearGrupo(
            @ShellOption(help = "Nombre único del nuevo grupo") String nombre
    ) {
        try {
            Long creadorId = getUsuarioIdActivo();
            Grupo creado = grupoService.crear(nombre, creadorId);
            return "Grupo '" + creado.getNombre() + "' creado exitosamente en MongoDB con ID: " + creado.getId();
        } catch (Exception e) {
            return "Error al crear grupo: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Agrega un miembro a un grupo existente", key = "add-miembro")
    public String agregarMiembro(
            @ShellOption(help = "Nombre del grupo") String nombreGrupo,
            @ShellOption(help = "ID del usuario MySQL a agregar") Long usuarioId
    ) {
        try {
            // Se puede agregar lógica de permisos aquí (por ejemplo, solo el creador puede agregar)
            // Long solicitanteId = getUsuarioIdActivo(); 
            
            Grupo actualizado = grupoService.agregarMiembro(nombreGrupo, usuarioId);
            
            List<String> miembrosStr = actualizado.getMiembrosUsuarioId().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
                    
            return "Usuario ID " + usuarioId + " añadido a grupo '" + nombreGrupo + "'. Miembros actuales: " + String.join(", ", miembrosStr);
        } catch (Exception e) {
            return "Error al añadir miembro: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Lista todos los grupos existentes", key = "list-grupos")
    public String listarGrupos() {
        // No requiere login, solo para depuración rápida
        List<Grupo> grupos = grupoService.listarTodos();

        if (grupos.isEmpty()) {
            return "No hay grupos registrados en MongoDB.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- Grupos Registrados (MongoDB) ---\n");
        for (Grupo g : grupos) {
            String miembros = g.getMiembrosUsuarioId().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            sb.append(String.format("ID: %s, Nombre: %s, Creador ID: %d, Miembros: [%s]\n",
                    g.getId(), g.getNombre(), g.getCreadorUsuarioId(), miembros));
        }
        return sb.toString();
    }
}