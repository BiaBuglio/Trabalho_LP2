package com.mycompany.consultapsicologica.controller;

import com.mycompany.consultapsicologica.model.Psicologo;
import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.service.PsicologoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/psicologos")
public class PsicologosController {

    @Autowired
    private PsicologoService psicologoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listar(Model model) {
        model.addAttribute("psicologos", psicologoService.listarTodos());
        return "psicologos/lista";
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public String novo(Model model) {
        model.addAttribute("psicologo", new Psicologo());
        model.addAttribute("usuario", new Usuario());
        return "psicologos/form";
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvar(@Valid @ModelAttribute("psicologo") Psicologo psicologo,
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "psicologos/form";
        }

        if (psicologoService.existePorEmail(usuario.getEmail())) {
            result.rejectValue("usuario.email", "error.email", "Este e-mail já está em uso");
            return "psicologos/form";
        }

        if (psicologoService.existePorCrp(psicologo.getCrp())) {
            result.rejectValue("crp", "error.crp", "Este CRP já está cadastrado");
            return "psicologos/form";
        }

        psicologo.setUsuario(usuario);
        psicologoService.salvar(psicologo);
        attributes.addFlashAttribute("mensagem", "Psicólogo salvo com sucesso!");
        return "redirect:/psicologos";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model) {
        Psicologo psicologo = psicologoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        model.addAttribute("psicologo", psicologo);
        model.addAttribute("usuario", psicologo.getUsuario());
        return "psicologos/form";
    }

    @PostMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        psicologoService.excluir(id);
        attributes.addFlashAttribute("mensagem", "Psicólogo excluído com sucesso!");
        return "redirect:/psicologos";
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public String buscar(@RequestParam(required = false) String nome, Model model) {
        if (nome != null && !nome.isEmpty()) {
            model.addAttribute("psicologos", psicologoService.buscarPorNome(nome));
        } else {
            model.addAttribute("psicologos", psicologoService.listarTodos());
        }
        return "psicologos/lista";
    }

    @GetMapping("/error")
    public String buildingPage() {
        return "building-page"; // nome do HTML sem .html
    }
}
