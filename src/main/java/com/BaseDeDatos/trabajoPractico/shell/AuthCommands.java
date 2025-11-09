package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import com.BaseDeDatos.trabajoPractico.service.AuthService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class AuthCommands {

    private final AuthService authService;

    // guardamos la sesión actual en memoria
    private Sesion sesionActiva;

    public AuthCommands(AuthService authService) {
        this.authService = authService;
    }

    @ShellMethod(key = "login", value = "Inicia sesión con email y contraseña")
    public String login(
            @ShellOption(help = "Email del usuario") String email,
            @ShellOption(help = "Contraseña del usuario") String password) {

        // si ya hay una sesión ACTIVA, avisamos
        if (sesionActiva != null && "ACTIVA".equalsIgnoreCase(sesionActiva.getEstado())) {
            Usuario u = sesionActiva.getUsuario();
            return "Ya hay una sesión activa para el usuario: "
                    + (u != null ? u.getEmail() : "(desconocido)")
                    + ". Primero ejecutá 'logout'.";
        }

        // llamamos al servicio de autenticación
        Sesion sesion = authService.login(email, password);

        // usamos los campos transitorios que agregamos en Sesion
        if (!sesion.isExitoso()) {
            return "Error al iniciar sesión: " + sesion.getMensajeError();
        }

        // sesión válida → la guardamos como sesión activa
        this.sesionActiva = sesion;

        Usuario usuario = sesion.getUsuario();
        String nombre = usuario != null ? usuario.getNombreCompleto() : "(sin nombre)";
        String mail = usuario != null ? usuario.getEmail() : "(sin email)";

        return "Sesión iniciada correctamente. Bienvenido "
                + nombre + " (" + mail + ")";
    }

    @ShellMethod(key = "logout", value = "Cierra la sesión actual")
    public String logout() {
        if (sesionActiva == null || !"ACTIVA".equalsIgnoreCase(sesionActiva.getEstado())) {
            return "No hay sesión activa.";
        }

        Usuario u = sesionActiva.getUsuario();
        String email = (u != null ? u.getEmail() : "(desconocido)");

        //acá ya NO hay error: logout(Long) y le pasamos un Long
        authService.logout(sesionActiva.getId());

        // marcamos la sesión como inactiva y la “olvidamos” en memoria
        sesionActiva.setEstado("INACTIVA");
        sesionActiva = null;

        return "Sesión cerrada para el usuario " + email;
    }
    public Sesion getSesionActiva() {
        return sesionActiva;
    }
    @ShellMethod(key = "whoami", value = "Muestra el usuario actualmente logueado")
    public String whoAmI() {
    if (sesionActiva == null || !"ACTIVA".equalsIgnoreCase(sesionActiva.getEstado())) {
        return "No hay ningún usuario logueado.";
    }

    // primero obtengo el usuario
    Usuario u = sesionActiva.getUsuario();

    // después uso u para armar nombre y mail
    String nombre = u != null ? u.getNombreCompleto() : "(sin nombre)";
    String mail   = u != null ? u.getEmail()            : "(sin email)";

    return "Usuario logueado: " + nombre
            + " (" + mail + ")\n"
            + "ID sesión: " + sesionActiva.getId();
}
}
