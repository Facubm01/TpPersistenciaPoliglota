package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.SolicitudDeProceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudDeProcesoRepository extends JpaRepository<SolicitudDeProceso, Long> {
    // Aquí puedes agregar métodos como:
}