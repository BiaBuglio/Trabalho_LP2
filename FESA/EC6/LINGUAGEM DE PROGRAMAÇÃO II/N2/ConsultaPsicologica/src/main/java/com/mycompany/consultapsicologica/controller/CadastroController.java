package com.mycompany.consultapsicologica.controller;

import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro/form"; // View: src/main/resources/templates/cadastro/form.html
    }

    @PostMapping
    public String cadastrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        // Verifica se email já está em uso
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("erro", "Este e-mail já está cadastrado.");
            return "cadastro/form";
        }

        // Salva no banco
        usuarioRepository.save(usuario);
        
        Optional<Usuario> usuarioExistente = usuarioRepository.findByNome(usuario.getNome());

    if (usuarioExistente.isPresent()) {
        // Exibe os dados do usuário encontrado
        System.out.println("Usuário encontrado: " + usuarioExistente.get());

    } else {
        System.out.println("Usuário não encontrado.");
    }        // Redireciona para login ou dashboard
        return "redirect:/auth/login?cadastroSucesso";
    }
}