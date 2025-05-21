/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.seuprojeto.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class MeuUserDetailsService implements UserDetailsService {

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if ("admin@example.com".equals(username)) {
            return User.builder()
                .username("admin@example.com")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("USER")
                .build();
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}
