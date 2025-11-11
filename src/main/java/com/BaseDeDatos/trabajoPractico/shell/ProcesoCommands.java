package com.BaseDeDatos.trabajoPractico.shell;

// Importaciones necesarias
import com.BaseDeDatos.trabajoPractico.dto.ProcesoDto;
import com.BaseDeDatos.trabajoPractico.dto.ProcesoRequest;
import com.BaseDeDatos.trabajoPractico.dto.SolicitudDeProcesoDto;
import com.BaseDeDatos.trabajoPractico.dto.SolicitudDeProcesoRequest;
import com.BaseDeDatos.trabajoPractico.service.ProcesoService; // Importamos ProcesoService
import com.BaseDeDatos.trabajoPractico.service.SolicitudProcesoService;
import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.math.BigDecimal; // Necesario para el costo

@ShellComponent
public class ProcesoCommands {

    private final SolicitudProcesoService solicitudProcesoService;
    private final ProcesoService procesoService; // Añadimos el servicio de Proceso
    private final AuthCommands authCommands;

    // Actualizamos el constructor para inyectar ambos servicios
    public ProcesoCommands(SolicitudProcesoService solicitudProcesoService,
                           ProcesoService procesoService, // Añadido
                           AuthCommands authCommands) {
        this.solicitudProcesoService = solicitudProcesoService;
        this.procesoService = procesoService; // Añadido
        this.authCommands = authCommands;
    }

    /**
     * Comando para CREAR un nuevo proceso en el catálogo de servicios.
     * Cumple el requisito TPO: "Gestión de nuevos procesos".
     */
    @ShellMethod(value = "Crea un nuevo proceso (servicio) en el catálogo", key = "crear-proceso")
    public String crearProceso(
            @ShellOption(help = "Nombre del proceso. Ej: 'Informe Max-Min Mensual'") String nombre,
            @ShellOption(help = "Descripción del proceso") String descripcion,
            @ShellOption(help = "Tipo de proceso. Ej: 'REPORTE'") String tipoProceso,
            @ShellOption(help = "Costo del proceso. Ej: 50.00") BigDecimal costo
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión primero. (Solo admins pueden crear procesos)";
        }
        // (Aquí podrías agregar validación de Rol "administrador")

        try {
            // Usamos el DTO ProcesoRequest que ya existe
            ProcesoRequest request = new ProcesoRequest();
            request.nombre = nombre;
            request.descripcion = descripcion;
            request.tipoProceso = tipoProceso;
            request.costo = costo;

            // Llamamos al ProcesoService que ya existe
            ProcesoDto dto = procesoService.crear(request);

            return String.format("✅ Proceso creado con ID: %d. Nombre: '%s', Costo: %.2f",
                    dto.id, dto.nombre, dto.costo);

        } catch (Exception e) {
            return "Error al crear el proceso: " + e.getMessage();
        }
    }


    /**
     * Comando para SOLICITAR un proceso existente.
     */
    @ShellMethod(value = "Solicita la ejecución de un proceso por su ID", key = "solicitar-proceso")
    public String solicitarProceso(
            @ShellOption(help = "ID del proceso a solicitar (creado con 'crear-proceso')") Long procesoId
    ) {
        Sesion sesionActiva = authCommands.getSesionActiva();
        if (sesionActiva == null) {
            return "Error: Debes iniciar sesión primero. Usa: login <email> <password>";
        }

        try {
            Long usuarioId = sesionActiva.getUsuario().getId();

            SolicitudDeProcesoRequest request = new SolicitudDeProcesoRequest();
            request.usuarioId = usuarioId;
            request.procesoId = procesoId;

            SolicitudDeProcesoDto dto = solicitudProcesoService.crear(request);

            return String.format("✅ Solicitud creada con ID: %d. Proceso: '%s'. Estado: %s",
                    dto.id, dto.procesoNombre, dto.estado);

        } catch (Exception e) {
            return "Error al crear la solicitud: " + e.getMessage();
        }
    }
}