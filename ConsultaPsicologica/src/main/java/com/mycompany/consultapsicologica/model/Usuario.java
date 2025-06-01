package com.mycompany.consultapsicologica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // Importar EnumType
import jakarta.persistence.Enumerated; // Importar Enumerated
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email; // Para validação de email
import jakarta.validation.constraints.NotBlank; // Para validação de campos não vazios
import jakarta.validation.constraints.NotNull; // Para validação de campos não nulos
import jakarta.validation.constraints.Size; // Para validação de tamanho de campo


@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O e-mail é obrigatório.") // Adicionado validação
    @Email(message = "Por favor, insira um e-mail válido.") // Adicionado validação
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória.") // Adicionado validação
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.") // Adicionado validação
    @Column(nullable = false)
    private String senha;

    @NotBlank(message = "O nome é obrigatório.") // Adicionado validação
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "O perfil é obrigatório.") // Adicionado validação
    @Enumerated(EnumType.STRING) // Anotação para mapear o Enum como String no banco
    @Column(nullable = false)
    private Perfil perfil; // Alterado o tipo para o Enum Perfil

    @Column(nullable = false)
    private boolean ativo = true; // Valor padrão para ativo

    // Getters e Setters completos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Perfil getPerfil() { // Retorna o Enum
        return perfil;
    }

    public void setPerfil(Perfil perfil) { // Recebe o Enum
        this.perfil = perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}