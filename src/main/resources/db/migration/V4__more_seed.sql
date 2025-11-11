-- V4: Datos adicionales de ejemplo

-- *** 1. Crear usuario de prueba para login/facturación ***
-- Creamos el usuario demo con su contraseña en texto plano 'demo123'
INSERT INTO usuarios (nombre_completo, email, password, estado, fecha_registro)
SELECT 'Usuario Demo', 'demo@example.com', 'demo123', 'ACTIVO', NOW()
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'demo@example.com');

-- *** 2. Asignación del rol 'usuario' (CORRECCIÓN) ***
-- Se busca el rol 'usuario' (que se insertó en V3) y se asigna al usuario demo.
INSERT INTO usuarios_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u 
CROSS JOIN roles r
WHERE u.email = 'demo@example.com' AND r.descripcion = 'usuario' -- <-- Buscamos el rol correcto 'usuario'
  AND NOT EXISTS (
      SELECT 1 FROM usuarios_roles ur WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
  );

-- *** 3. Crear Cuenta Corriente (PREPARACIÓN PARA FACTURACIÓN) ***
-- Para el Comando 6, el usuario debe tener una Cuenta Corriente asociada.
INSERT INTO cuentas_corrientes (usuario_id, saldo_actual)
SELECT u.id, 0.00 -- Saldo inicial en cero
FROM usuarios u
WHERE u.email = 'demo@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM cuentas_corrientes cc WHERE cc.usuario_id = u.id
  );