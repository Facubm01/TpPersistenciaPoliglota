package com.BaseDeDatos.trabajoPractico.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "sensores") // Le dice a Spring que esto es un documento en la colección "sensores"
public class Sensor {

    @Id
    private String id; // En MongoDB, los IDs son strings por defecto

    private String nombreCodigo; 

    private String tipoSensor; // "temperatura", "humedad" [cite: 19]

    // Campo especial para coordenadas geográficas. ¡La clave de MongoDB!
    private GeoJsonPoint ubicacion; // Almacena [longitud, latitud] [cite: 20, 21]

    private String ciudad;

    private String pais; 

    private String estadoSensor; // "activo", "inactivo", "falla" [cite: 24]

    private Date fechaInicioEmision; 

    // Podemos agregar campos flexibles aquí sin cambiar la estructura de otros sensores.
    // Por ejemplo: private String versionFirmware;
}