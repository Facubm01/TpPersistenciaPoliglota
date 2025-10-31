package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Puedes agregar métodos de búsqueda personalizados aquí, por ejemplo:
    Rol findByDescripcion(String descripcion);
}