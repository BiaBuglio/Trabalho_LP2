package com.mycompany.consultapsicologica.service;

import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE
    public Usuario criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // READ
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // UPDATE
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNome(usuarioAtualizado.getNome());
                usuario.setPerfil(usuarioAtualizado.getPerfil());
                if (usuarioAtualizado.getSenha() != null) {
                    usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
                }
                return usuarioRepository.save(usuario);
            })
            .orElseGet(() -> {
                usuarioAtualizado.setId(id);
                return usuarioRepository.save(usuarioAtualizado);
            });
    }

    // DELETE (soft delete)
    public void desativarUsuario(Long id) {
        usuarioRepository.desativarUsuario(id);
    }
}