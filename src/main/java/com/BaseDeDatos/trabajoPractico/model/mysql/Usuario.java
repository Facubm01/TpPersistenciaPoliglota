package com.BaseDeDatos.trabajoPractico.model.mysql;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // AÑADIDO: Mapeo explícito a la columna con guion bajo
    @Column(name = "nombre_completo", nullable = false) 
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String estado;

    // AÑADIDO: Mapeo explícito a la columna con guion bajo
    @Column(name = "fecha_registro", nullable = false) 
    private LocalDateTime fechaRegistro;

    // --- Relación Muchos a Muchos con Rol ---
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "usuarios_roles", 
            joinColumns = @JoinColumn(name = "usuario_id"), 
            inverseJoinColumns = @JoinColumn(name = "rol_id") 
    )
    private Set<Rol> roles = new HashSet<>();
}