package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.dto.FacturaDto;
import com.BaseDeDatos.trabajoPractico.dto.FacturaRequest;
import com.BaseDeDatos.trabajoPractico.dto.SolicitudDeProcesoDto;
import com.BaseDeDatos.trabajoPractico.service.FacturaService;
import com.BaseDeDatos.trabajoPractico.service.SolicitudProcesoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class FacturaCommands {

    private final FacturaService facturaService;
    private final SolicitudProcesoService solicitudProcesoService;
    private final AuthCommands authCommands;

    public FacturaCommands(FacturaService facturaService, 
                           SolicitudProcesoService solicitudProcesoService, 
                           AuthCommands authCommands) {
        this.facturaService = facturaService;
        this.solicitudProcesoService = solicitudProcesoService;
        this.authCommands = authCommands;
    }

    @ShellMethod(value = "Genera una factura para un usuario, agrupando solicitudes pendientes.", key = "generar-factura")
    public String generarFactura(
            @ShellOption(help = "ID del usuario a facturar") Long usuarioId
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión (como admin) para facturar.";
        }

        try {
            // 1. Encontrar todas las solicitudes de este usuario que no tengan factura
            // Tu 'reporte-maxmin' actualiza el historial, pero no la solicitud.
            // Así que buscaremos solicitudes que aún no tengan facturaId.
            List<Long> solicitudIds = solicitudProcesoService.listar().stream()
                    .filter(s -> s.usuarioId.equals(usuarioId) && 
                                 s.facturaId == null) // La clave es que no esté facturada
                    .map(s -> s.id)
                    .collect(Collectors.toList());

            if (solicitudIds.isEmpty()) {
                return "No se encontraron solicitudes sin facturar para el usuario ID: " + usuarioId;
            }

            // 2. Crear el DTO de Request
            FacturaRequest request = new FacturaRequest();
            request.usuarioId = usuarioId;
            request.solicitudesProcesoIds = solicitudIds;

            // 3. Llamar al servicio
            FacturaDto dto = facturaService.generarFactura(request);

            return String.format("✅ Factura generada con ID: %d. Estado: %s. Incluye %d solicitudes.",
                    dto.id, dto.estado, solicitudIds.size());

        } catch (Exception e) {
            return "Error al generar factura: " + e.getMessage();
        }
    }
}