-- Creación de la tabla para gestionar las sesiones de usuario
CREATE TABLE sesiones (
    -- ID único de la sesión
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Clave foránea al usuario que inició la sesión
    usuario_id BIGINT NOT NULL,

    -- Clave foránea al rol que se usó en la sesión
    rol_id BIGINT NOT NULL,

    -- Fecha y hora de inicio de la sesión
    fecha_hora_inicio DATETIME NOT NULL,

    -- Fecha y hora de cierre (nulo si sigue activa)
    fecha_hora_cierre DATETIME NULL,

    -- Estado de la sesión (ej: "ACTIVA", "INACTIVA")
    estado VARCHAR(20) NOT NULL,

    -- Definición de las claves foráneas
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);