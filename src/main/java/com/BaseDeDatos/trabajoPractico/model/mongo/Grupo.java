package com.BaseDeDatos.trabajoPractico.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Representa un grupo de usuarios para el sistema de mensajería (MongoDB).
 * Atributos requeridos: ID, Nombre del grupo, Usuarios miembros.
 */
@Data
@Document(collection = "grupos")
public class Grupo {

    @Id
    private String id;

    // El nombre debe ser único
    @Indexed(unique = true)
    private String nombre;

    // Lista de IDs de los usuarios de MySQL que pertenecen al grupo
    private List<Long> miembrosUsuarioId;

    // ID del usuario que creó el grupo
    private Long creadorUsuarioId;
}