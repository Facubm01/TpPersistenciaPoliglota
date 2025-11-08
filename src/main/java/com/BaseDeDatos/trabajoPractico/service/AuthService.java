package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.model.mysql.Rol;
import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import com.BaseDeDatos.trabajoPractico.repository.mysql.SesionRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SesionRepository sesionRepository;

    
    public AuthService(UsuarioRepository usuarioRepository, SesionRepository sesionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.sesionRepository = sesionRepository;
    }

    /**
     * Intenta autenticar a un usuario y crear un registro de sesión.
     * @param email Email del usuario.
     * @param password Contraseña en texto plano.
     * @return La entidad Sesion creada si el login es exitoso.
     * @throws RuntimeException si las credenciales son incorrectas o el usuario no tiene roles.
     */
    public Sesion login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        // --- AVISO DE SEGURIDAD IMPORTANTE ---
        // TODO: ¡Esto es inseguro! Estás comparando contraseñas en texto plano.
        // En una aplicación real, DEBES usar Spring Security con un PasswordEncoder (ej. BCrypt).
        // El password guardado en la BD debería estar hasheado.
        // La comparación correcta sería: passwordEncoder.matches(password, usuario.getPassword())
        // ----------------------------------------
        if (usuario != null && usuario.getPassword().equals(password)) {

            // Verifica que el usuario tenga al menos un rol asignado
            if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
                throw new RuntimeException("El usuario '" + email + "' no tiene roles asignados.");
            }

            // Toma el primer rol del Set de roles del usuario
            Rol rolUsuario = usuario.getRoles().iterator().next();

            // Crea la nueva sesión
            Sesion nuevaSesion = new Sesion();
            nuevaSesion.setUsuario(usuario);
            nuevaSesion.setRol(rolUsuario); // Asigna el rol a la sesión
            nuevaSesion.setFechaHoraInicio(LocalDateTime.now());
            nuevaSesion.setEstado("ACTIVA");

            return sesionRepository.save(nuevaSesion);
        } else {
            throw new RuntimeException("Credenciales inválidas para el email: " + email);
        }
    }

    /**
     * Cierra la sesión de un usuario.
     * @param sesionId El ID de la sesión a cerrar.
     * @throws RuntimeException si no se encuentra la sesión.
     */
    public void logout(Long sesionId) {
        Optional<Sesion> sesionOpt = sesionRepository.findById(sesionId);

        if (sesionOpt.isPresent()) {
            Sesion sesion = sesionOpt.get();
            sesion.setEstado("INACTIVA");
            sesion.setFechaHoraCierre(LocalDateTime.now());
            sesionRepository.save(sesion);
        } else {
            throw new RuntimeException("No se encontró la sesión con ID: " + sesionId);
        }
    }
}