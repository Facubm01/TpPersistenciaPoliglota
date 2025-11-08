package com.BaseDeDatos.trabajoPractico.service; // Corregido (sin .mysql)

import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import com.BaseDeDatos.trabajoPractico.repository.mysql.SesionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SesionService {

    private final SesionRepository sesionRepository;

    // Inyección por constructor (sin @Autowired, como hablamos)
    public SesionService(SesionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    /**
     * Devuelve una lista de todas las sesiones que están actualmente "ACTIVA".
     * @return Lista de entidades Sesion.
     */
    public List<Sesion> getSesionesActivas() {
        // Trae todas las sesiones
        List<Sesion> todas = sesionRepository.findAll();
        
        // Filtra la lista y devuelve solo las activas
        return todas.stream()
                .filter(sesion -> "ACTIVA".equals(sesion.getEstado()))
                .collect(Collectors.toList());
    }

    /**
     * Devuelve un historial completo de todas las sesiones (activas e inactivas).
     * @return Lista de todas las entidades Sesion.
     */
    public List<Sesion> getTodasLasSesiones() {
        return sesionRepository.findAll();
    }

    // Aquí se podrían agregar más métodos, como:
    // - "cerrarSesionesExpiradas()"
    // - "getHistorialDeSesionesPorUsuario(Long usuarioId)"
}