package com.mycompany.consultapsicologica.repository;

import com.mycompany.consultapsicologica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    // Mantemos apenas a query de desativar, pois a de atualizar será feita via save() no service
    @Modifying
    @Query("UPDATE Usuario u SET u.ativo = false WHERE u.id = :id")
    void desativarUsuario(@Param("id") Long id);
}