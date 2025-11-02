-- V2__create_billing_and_process_tables.sql

-- Catálogo de procesos/servicios disponibles
CREATE TABLE procesos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- CORRECCIÓN: 'IDENTITY(1,1)' cambiado a 'AUTO_INCREMENT'
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT,
    tipo_proceso VARCHAR(100) NOT NULL,
    costo DECIMAL(10, 2) NOT NULL
);

-- Tabla para las facturas emitidas a los usuarios
CREATE TABLE facturas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- CORRECCIÓN: 'IDENTITY(1,1)' cambiado a 'AUTO_INCREMENT'
    usuario_id BIGINT NOT NULL,
    fecha_emision DATE NOT NULL,
    estado VARCHAR(50) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla para las solicitudes de procesos que hacen los usuarios
CREATE TABLE solicitudes_de_proceso (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- CORRECCIÓN: 'IDENTITY(1,1)' cambiado a 'AUTO_INCREMENT'
    usuario_id BIGINT NOT NULL,
    proceso_id BIGINT NOT NULL,
    factura_id BIGINT, -- Puede ser nulo hasta que se facture
    fecha_solicitud TIMESTAMP NOT NULL,
    estado VARCHAR(50) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (proceso_id) REFERENCES procesos(id),
    FOREIGN KEY (factura_id) REFERENCES facturas(id)
);

-- Tabla para registrar los pagos de las facturas
CREATE TABLE pagos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- CORRECCIÓN: 'IDENTITY(1,1)' cambiado a 'AUTO_INCREMENT'
    factura_id BIGINT NOT NULL,
    fecha_pago TIMESTAMP NOT NULL,
    monto_pagado DECIMAL(10, 2) NOT NULL,
    metodo_pago VARCHAR(100) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id)
);

-- Tabla para la cuenta corriente de cada usuario
CREATE TABLE cuentas_corrientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- CORRECCIÓN: 'IDENTITY(1,1)' cambiado a 'AUTO_INCREMENT'
    usuario_id BIGINT NOT NULL UNIQUE, -- UNIQUE asegura la relación 1 a 1
    saldo_actual DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);