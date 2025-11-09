package com.BaseDeDatos.trabajoPractico.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;

@Configuration
public class MongoIndexesConfig {

    @Bean
    CommandLineRunner initMongoIndexes(MongoTemplate mongoTemplate) {
        return args -> {
            // Índices para sensores: nombreCodigo único, geoespacial 2dsphere en ubicacion
            IndexOperations sensorIdx = mongoTemplate.indexOps("sensores");
            sensorIdx.createIndex(new Index().on("nombreCodigo", Sort.Direction.ASC).unique());
            sensorIdx.createIndex(new GeospatialIndex("ubicacion").typed(GeoSpatialIndexType.GEO_2DSPHERE));

            // Índices para alertas: TTL por fechaHora (por ejemplo 30 días)
            IndexOperations alertasIdx = mongoTemplate.indexOps("alertas");
            alertasIdx.createIndex(new Index().on("fechaHora", Sort.Direction.ASC).expire(60L * 60L * 24L * 30L));
            alertasIdx.createIndex(new Index().on("sensorId", Sort.Direction.ASC));
            alertasIdx.createIndex(new Index().on("tipo", Sort.Direction.ASC));

            // Índices para controles de funcionamiento: sensorId y estadoSensor para búsquedas rápidas
            IndexOperations controlesIdx = mongoTemplate.indexOps("controles_funcionamiento");
            controlesIdx.createIndex(new Index().on("sensorId", Sort.Direction.ASC));
            controlesIdx.createIndex(new Index().on("estadoSensor", Sort.Direction.ASC));
            controlesIdx.createIndex(new Index().on("fechaRevision", Sort.Direction.DESC));
            // Índice compuesto para búsquedas por sensor y estado
            controlesIdx.createIndex(new Index().on("sensorId", Sort.Direction.ASC).on("estadoSensor", Sort.Direction.ASC));
        };
    }
}


