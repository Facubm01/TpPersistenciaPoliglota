package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    // Aquí puedes agregar métodos de consulta personalizados si los necesitas, por ejemplo:
    // Optional<Sesion> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}