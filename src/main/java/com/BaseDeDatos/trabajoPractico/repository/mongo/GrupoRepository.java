package com.BaseDeDatos.trabajoPractico.repository.mongo;

import com.BaseDeDatos.trabajoPractico.model.mongo.Grupo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface GrupoRepository extends MongoRepository<Grupo, String> {
    
    // Método para buscar un grupo por su nombre (útil para comandos shell)
    Optional<Grupo> findByNombre(String nombre);

    // Método para encontrar grupos donde un usuario es miembro (solo para listado inicial)
    // Nota: Esto puede ser ineficiente para muchos grupos, pero funciona para un MVP
    List<Grupo> findByMiembrosUsuarioIdContaining(Long usuarioId);
}