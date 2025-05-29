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
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/error",
                "/favicon.ico",
                "/js/**",          // liberar scripts
                "/css/**",         // liberar css
                "/images/**",      // ajustar conforme sua pasta de imagens (ex: /IMG/**)
                "/IMG/**",
                "/h2-console/**"   // liberar console H2
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth //ai em baixo você pode definir quais caminhos das controller estão livres sem login
                .requestMatchers("/", "/index", "/auth/login", "/usuario/cadastro", "/usuario/autenticar", "/h2-console/**", "auth/cadastro", "usuarios/salvar").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuario/salvar").permitAll()
                .anyRequest().authenticated()
            )
            //Configura o login
            .formLogin(form -> form
                .loginPage("/auth/login") //pagina de login
                .loginProcessingUrl("/auth/login")// caminho da função
                .defaultSuccessUrl("/", true)//a onde será redirecionado quando aprovado
                .failureUrl("/auth/login?error=true")//tela de erro
                .permitAll()
            )

            //Configura o logout
            .logout(logout -> logout
                .logoutUrl("/auth/logout") // caminho do logout
                .logoutSuccessUrl("/") // a onde será redirecionado quando sair
                .invalidateHttpSession(true)  // invalida a sessão
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