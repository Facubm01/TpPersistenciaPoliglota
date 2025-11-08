package com.BaseDeDatos.trabajoPractico.repository.mysql;

import com.BaseDeDatos.trabajoPractico.model.mysql.Usuario;
import org.junit.jupiter.api.AfterAll;
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

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {

    @Container
    @SuppressWarnings("resource") // Testcontainers maneja el ciclo de vida automáticamente
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("tpdb")
            .withUsername("tpuser")
            .withPassword("tppass")
            .withCommand("--default-authentication-plugin=mysql_native_password");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    void creaYLeeUsuario() {
        Usuario u = new Usuario();
        u.setNombreCompleto("Test User");
        u.setEmail("testuser@example.com");
        u.setPassword("secreta");
        u.setEstado("ACTIVO");
        u.setFechaRegistro(LocalDateTime.now());
        Usuario guardado = usuarioRepository.save(u);

        assertThat(guardado.getId()).isNotNull();
        Usuario encontrado = usuarioRepository.findByEmail("testuser@example.com");
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNombreCompleto()).isEqualTo("Test User");
    }
}


