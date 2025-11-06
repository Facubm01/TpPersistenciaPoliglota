package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.RolDto;
import com.BaseDeDatos.trabajoPractico.dto.RolRequest;
import com.BaseDeDatos.trabajoPractico.model.mysql.Rol;
import com.BaseDeDatos.trabajoPractico.repository.mysql.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    private RolDto toDto(Rol r) {
        RolDto dto = new RolDto();
        dto.id = r.getId();
        dto.descripcion = r.getDescripcion();
        return dto;
    }

    public List<RolDto> listar() {
        return rolRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public RolDto obtener(Long id) {
        return rolRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    public RolDto crear(RolRequest req) {
        Rol r = new Rol();
        r.setDescripcion(req.descripcion);
        return toDto(rolRepository.save(r));
    }

    @Transactional
    public RolDto actualizar(Long id, RolRequest req) {
        Rol r = rolRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        r.setDescripcion(req.descripcion);
        return toDto(rolRepository.save(r));
    }

    @Transactional
    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }
}


