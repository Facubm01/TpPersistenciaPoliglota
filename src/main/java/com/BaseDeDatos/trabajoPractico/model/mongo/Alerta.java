package com.BaseDeDatos.trabajoPractico.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Document(collection = "alertas")
public class Alerta {

    @Id
    private String id;

    private String tipo; // "sensor" o "climática" [cite: 97]

    private String sensorId; // Opcional, solo si el tipo es "sensor" [cite: 98]

    private java.time.LocalDateTime fechaHora; 

    private String descripcion; 

    private String estado; // "activa", "resuelta" [cite: 101]
}