package com.mycompany.consultapsicologica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // Garante que @PreAuthorize funcione
public class WebSecurityConfig {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/error",
                "/favicon.ico",
                "/js/**",           // liberar scripts
                "/css/**",          // liberar css
                "/images/**",       // ajustar conforme sua pasta de imagens (ex: /IMG/**)
                "/IMG/**",
                "/h2-console/**"    // liberar console H2 (APENAS PARA DESENVOLVIMENTO)
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas (sem necessidade de login)
                .requestMatchers("/", "/index", "/auth/login", "/auth/cadastro").permitAll()
                // Permitir POST para o formulário de cadastro público de usuários (PACIENTES)
                .requestMatchers(HttpMethod.POST, "/usuarios/cadastro").permitAll()
                
                // O dashboard requer que o usuário esteja autenticado
                .requestMatchers("/dashboard").authenticated()
                
                // PROTEGER ROTAS DE GERENCIAMENTO DE USUÁRIOS: APENAS ADMIN PODE ACESSAR
                .requestMatchers("/usuarios/**").hasRole("ADMIN") // Todas as rotas sob /usuarios/ exigem a ROLE_ADMIN
                
                // Rotas futuras de gerenciamento de psicólogos e pacientes também seriam protegidas aqui
                // .requestMatchers("/psicologos/**").hasRole("ADMIN")
                // .requestMatchers("/pacientes/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "PSICOLOGO")
                
                // Todas as outras requisições que não foram explicitamente permitidas, requerem autenticação
                .anyRequest().authenticated()
            )
            //Configura o login
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/dashboard", true) // Redireciona para o dashboard após login bem-sucedido
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )

            //Configura o logout
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .headers(headers -> headers.frameOptions().sameOrigin())
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}