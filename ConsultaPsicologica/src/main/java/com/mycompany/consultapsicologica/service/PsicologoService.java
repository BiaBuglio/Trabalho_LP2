package com.mycompany.consultapsicologica.service;

import com.mycompany.consultapsicologica.model.Psicologo;
import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.PsicologoRepository;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PsicologoService {

    @Autowired
    private PsicologoRepository psicologoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Psicologo> listarTodos() {
        return psicologoRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Psicologo> buscarPorNome(String nome) {
        return psicologoRepository.findByUsuario_NomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public Optional<Psicologo> buscarPorId(Long id) {
        return psicologoRepository.findById(id);
    }

    @Transactional
    public Psicologo salvar(Psicologo psicologo) {
        if (psicologo.getUsuario() != null && psicologo.getUsuario().getId() == null) {
            Usuario usuario = psicologo.getUsuario();
            usuario.setPerfil(com.mycompany.consultapsicologica.model.Perfil.PSICOLOGO);
            usuario = usuarioRepository.save(usuario);
            psicologo.setUsuario(usuario);
        }
        return psicologoRepository.save(psicologo);
    }

    @Transactional
    public void excluir(Long id) {
        Optional<Psicologo> psicologoOpt = psicologoRepository.findById(id);
        if (psicologoOpt.isPresent()) {
            Psicologo psicologo = psicologoOpt.get();
            psicologo.setAtivo(false);
            psicologoRepository.save(psicologo);
        }
    }

    @Transactional
    public boolean existePorEmail(String email) {
        return psicologoRepository.existsByUsuario_Email(email);
    }

    @Transactional
    public boolean existePorCrp(String crp) {
        return psicologoRepository.existsByCrp(crp);
    }

    @Transactional(readOnly = true)
    public Psicologo buscarPorEmail(String email) {
        return psicologoRepository.findByUsuario_Email(email);
    }
} 