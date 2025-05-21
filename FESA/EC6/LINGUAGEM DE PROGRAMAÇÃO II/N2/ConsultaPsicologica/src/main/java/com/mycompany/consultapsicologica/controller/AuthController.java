package com.mycompany.consultapsicologica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro/formulario";
    }

    @GetMapping("/esqueci-senha")
    public String esqueciSenha() {
        return "auth/esqueci-senha";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
    
    @GetMapping("/criar-conta")
    public String criarconta() {
        return "cadastro/formulario";
    }
}