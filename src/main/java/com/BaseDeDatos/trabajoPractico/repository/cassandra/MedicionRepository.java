package com.BaseDeDatos.trabajoPractico.repository.cassandra;

import com.BaseDeDatos.trabajoPractico.model.cassandra.Medicion;
import com.BaseDeDatos.trabajoPractico.model.cassandra.MedicionKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicionRepository extends CassandraRepository<Medicion, MedicionKey> {
    List<Medicion> findByKeySensorIdAndKeyFechaHoraBetween(String sensorId, Instant start, Instant end);
    List<Medicion> findByKeySensorIdInAndKeyFechaHoraBetween(List<String> sensorIds, Instant start, Instant end);
}
    