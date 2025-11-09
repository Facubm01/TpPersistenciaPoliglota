package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {

    @SuppressWarnings("resource") // evita el warning de "resource leak" - Testcontainers maneja el ciclo de vida automáticamente
    @Container
    private static final MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("tpdb")
                    .withUsername("tpuser")
                    .withPassword("tppass")
                    .withCommand("--default-authentication-plugin=mysql_native_password");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void guardarYBuscarUsuario_porId_funciona() {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Juan Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("password123");
        usuario.setEstado("ACTIVO");
        usuario.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(usuario);

        Optional<Usuario> opt = usuarioRepository.findById(usuario.getId());
        assertTrue(opt.isPresent());
    }
}
