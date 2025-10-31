package com.BaseDeDatos.trabajoPractico.repository.cassandra;

import com.BaseDeDatos.trabajoPractico.model.cassandra.Medicion;
import com.BaseDeDatos.trabajoPractico.model.cassandra.MedicionKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicionRepository extends CassandraRepository<Medicion, MedicionKey> {
    // Spring Data Cassandra es lo suficientemente inteligente para crear
    // consultas por rangos sobre la clave de agrupación (clustering key).
    // Por ejemplo, para buscar mediciones de un sensor en un rango de fechas:
    // List<Medicion> findByKeySensorIdAndKeyFechaHoraBetween(String sensorId, Instant start, Instant end);
}