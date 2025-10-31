package com.BaseDeDatos.trabajoPractico.model.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.Instant;

@Data
@PrimaryKeyClass
public class MedicionKey implements Serializable {

    @PrimaryKeyColumn(name = "sensor_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String sensorId;

    @PrimaryKeyColumn(name = "fecha_hora", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private Instant fechaHora;
}