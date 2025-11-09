package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.model.mongo.Grupo;
import com.BaseDeDatos.trabajoPractico.repository.mongo.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public Grupo crear(String nombreGrupo, Long creadorId) {
        if (grupoRepository.findByNombre(nombreGrupo).isPresent()) {
            throw new IllegalArgumentException("Ya existe un grupo con el nombre: " + nombreGrupo);
        }

        Grupo nuevoGrupo = new Grupo();
        nuevoGrupo.setNombre(nombreGrupo);
        nuevoGrupo.setCreadorUsuarioId(creadorId);
        // El creador es el primer miembro
        nuevoGrupo.setMiembrosUsuarioId(new ArrayList<>(List.of(creadorId)));

        return grupoRepository.save(nuevoGrupo);
    }

    public List<Grupo> listarTodos() {
        return grupoRepository.findAll();
    }

    public Grupo agregarMiembro(String nombreGrupo, Long usuarioId) {
        Grupo grupo = grupoRepository.findByNombre(nombreGrupo)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado: " + nombreGrupo));

        if (!grupo.getMiembrosUsuarioId().contains(usuarioId)) {
            grupo.getMiembrosUsuarioId().add(usuarioId);
            return grupoRepository.save(grupo);
        }
        return grupo;
    }
}