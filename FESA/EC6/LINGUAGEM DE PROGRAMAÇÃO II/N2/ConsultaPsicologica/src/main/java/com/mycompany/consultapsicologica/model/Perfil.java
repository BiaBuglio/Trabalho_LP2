/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.consultapsicologica.model;

public enum Perfil {
    ADMIN("Administrador"),
    PSICOLOGO("Psicólogo"),
    PACIENTE("Paciente"),
    RECEPCIONISTA("Recepcionista");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}