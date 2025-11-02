-- V1__create_user_and_role_tables.sql

-- Tabla para almacenar los roles del sistema
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- CORRECCIÓN: 'IDENTITY(1,1)' cambiado a 'AUTO_INCREMENT'
    descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla para almacenar los usuarios
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- CORRECCIÓN: 'IDENTITY(1,1)' cambiado a 'AUTO_INCREMENT'
    nombre_completo VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_registro DATETIME NOT NULL -- CORRECCIÓN: 'DATETIME2' cambiado a 'DATETIME' (sintaxis MySQL)
);

-- Tabla intermedia para la relación Muchos a Muchos entre usuarios y roles
CREATE TABLE usuarios_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- Opcional: Insertar roles iniciales para que existan al iniciar
INSERT INTO roles (descripcion) VALUES ('usuario'), ('tecnico'), ('administrador');