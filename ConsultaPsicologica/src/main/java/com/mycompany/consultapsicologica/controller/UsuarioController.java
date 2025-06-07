package com.mycompany.consultapsicologica.controller;

import com.mycompany.consultapsicologica.model.Perfil;
import com.mycompany.consultapsicologica.service.EmailAlreadyExistsException; // Importar a nova exceção
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/index";
    }

    @GetMapping("/novo")
    public String mostrarFormularioCriacao(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("perfis", Perfil.values());
        return "usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        return usuarioService.buscarPorId(id).map(usuario -> {
            model.addAttribute("usuario", usuario);
            model.addAttribute("perfis", Perfil.values());
            return "usuarios/form";
        }).orElseGet(() -> {
            attributes.addFlashAttribute("mensagemErro", "Usuário não encontrado para edição.");
            return "redirect:/usuarios";
        });
    }

    // Salva um usuario (ou atualiza)
    @PostMapping("/salvar")
    public String salvarUsuario(@Validated @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("perfis", Perfil.values());
            return "usuarios/form";
        }

        try {
            if (usuario.getId() == null) {
                usuarioService.criarUsuario(usuario);
                attributes.addFlashAttribute("mensagem", "Usuário criado com sucesso!");
            } else {
                usuarioService.atualizarUsuario(usuario.getId(), usuario);
                attributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
            }
        } catch (EmailAlreadyExistsException e) {
            // Adiciona o erro ao BindingResult para que seja exibido no campo email
            result.rejectValue("email", "error.usuario", e.getMessage());
            model.addAttribute("perfis", Perfil.values()); // Manter perfis para a view
            return "usuarios/form"; // Retornar ao formulário com o erro
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/desativar/{id}")
    public String desativarUsuario(@PathVariable Long id, RedirectAttributes attributes) {
        if (id == 1L) { // Supondo que o ADMIN padrão é o ID 1
            attributes.addFlashAttribute("mensagemErro", "O usuário administrador principal não pode ser desativado.");
            return "redirect:/usuarios";
        }

        usuarioService.desativarUsuario(id);
        attributes.addFlashAttribute("mensagem", "Usuário desativado com sucesso!");
        return "redirect:/usuarios";
    }

    // Lidar com o POST do formulário de cadastro público
    @PostMapping("/cadastro")
    public String processarCadastroPublico(@Validated @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            // Note: para o cadastro público, não precisamos de "perfis" no Model,
            // pois o campo de perfil não existe no form.html da pasta cadastro.
            return "cadastro/form";
        }

        usuario.setPerfil(Perfil.PACIENTE); // Define o perfil como PACIENTE
        usuario.setAtivo(true);

        try {
            usuarioService.criarUsuario(usuario);
            attributes.addFlashAttribute("mensagem", "Sua conta foi criada com sucesso! Faça login para continuar.");
            return "redirect:/auth/login";
        } catch (EmailAlreadyExistsException e) {
            // Adiciona o erro ao BindingResult para que seja exibido no campo email
            result.rejectValue("email", "error.usuario", e.getMessage());
            return "cadastro/form"; // Retornar ao formulário com o erro
        }
    }

    @GetMapping("/error")
    public String buildingPage() {
        return "building-page"; // nome do HTML sem .html
    }
}
