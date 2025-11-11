package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.dto.MovimientoDto;
import com.BaseDeDatos.trabajoPractico.service.CuentaCorrienteService;
import com.BaseDeDatos.trabajoPractico.model.mysql.Sesion;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.math.BigDecimal;
import java.util.List;

@ShellComponent
public class CuentaCorrienteCommands {

    private final CuentaCorrienteService cuentaCorrienteService;
    private final AuthCommands authCommands;

    public CuentaCorrienteCommands(CuentaCorrienteService cuentaCorrienteService, AuthCommands authCommands) {
        this.cuentaCorrienteService = cuentaCorrienteService;
        this.authCommands = authCommands;
    }

    @ShellMethod(value = "Consulta el saldo de tu cuenta corriente.", key = "consultar-saldo")
    public String consultarSaldo() {
        Sesion sesionActiva = authCommands.getSesionActiva();
        if (sesionActiva == null) {
            return "Error: Debes iniciar sesión primero.";
        }
        
        try {
            Long usuarioId = sesionActiva.getUsuario().getId();
            BigDecimal saldo = cuentaCorrienteService.consultarSaldo(usuarioId);
            return String.format("Tu saldo actual es: $%.2f", saldo);
        } catch (Exception e) {
            return "Error al consultar saldo: " + e.getMessage();
        }
    }

    @ShellMethod(value = "Muestra el historial de movimientos de tu cuenta corriente.", key = "ver-historial-movimientos")
    public String verHistorialMovimientos() {
        Sesion sesionActiva = authCommands.getSesionActiva();
        if (sesionActiva == null) {
            return "Error: Debes iniciar sesión primero.";
        }

        try {
            Long usuarioId = sesionActiva.getUsuario().getId();
            List<MovimientoDto> historial = cuentaCorrienteService.obtenerHistorial(usuarioId);

            if (historial.isEmpty()) {
                return "No tienes movimientos en tu cuenta.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("--- Historial de Movimientos ---\n");
            for (MovimientoDto m : historial) {
                sb.append(String.format("[%s] %s | $%.2f | %s\n",
                        m.fecha.toLocalDate(), m.tipo, m.monto, m.descripcion));
            }
            return sb.toString();
            
        } catch (Exception e) {
            return "Error al obtener historial: " + e.getMessage();
        }
    }
}