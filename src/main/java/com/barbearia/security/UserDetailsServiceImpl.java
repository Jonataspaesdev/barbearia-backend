package com.barbearia.security;

import com.barbearia.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Ajusta a role vinda do banco
        // Ex: "ADMIN", "BARBEIRO", "USER"
        String role = usuario.getRole();

        if (role == null || role.isBlank()) {
            role = "USER";
        }

        // remove "ROLE_" caso venha assim do banco, e deixa só "ADMIN"
        role = role.replace("ROLE_", "").trim();

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(role) // <-- AGORA usa a role real do usuário
                .build();
    }
}