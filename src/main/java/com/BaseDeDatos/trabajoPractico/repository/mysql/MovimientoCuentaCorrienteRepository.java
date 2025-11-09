package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.MovimientoCuentaCorriente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoCuentaCorrienteRepository extends JpaRepository<MovimientoCuentaCorriente, Long> {
    List<MovimientoCuentaCorriente> findByCuentaCorrienteIdOrderByFechaDesc(Long cuentaCorrienteId);
}

