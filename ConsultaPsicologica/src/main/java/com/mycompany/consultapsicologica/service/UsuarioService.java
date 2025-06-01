package com.mycompany.consultapsicologica.service;

import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException; // Importar DataIntegrityViolationException
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
        if (usuario.getId() == null) { // Apenas para novos usuários
             usuario.setAtivo(true); // Garante que o usuário é ativo por padrão
        }
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            // Verifica se a exceção é devido à restrição de unicidade do e-mail
            // A mensagem de erro pode variar dependendo do banco de dados (H2, MySQL, PostgreSQL)
            // Para H2, a mensagem de erro da imagem é clara sobre "unique index"
            // Você pode adicionar mais verificações aqui se quiser ser mais específico para outros bancos
            if (e.getMessage().contains("EMAIL") || e.getMessage().contains("unique index")) {
                throw new EmailAlreadyExistsException(usuario.getEmail());
            }
            throw e; // Relança a exceção original se não for sobre o e-mail
        }
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
            .map(usuarioExistente -> {
                usuarioExistente.setNome(usuarioAtualizado.getNome());
                usuarioExistente.setPerfil(usuarioAtualizado.getPerfil());
                
                if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                    usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
                }
                
                try { // Também capturar na atualização caso o email seja alterado para um existente
                    return usuarioRepository.save(usuarioExistente);
                } catch (DataIntegrityViolationException e) {
                    if (e.getMessage().contains("EMAIL") || e.getMessage().contains("unique index")) {
                        throw new EmailAlreadyExistsException(usuarioAtualizado.getEmail());
                    }
                    throw e;
                }
            })
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado para atualização com ID: " + id));
    }

    // DELETE (soft delete)
    public void desativarUsuario(Long id) {
        usuarioRepository.desativarUsuario(id);
    }
}