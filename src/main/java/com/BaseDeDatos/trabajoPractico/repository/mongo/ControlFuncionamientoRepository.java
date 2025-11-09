package com.BaseDeDatos.trabajoPractico.repository.mongo;

import com.BaseDeDatos.trabajoPractico.model.mongo.ControlFuncionamiento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControlFuncionamientoRepository extends MongoRepository<ControlFuncionamiento, String> {
    // Buscar controles por sensor
    List<ControlFuncionamiento> findBySensorId(String sensorId);
    
    // Buscar controles por estado del sensor
    List<ControlFuncionamiento> findByEstadoSensor(String estadoSensor);
    
    // Buscar controles por sensor y estado
    List<ControlFuncionamiento> findBySensorIdAndEstadoSensor(String sensorId, String estadoSensor);
}

