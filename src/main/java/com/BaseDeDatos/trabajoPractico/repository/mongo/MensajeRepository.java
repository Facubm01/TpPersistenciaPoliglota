package com.BaseDeDatos.trabajoPractico.repository.mongo;

import com.BaseDeDatos.trabajoPractico.model.mongo.Mensaje;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends MongoRepository<Mensaje, String> {

    /**
     * Busca mensajes privados entre dos usuarios específicos, ordenados por fecha.
     * El 'remitente' puede ser A o B, y el 'destinatario' puede ser B o A.
     */
    @Query("{ $or: [ " +
            "{ 'remitenteUsuarioId': ?0, 'destinatarioUsuarioId': ?1 }, " +
            "{ 'remitenteUsuarioId': ?1, 'destinatarioUsuarioId': ?0 } " +
            "] }")
    List<Mensaje> findMensajesPrivadosEntreUsuarios(Long usuarioIdA, Long usuarioIdB);

    /**
     * Busca todos los mensajes enviados a un grupo específico.
     */
    List<Mensaje> findByDestinatarioGrupoIdOrderByFechaHoraAsc(String grupoId);
}