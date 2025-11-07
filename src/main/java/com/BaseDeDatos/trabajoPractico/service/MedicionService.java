package com.BaseDeDatos.trabajoPractico.service;

import com.BaseDeDatos.trabajoPractico.dto.MedicionDto;
import com.BaseDeDatos.trabajoPractico.model.cassandra.Medicion;
import com.BaseDeDatos.trabajoPractico.model.cassandra.MedicionKey;
import com.BaseDeDatos.trabajoPractico.repository.cassandra.MedicionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicionService {

    private final MedicionRepository medicionRepository;

    public MedicionService(MedicionRepository medicionRepository) {
        this.medicionRepository = medicionRepository;
    }

    private MedicionDto toDto(Medicion m) {
        MedicionDto dto = new MedicionDto();
        dto.sensorId = m.getKey().getSensorId();
        dto.fechaHora = m.getKey().getFechaHora();
        dto.temperatura = m.getTemperatura();
        dto.humedad = m.getHumedad();
        return dto;
    }

    public List<MedicionDto> buscarPorSensorYRango(String sensorId, Instant inicio, Instant fin) {
        return medicionRepository.findByKeySensorIdAndKeyFechaHoraBetween(sensorId, inicio, fin)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public MedicionDto crear(MedicionDto req) {
        Medicion m = new Medicion();
        MedicionKey key = new MedicionKey();
        key.setSensorId(req.sensorId);
        key.setFechaHora(req.fechaHora != null ? req.fechaHora : Instant.now());
        m.setKey(key);
        m.setTemperatura(req.temperatura);
        m.setHumedad(req.humedad);
        return toDto(medicionRepository.save(m));
    }

    public List<MedicionDto> ultimas(String sensorId, int limit) {
        Instant fin = Instant.now();
        Instant inicio = fin.minusSeconds(60L * 60L * 24L * 30L); // ventana 30 días
        return medicionRepository.findByKeySensorIdAndKeyFechaHoraBetween(sensorId, inicio, fin)
                .stream()
                .sorted((a,b) -> b.getKey().getFechaHora().compareTo(a.getKey().getFechaHora()))
                .limit(limit)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MedicionDto> porDia(String sensorId, Instant diaInicioUTC) {
        Instant inicio = diaInicioUTC;
        Instant fin = diaInicioUTC.plusSeconds(60L * 60L * 24L - 1);
        return buscarPorSensorYRango(sensorId, inicio, fin);
    }
}


