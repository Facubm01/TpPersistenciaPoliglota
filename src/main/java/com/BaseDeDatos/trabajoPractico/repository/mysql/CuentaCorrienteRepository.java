package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.CuentaCorriente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaCorrienteRepository extends JpaRepository<CuentaCorriente, Long> {
    // Aquí puedes agregar métodos como:
}