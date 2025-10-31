package com.BaseDeDatos.trabajoPractico.repository.mongo;

import com.BaseDeDatos.trabajoPractico.model.mongo.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends MongoRepository<Sensor, String> {
    // Spring Data MongoDB nos permite crear consultas complejas solo con el nombre del método.
    // Por ejemplo, para buscar todos los sensores de un país:
    List<Sensor> findByPais(String pais);
}