package com.BaseDeDatos.trabajoPractico.repository.mongo;

import com.BaseDeDatos.trabajoPractico.model.mongo.Alerta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends MongoRepository<Alerta, String> {
    // Por ejemplo, buscar todas las alertas activas de un tipo específico:
    List<Alerta> findByTipoAndEstado(String tipo, String estado);
}
