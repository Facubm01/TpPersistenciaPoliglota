package com.BaseDeDatos.trabajoPractico.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import com.BaseDeDatos.trabajoPractico.repository.mongo.AlertaRepository;
import com.BaseDeDatos.trabajoPractico.model.mongo.Alerta; // Asegúrate de que esta entidad existe
import java.time.LocalDateTime;
import java.util.List;

@ShellComponent
public class AlertaCommands {

    private final AlertaRepository alertaRepository;
    
    // Inyección por Constructor (Spring Shell)
    public AlertaCommands(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    // 1. Comando crear-alerta
    @ShellMethod("Crea una alerta manual para un sensor o evento.")
    public String crearAlerta(String tipo, String descripcion, String sensorId) { 
        try {
            Alerta nuevaAlerta = new Alerta();
            
            
            nuevaAlerta.setDescripcion(descripcion); 
            
            
            nuevaAlerta.setTipo(tipo); 
            nuevaAlerta.setSensorId(sensorId); 
            nuevaAlerta.setFechaHora(LocalDateTime.now());
            
            alertaRepository.save(nuevaAlerta);
            
            return "Alerta de tipo '" + tipo + "' guardada con éxito en MongoDB.";
        } catch (Exception e) {
            return "ERROR al crear alerta: " + e.getMessage();
        }
    }

    // 2. Comando ver-alertas
    @ShellMethod("Muestra todas las alertas activas o registradas.")
    public String verAlertas() {
        // 1. Obtener todas las alertas de MongoDB
        List<Alerta> alertas = alertaRepository.findAll(); 
        
        if (alertas.isEmpty()) {
            return "No hay alertas registradas en MongoDB.";
        }
        
        // 2. Formatear la salida para la consola
        StringBuilder sb = new StringBuilder("\n🚨 LISTA DE ALERTAS (" + alertas.size() + ")\n");
        sb.append("--------------------------------------------------\n");
        
        for (Alerta a : alertas) {
            // Se asume que la entidad Alerta tiene Getters y toString() funcional.
            // NOTA: Para obtener un ID de tipo ObjectId, deberías usar a.getId().toHexString() o similar, 
            // pero para simplificar, usamos getId().
            sb.append("ID: ").append(a.getId())
              .append(" | Sensor: ").append(a.getSensorId())
              .append(" | Tipo: ").append(a.getTipo())
              .append(" | Mensaje: ").append(a.getDescripcion())
              .append("\n");
        }
        sb.append("--------------------------------------------------");
        return sb.toString();
    }
}