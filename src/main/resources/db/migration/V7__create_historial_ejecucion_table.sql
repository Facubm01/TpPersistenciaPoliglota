-- V7__create_historial_ejecucion_table.sql

-- Tabla para el historial de ejecución de procesos
CREATE TABLE historial_ejecuciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solicitud_id BIGINT NOT NULL,
    fecha_ejecucion TIMESTAMP NOT NULL,
    resultado TEXT,
    estado VARCHAR(50) NOT NULL,
    FOREIGN KEY (solicitud_id) REFERENCES solicitudes_de_proceso(id)
);

-- Índice para mejorar las consultas por solicitud
CREATE INDEX idx_historial_solicitud_id ON historial_ejecuciones(solicitud_id);

-- Índice para mejorar las consultas por fecha
CREATE INDEX idx_historial_fecha_ejecucion ON historial_ejecuciones(fecha_ejecucion DESC);

