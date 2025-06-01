package com.mycompany.consultapsicologica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.consultapsicologica.model.Usuario;

//Caminho de autenticação
@Controller
@RequestMapping("/auth")
public class AuthController {

    //Model é o que você usa para enviar dados para a view, pode dar erro se não enviar algo que ela pede
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());// manda um usuario vazio para preenceher
        return "auth/login";
    }

    // O cadastro público agora será processado por UsuarioController.processarCadastroPublico
    // E o GET para o formulário de cadastro público também será em AuthController
    @GetMapping("/cadastro")
    public String cadastroPublico(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro/form"; // Este é o formulário de cadastro que será usado publicamente
    }

    @GetMapping("/esqueci-senha")
    public String esqueciSenha() {
        return "auth/esqueci-senha";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}