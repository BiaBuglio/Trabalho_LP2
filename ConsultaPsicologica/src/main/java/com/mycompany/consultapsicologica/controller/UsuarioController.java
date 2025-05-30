package com.mycompany.consultapsicologica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.service.UsuarioService;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String mostrarFormularioCriacao(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    //Salva um usuario
    @PostMapping
    @RequestMapping("/salvar")
    public String salvarUsuario(@Validated @ModelAttribute("usuario") Usuario usuario, 
                              BindingResult result,
                              RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "usuarios/form";
        }
        usuarioService.criarUsuario(usuario);
        attributes.addFlashAttribute("mensagem", "Usuario criado com sucesso!");
        return "redirect:/"; // redirect retorna sempre uma controller e não uma view
    }
}