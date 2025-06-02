package com.mycompany.consultapsicologica.repository;

import com.mycompany.consultapsicologica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByAtivoTrue();
    List<Paciente> findByUsuario_NomeContainingIgnoreCase(String nome);
    boolean existsByUsuario_Email(String email);
    Paciente findByUsuario_Email(String email);
} 