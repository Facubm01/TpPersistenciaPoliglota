package com.BaseDeDatos.trabajoPractico.model.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("mediciones")
public class Medicion {

    @PrimaryKey
    private MedicionKey key;

    private Double temperatura;

    private Double humedad;
}