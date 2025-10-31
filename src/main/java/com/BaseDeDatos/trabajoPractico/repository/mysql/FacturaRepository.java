package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    // Aquí puedes agregar métodos como:
    // List<Factura> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}