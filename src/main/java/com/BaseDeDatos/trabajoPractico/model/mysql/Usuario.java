package com.BaseDeDatos.trabajoPractico.model.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto; 

    @Column(nullable = false, unique = true)
    private String email; 

    @Column(nullable = false)
    private String password; 

    @Column(nullable = false, length = 20)
    private String estado; 

    @Column(nullable = false)
    private LocalDateTime fechaRegistro; 

    // --- Relación Muchos a Muchos con Rol ---
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "usuarios_roles", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"), // FK a esta entidad (Usuario)
            inverseJoinColumns = @JoinColumn(name = "rol_id") // FK a la otra entidad (Rol)
    )
    private Set<Rol> roles = new HashSet<>();
}