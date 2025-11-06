-- Seed de datos iniciales para MySQL

-- Roles básicos
INSERT INTO roles (descripcion) VALUES ('administrador') ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);
INSERT INTO roles (descripcion) VALUES ('tecnico') ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);
INSERT INTO roles (descripcion) VALUES ('usuario') ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Usuario admin por defecto (si no existe)
INSERT INTO usuarios (nombre_completo, email, password, estado, fecha_registro)
SELECT 'Administrador', 'admin@example.com', 'admin123', 'ACTIVO', NOW()
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@example.com');

-- Asignar rol administrador al usuario admin
INSERT INTO usuarios_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u CROSS JOIN roles r
WHERE u.email = 'admin@example.com' AND r.descripcion = 'administrador'
  AND NOT EXISTS (
      SELECT 1 FROM usuarios_roles ur WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
  );


