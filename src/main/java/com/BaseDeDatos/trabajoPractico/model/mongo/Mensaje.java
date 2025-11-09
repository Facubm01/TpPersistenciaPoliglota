package com.BaseDeDatos.trabajoPractico.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Representa un mensaje intercambiado (privado o grupal) (MongoDB).
 * Solo uno de los campos de destino (destinatarioUsuarioId o destinatarioGrupoId) debe estar seteado.
 */
@Data
@Document(collection = "mensajes")
public class Mensaje {

    @Id
    private String id;

    // ID del usuario de MySQL que envía el mensaje
    private Long remitenteUsuarioId;

    // Destino (solo para mensajes privados)
    private Long destinatarioUsuarioId;

    // Destino (solo para mensajes grupales)
    private String destinatarioGrupoId; // Este es el ID de MongoDB del Grupo

    private LocalDateTime fechaHora;

    private String contenido;

    // Tipo (Por simplicidad, lo determinamos por el destinatario, 
    // pero guardarlo puede ser útil para consultas)
    private String tipo; // "PRIVADO" o "GRUPAL"
}