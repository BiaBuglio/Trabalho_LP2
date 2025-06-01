package com.mycompany.consultapsicologica.controller;

import com.mycompany.consultapsicologica.model.Perfil; // Importar o enum Perfil
import com.mycompany.consultapsicologica.model.Usuario; // Importar a entidade Usuario
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Para acessar o usuário logado
import org.springframework.security.core.userdetails.UserDetails; // Tipo do usuário logado
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.mycompany.consultapsicologica.service.UsuarioService; // Para buscar o usuário completo

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UsuarioService usuarioService;

    @Autowired
    public DashboardController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String showDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // userDetails contém apenas o email (username) e as roles.
        // Para obter o objeto Usuario completo, precisamos buscá-lo pelo email.
        Usuario usuarioLogado = usuarioService.buscarPorEmail(userDetails.getUsername())
                                    .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado no banco de dados."));
        
        model.addAttribute("usuarioLogado", usuarioLogado); // Adiciona o objeto Usuario completo ao modelo

        // Lógica para determinar qual parte do dashboard exibir ou quais dados passar
        // Podemos usar o perfil do usuário para isso.
        model.addAttribute("perfilUsuario", usuarioLogado.getPerfil().name()); // Envia o nome do perfil (ex: "ADMIN")

        // Exemplo de como passar dados condicionais
        if (usuarioLogado.getPerfil() == Perfil.ADMIN) {
            // Futuramente, pode adicionar dados específicos do admin, como contagem de usuários
             model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        } else if (usuarioLogado.getPerfil() == Perfil.PSICOLOGO) {
            // Dados para psicólogo: próximos agendamentos, lista de pacientes
            // model.addAttribute("proximosAgendamentos", consultaService.buscarProximosAgendamentos(usuarioLogado));
        } else if (usuarioLogado.getPerfil() == Perfil.PACIENTE) {
            // Dados para paciente: suas consultas
            // model.addAttribute("minhasConsultas", consultaService.buscarMinhasConsultas(usuarioLogado));
        }
        
        return "dashboard"; // Retorna o nome do template Thymeleaf
    }
}