package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.dto.UsuarioCreateRequest;
import com.BaseDeDatos.trabajoPractico.dto.UsuarioDto;
import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import com.BaseDeDatos.trabajoPractico.service.UsuarioService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class UsuarioCommands {

    private final UsuarioService usuarioService;
    private final AuthCommands authCommands;

    public UsuarioCommands(UsuarioService usuarioService, AuthCommands authCommands) {
        this.usuarioService = usuarioService;
        this.authCommands = authCommands;
    }

    /**
     * Comando para crear un nuevo usuario.
     * Permite registrar nuevos usuarios en el sistema.
     */
    @ShellMethod(value = "Crea un nuevo usuario en el sistema", key = "crear-usuario")
    public String crearUsuario(
            @ShellOption(help = "Nombre completo del usuario") String nombreCompleto,
            @ShellOption(help = "Email del usuario (debe ser único)") String email,
            @ShellOption(help = "Contraseña del usuario (mínimo 6 caracteres)") String password,
            @ShellOption(help = "Estado del usuario (ej: 'activo' o 'inactivo')", defaultValue = "activo") String estado
    ) {
        // Validar que haya una sesión activa (opcional, pero recomendado para seguridad)
        Sesion sesionActiva = authCommands.getSesionActiva();
        if (sesionActiva == null) {
            return "Error: Debes iniciar sesión primero. Usa: login <email> <password>";
        }

        try {
            // Crear el request
            UsuarioCreateRequest request = new UsuarioCreateRequest();
            request.nombreCompleto = nombreCompleto;
            request.email = email;
            request.password = password;
            request.estado = estado != null && !estado.isBlank() ? estado : "activo";

            // Llamar al servicio
            UsuarioDto usuarioCreado = usuarioService.crear(request);

            return String.format("✅ Usuario creado exitosamente. ID: %d, Email: %s, Nombre: %s",
                    usuarioCreado.id, usuarioCreado.email, usuarioCreado.nombreCompleto);

        } catch (Exception e) {
            return "Error al crear usuario: " + e.getMessage();
        }
    }

    /**
     * Comando para asignar un rol a un usuario.
     * Permite asignar roles como "usuario", "tecnico", "administrador" a usuarios existentes.
     */
    @ShellMethod(value = "Asigna un rol a un usuario", key = "asignar-rol")
    public String asignarRol(
            @ShellOption(help = "ID del usuario") Long usuarioId,
            @ShellOption(help = "Descripción del rol (ej: 'usuario', 'tecnico', 'administrador')") String descripcionRol
    ) {
        // Validar que haya una sesión activa
        Sesion sesionActiva = authCommands.getSesionActiva();
        if (sesionActiva == null) {
            return "Error: Debes iniciar sesión primero. Usa: login <email> <password>";
        }

        try {
            // Llamar al servicio para asignar el rol
            UsuarioDto usuarioActualizado = usuarioService.asignarRol(usuarioId, descripcionRol);

            return String.format("✅ Rol '%s' asignado exitosamente al usuario ID: %d (%s)",
                    descripcionRol, usuarioActualizado.id, usuarioActualizado.email);

        } catch (Exception e) {
            return "Error al asignar rol: " + e.getMessage();
        }
    }
}

