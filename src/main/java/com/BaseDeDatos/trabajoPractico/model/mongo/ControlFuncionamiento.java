package com.BaseDeDatos.trabajoPractico.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "controles_funcionamiento")
public class ControlFuncionamiento {

    @Id
    private String id; // ID de control

    private String sensorId; // Referencia al sensor (ID del sensor en MongoDB)

    private Date fechaRevision; // Fecha de revisión

    private String estadoSensor; // Estado del sensor: "activo", "inactivo", "falla", "mantenimiento", etc.

    private String observaciones; // Observaciones del control
}

