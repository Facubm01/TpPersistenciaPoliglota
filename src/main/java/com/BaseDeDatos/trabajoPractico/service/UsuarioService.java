package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.UsuarioCreateRequest;
import com.BaseDeDatos.trabajoPractico.dto.UsuarioDto;
import com.BaseDeDatos.trabajoPractico.dto.UsuarioUpdateRequest;
import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import com.BaseDeDatos.trabajoPractico.repository.mysql.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private UsuarioDto toDto(Usuario u) {
        UsuarioDto dto = new UsuarioDto();
        dto.id = u.getId();
        dto.nombreCompleto = u.getNombreCompleto();
        dto.email = u.getEmail();
        dto.estado = u.getEstado();
        dto.fechaRegistro = u.getFechaRegistro();
        return dto;
    }

    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UsuarioDto obtener(Long id) {
        return usuarioRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    public UsuarioDto crear(UsuarioCreateRequest req) {
        Usuario u = new Usuario();
        u.setNombreCompleto(req.nombreCompleto);
        u.setEmail(req.email);
        u.setPassword(req.password);
        u.setEstado(req.estado);
        u.setFechaRegistro(LocalDateTime.now());
        Usuario guardado = usuarioRepository.save(u);
        return toDto(guardado);
    }

    @Transactional
    public UsuarioDto actualizar(Long id, UsuarioUpdateRequest req) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        u.setNombreCompleto(req.nombreCompleto);
        u.setEmail(req.email);
        if (req.password != null && !req.password.isBlank()) {
            u.setPassword(req.password);
        }
        u.setEstado(req.estado);
        return toDto(usuarioRepository.save(u));
    }

    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}


