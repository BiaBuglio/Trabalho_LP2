package com.mycompany.consultapsicologica.repository;

import com.mycompany.consultapsicologica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por nome
    Optional<Usuario> findByNome(String nome);
    
    // SELECT automático do Spring Data
    Optional<Usuario> findByEmail(String email);
    
    // Consulta customizada para update
    @Modifying
    @Query("UPDATE Usuario u SET u.nome = :nome, u.perfil = :perfil WHERE u.id = :id")
    void atualizarUsuario(@Param("id") Long id, 
                         @Param("nome") String nome, 
                         @Param("perfil") String perfil);
    
    // Consulta customizada para desativar usuário
    @Modifying
    @Query("UPDATE Usuario u SET u.ativo = false WHERE u.id = :id")
    void desativarUsuario(@Param("id") Long id);
}