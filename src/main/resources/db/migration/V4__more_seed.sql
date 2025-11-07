-- Datos adicionales de ejemplo

INSERT INTO roles (descripcion) VALUES ('analista') ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

INSERT INTO usuarios (nombre_completo, email, password, estado, fecha_registro)
SELECT 'Usuario Demo', 'demo@example.com', 'demo123', 'ACTIVO', NOW()
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'demo@example.com');

INSERT INTO usuarios_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u CROSS JOIN roles r
WHERE u.email = 'demo@example.com' AND r.descripcion = 'usuario'
  AND NOT EXISTS (
      SELECT 1 FROM usuarios_roles ur WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
  );


