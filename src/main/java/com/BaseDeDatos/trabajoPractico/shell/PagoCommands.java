package com.BaseDeDatos.trabajoPractico.shell;

import com.BaseDeDatos.trabajoPractico.dto.PagoDto;
import com.BaseDeDatos.trabajoPractico.dto.PagoRequest;
import com.BaseDeDatos.trabajoPractico.service.PagoService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.math.BigDecimal;

@ShellComponent
public class PagoCommands {

    private final PagoService pagoService;
    private final AuthCommands authCommands;

    public PagoCommands(PagoService pagoService, AuthCommands authCommands) {
        this.pagoService = pagoService;
        this.authCommands = authCommands;
    }

    @ShellMethod(value = "Registra el pago de una factura existente.", key = "registrar-pago")
    public String registrarPago(
            @ShellOption(help = "ID de la factura a pagar") Long facturaId,
            @ShellOption(help = "Monto del pago") BigDecimal monto,
            @ShellOption(help = "Método de pago. Ej: 'Transferencia'") String metodoPago
    ) {
        if (authCommands.getSesionActiva() == null) {
            return "Error: Debes iniciar sesión (como admin) para registrar pagos.";
        }

        try {
            // 1. Preparar el DTO de Request
            PagoRequest request = new PagoRequest();
            request.facturaId = facturaId;
            request.montoPagado = monto;
            request.metodoPago = metodoPago;

            // 2. Llamar al servicio
            PagoDto dto = pagoService.registrarPago(request);

            return String.format("✅ Pago registrado con ID: %d para la Factura ID: %d. Monto: %.2f",
                    dto.id, dto.facturaId, dto.montoPagado);

        } catch (Exception e) {
            return "Error al registrar el pago: " + e.getMessage();
        }
    }
}