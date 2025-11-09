-- V6__create_historial_movimientos_table.sql

-- Tabla para el historial de movimientos de cuenta corriente
CREATE TABLE movimientos_cuenta_corriente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cuenta_corriente_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- "ACREDITACION" o "DEBITO"
    monto DECIMAL(12, 2) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    descripcion VARCHAR(500),
    FOREIGN KEY (cuenta_corriente_id) REFERENCES cuentas_corrientes(id)
);

-- Índice para mejorar las consultas por cuenta corriente
CREATE INDEX idx_movimientos_cuenta_corriente_id ON movimientos_cuenta_corriente(cuenta_corriente_id);

-- Índice para mejorar las consultas por fecha
CREATE INDEX idx_movimientos_fecha ON movimientos_cuenta_corriente(fecha DESC);

