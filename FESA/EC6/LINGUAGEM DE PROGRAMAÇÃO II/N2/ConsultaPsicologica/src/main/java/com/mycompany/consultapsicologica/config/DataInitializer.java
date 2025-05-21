package com.mycompany.consultapsicologica.config;

import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByEmail("admin@clinica.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@clinica.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setNome("Administrador");
                admin.setPerfil("ADMIN");
                admin.setAtivo(true);
                usuarioRepository.save(admin);
            }
        };
    }
}