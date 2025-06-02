package com.mycompany.consultapsicologica.service;

import com.mycompany.consultapsicologica.model.Paciente;
import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.PacienteRepository;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Paciente> listarTodos() {
        return pacienteRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.findByUsuario_NomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    @Transactional
    public Paciente salvar(Paciente paciente) {
        if (paciente.getUsuario() != null && paciente.getUsuario().getId() == null) {
            Usuario usuario = paciente.getUsuario();
            usuario.setPerfil(com.mycompany.consultapsicologica.model.Perfil.PACIENTE);
            usuario = usuarioRepository.save(usuario);
            paciente.setUsuario(usuario);
        }
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void excluir(Long id) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(id);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            paciente.setAtivo(false);
            pacienteRepository.save(paciente);
        }
    }

    @Transactional
    public boolean existePorEmail(String email) {
        return pacienteRepository.existsByUsuario_Email(email);
    }

    @Transactional(readOnly = true)
    public Paciente buscarPorEmail(String email) {
        return pacienteRepository.findByUsuario_Email(email);
    }
} 