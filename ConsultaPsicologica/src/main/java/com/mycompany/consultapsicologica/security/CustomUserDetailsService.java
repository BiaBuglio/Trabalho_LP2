package com.mycompany.consultapsicologica.security;

import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List; 
import java.util.stream.Collectors; 

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado para o email: " + email));

        // CRUCIAL: Cria uma lista de GrantedAuthority baseada no perfil do usuário, prefixando com "ROLE_"
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()) // Usar .name() para obter a String do enum
        );

        return new User(
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.isAtivo(),       // Habilitar ou desabilitar o usuário com base no campo 'ativo'
            true,                    // accountNonExpired (sempre true por enquanto)
            true,                    // credentialsNonExpired (sempre true por enquanto)
            true,                    // accountNonLocked (sempre true por enquanto)
            authorities              // As roles do usuário
        );
    }
}