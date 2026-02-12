package com.barbearia.config;

import com.barbearia.model.Usuario;
import com.barbearia.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String email = "admin@admin.com";
        String senha = "123456";

        usuarioRepository.findByEmail(email).ifPresentOrElse(admin -> {
            // ✅ Admin já existe: garante role certa e senha
            admin.setRole("ROLE_ADMIN");
            admin.setSenha(passwordEncoder.encode(senha));
            usuarioRepository.save(admin);

            System.out.println("✅ Admin atualizado: " + email + " / " + senha + " (ROLE_ADMIN)");
        }, () -> {
            // ✅ Admin não existe: cria já certo
            Usuario admin = new Usuario();
            admin.setEmail(email);
            admin.setNome("Administrador");
            admin.setRole("ROLE_ADMIN");
            admin.setSenha(passwordEncoder.encode(senha));
            usuarioRepository.save(admin);

            System.out.println("✅ Admin criado: " + email + " / " + senha + " (ROLE_ADMIN)");
        });
    }
}