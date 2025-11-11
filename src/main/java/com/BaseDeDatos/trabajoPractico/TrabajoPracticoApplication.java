package com.BaseDeDatos.trabajoPractico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.WebApplicationType; // <-- 1. ¡Nueva importación necesaria!

@SpringBootApplication
public class TrabajoPracticoApplication {

    public static void main(String[] args) {
        // 2. Crear una instancia de SpringApplication para configurarla.
        SpringApplication application = new SpringApplication(TrabajoPracticoApplication.class);
        
        // 3. Deshabilitar explícitamente el tipo de aplicación web.
        application.setWebApplicationType(WebApplicationType.NONE); 
        
        // 4. Ejecutar la aplicación con la nueva configuración.
        application.run(args);
    }
}