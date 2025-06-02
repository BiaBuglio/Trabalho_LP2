package com.mycompany.consultapsicologica.repository;

import com.mycompany.consultapsicologica.model.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PsicologoRepository extends JpaRepository<Psicologo, Long> {
    List<Psicologo> findByAtivoTrue();
    List<Psicologo> findByUsuario_NomeContainingIgnoreCase(String nome);
    boolean existsByUsuario_Email(String email);
    boolean existsByCrp(String crp);
    Psicologo findByUsuario_Email(String email);
} 