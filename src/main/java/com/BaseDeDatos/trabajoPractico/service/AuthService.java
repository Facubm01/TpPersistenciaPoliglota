package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.model.mysql.Rol;
import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import com.BaseDeDatos.trabajoPractico.repository.mysql.SesionRepository;
import com.BaseDeDatos.trabajoPractico.repository.mysql.UsuarioRepository;
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
     * @return La entidad Sesion creada si el login es exitoso, con exitoso=true.
     * @throws IllegalArgumentException si las credenciales son incorrectas o el usuario no tiene roles.
     */
    public Sesion login(String email, String password) {
        // Validación de parámetros de entrada
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }

        Usuario usuario = usuarioRepository.findByEmail(email);

        // --- AVISO DE SEGURIDAD IMPORTANTE ---
        // TODO: ¡Esto es inseguro! Estás comparando contraseñas en texto plano.
        // En una aplicación real, DEBES usar Spring Security con un PasswordEncoder (ej. BCrypt).
        // El password guardado en la BD debería estar hasheado.
        // La comparación correcta sería: passwordEncoder.matches(password, usuario.getPassword())
        // ----------------------------------------
        if (usuario == null) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // Verifica que el usuario tenga al menos un rol asignado
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene roles asignados");
        }

        // Toma el primer rol del Set de roles del usuario
        Rol rolUsuario = usuario.getRoles().iterator().next();

        // Crea la nueva sesión
        Sesion nuevaSesion = new Sesion();
        nuevaSesion.setUsuario(usuario);
        nuevaSesion.setRol(rolUsuario);
        nuevaSesion.setFechaHoraInicio(LocalDateTime.now());
        nuevaSesion.setEstado("ACTIVA");
        nuevaSesion.setExitoso(true); // Marca la sesión como exitosa

        return sesionRepository.save(nuevaSesion);
    }

    /**
     * Cierra la sesión de un usuario.
     * @param sesionId El ID de la sesión a cerrar.
     * @throws IllegalArgumentException si el ID es nulo o no se encuentra la sesión.
     */
    public void logout(Long sesionId) {
        if (sesionId == null) {
            throw new IllegalArgumentException("El ID de sesión no puede ser nulo");
        }

        Optional<Sesion> sesionOpt = sesionRepository.findById(sesionId);

        if (sesionOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la sesión con ID: " + sesionId);
        }

        Sesion sesion = sesionOpt.get();
        sesion.setEstado("INACTIVA");
        sesion.setFechaHoraCierre(LocalDateTime.now());
        sesionRepository.save(sesion);
    }
}