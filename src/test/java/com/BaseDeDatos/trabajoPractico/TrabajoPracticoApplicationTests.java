package com.BaseDeDatos.trabajoPractico;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Context test deshabilitado para evitar requerir datasource en CI; usar tests específicos con Testcontainers")
class TrabajoPracticoApplicationTests {

	@Test
	void contextLoads() {
	}

}
