package com.BaseDeDatos.trabajoPractico.model.mysql;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   // Descripciones como "usuario", "técnico", "administrador" [cite: 48]
    @Column(nullable = false, unique = true, length = 50)
    private String descripcion;
}