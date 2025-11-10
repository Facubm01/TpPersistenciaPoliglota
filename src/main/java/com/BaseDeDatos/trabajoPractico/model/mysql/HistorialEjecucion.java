package com.BaseDeDatos.trabajoPractico.model.mysql;

// Asegúrate de que los imports de jakarta.persistence sean correctos.
// Si tu proyecto usa Spring Boot 2, puede que sea javax.persistence
import jakarta.persistence.*; 
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_ejecuciones") // El nombre de la tabla que definimos en el SQL
public class HistorialEjecucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // [cite: 70]

    // Relación con SolicitudProceso [cite: 71]
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id") // El nombre de la foreign key en tu tabla SQL
    private SolicitudDeProceso solicitud;

    @Column(name = "fecha_ejecucion", nullable = false)
    private LocalDateTime fechaEjecucion; // [cite: 72]

    @Column(name = "resultado", length = 1000)
    private String resultado; // [cite: 73]

    @Column(name = "estado", nullable = false)
    private String estado; // [cite: 74]

    // --- Constructores, Getters y Setters ---

    public HistorialEjecucion() {
        // Constructor vacío requerido por JPA
    }

    // --- Getters ---
    public Long getId() { 
        return id; 
    }
    public SolicitudDeProceso getSolicitud() { 
        return solicitud; 
    }
    public LocalDateTime getFechaEjecucion() { 
        return fechaEjecucion; 
    }
    public String getResultado() { 
        return resultado; 
    }
    public String getEstado() { 
        return estado; 
    }

    // --- Setters ---
    public void setId(Long id) { 
        this.id = id; 
    }
    public void setSolicitud(SolicitudDeProceso solicitud) { 
        this.solicitud = solicitud; 
    }
    public void setFechaEjecucion(LocalDateTime fechaEjecucion) { 
        this.fechaEjecucion = fechaEjecucion; 
    }
    public void setResultado(String resultado) { 
        this.resultado = resultado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
}