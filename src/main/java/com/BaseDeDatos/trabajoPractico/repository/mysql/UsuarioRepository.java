package com.BaseDeDatos.trabajoPractico.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Ejemplo de método personalizado
    
    Usuario findByEmail(String email);
}