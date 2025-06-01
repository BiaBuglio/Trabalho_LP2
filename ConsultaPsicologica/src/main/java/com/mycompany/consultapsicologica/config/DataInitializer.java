package com.mycompany.consultapsicologica.config;

import com.mycompany.consultapsicologica.model.Perfil;
import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException; // Importar

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Tenta criar o ADMIN padrão
            try {
                if (usuarioRepository.findByEmail("admin@clinica.com").isEmpty()) {
                    Usuario admin = new Usuario();
                    admin.setEmail("admin@clinica.com");
                    admin.setSenha(passwordEncoder.encode("admin123"));
                    admin.setNome("Administrador Principal");
                    admin.setPerfil(Perfil.ADMIN);
                    admin.setAtivo(true);
                    usuarioRepository.save(admin);
                }
            } catch (DataIntegrityViolationException e) {
                System.err.println("Atenção: O usuário admin@clinica.com já existe. Pulando criação. Erro: " + e.getMessage());
            }

            // Tenta criar o Psicólogo de Teste
            try {
                if (usuarioRepository.findByEmail("testepsicologo@clinica.com").isEmpty()) {
                    Usuario psicologo = new Usuario();
                    psicologo.setEmail("testepsicologo@clinica.com");
                    psicologo.setSenha(passwordEncoder.encode("psico123"));
                    psicologo.setNome("Psicólogo de Teste");
                    psicologo.setPerfil(Perfil.PSICOLOGO);
                    psicologo.setAtivo(true);
                    usuarioRepository.save(psicologo);
                }
            } catch (DataIntegrityViolationException e) {
                System.err.println("Atenção: O usuário testepsicologo@clinica.com já existe. Pulando criação. Erro: " + e.getMessage());
            }

            // Tenta criar o Paciente de Teste
            try {
                if (usuarioRepository.findByEmail("testepaciente@clinica.com").isEmpty()) {
                    Usuario paciente = new Usuario();
                    paciente.setEmail("testepaciente@clinica.com");
                    paciente.setSenha(passwordEncoder.encode("paciente123"));
                    paciente.setNome("Paciente de Teste");
                    paciente.setPerfil(Perfil.PACIENTE);
                    paciente.setAtivo(true);
                    usuarioRepository.save(paciente);
                }
            } catch (DataIntegrityViolationException e) {
                System.err.println("Atenção: O usuário testepaciente@clinica.com já existe. Pulando criação. Erro: " + e.getMessage());
            }

            // Tenta criar o Recepcionista de Teste
            try {
                if (usuarioRepository.findByEmail("testrecepcionista@clinica.com").isEmpty()) {
                    Usuario recepcionista = new Usuario();
                    recepcionista.setEmail("testrecepcionista@clinica.com");
                    recepcionista.setSenha(passwordEncoder.encode("recep123"));
                    recepcionista.setNome("Recepcionista de Teste");
                    recepcionista.setPerfil(Perfil.RECEPCIONISTA);
                    recepcionista.setAtivo(true);
                    usuarioRepository.save(recepcionista);
                }
            } catch (DataIntegrityViolationException e) {
                System.err.println("Atenção: O usuário testrecepcionista@clinica.com já existe. Pulando criação. Erro: " + e.getMessage());
            }
        };
    }
}