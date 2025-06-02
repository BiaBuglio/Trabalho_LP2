package com.mycompany.consultapsicologica.controller;

import com.mycompany.consultapsicologica.model.Paciente;
import com.mycompany.consultapsicologica.model.Usuario;
import com.mycompany.consultapsicologica.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pacientes")
public class PacientesController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public String listar(Model model) {
        model.addAttribute("pacientes", pacienteService.listarTodos());
        return "pacientes/lista";
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public String novo(Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("usuario", new Usuario());
        return "pacientes/form";
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public String salvar(@Valid @ModelAttribute("paciente") Paciente paciente,
                        @Valid @ModelAttribute("usuario") Usuario usuario,
                        BindingResult result,
                        RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "pacientes/form";
        }

        if (pacienteService.existePorEmail(usuario.getEmail())) {
            result.rejectValue("usuario.email", "error.email", "Este e-mail já está em uso");
            return "pacientes/form";
        }

        paciente.setUsuario(usuario);
        pacienteService.salvar(paciente);
        attributes.addFlashAttribute("mensagem", "Paciente salvo com sucesso!");
        return "redirect:/pacientes";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public String editar(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        model.addAttribute("paciente", paciente);
        model.addAttribute("usuario", paciente.getUsuario());
        return "pacientes/form";
    }

    @PostMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        pacienteService.excluir(id);
        attributes.addFlashAttribute("mensagem", "Paciente excluído com sucesso!");
        return "redirect:/pacientes";
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public String buscar(@RequestParam(required = false) String nome, Model model) {
        if (nome != null && !nome.isEmpty()) {
            model.addAttribute("pacientes", pacienteService.buscarPorNome(nome));
        } else {
            model.addAttribute("pacientes", pacienteService.listarTodos());
        }
        return "pacientes/lista";
    }
}
