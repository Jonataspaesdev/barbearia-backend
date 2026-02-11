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

        if (usuarioRepository.findByEmail(email).isEmpty()) {

            Usuario admin = new Usuario();
            admin.setEmail(email);
            admin.setNome("Administrador");
            admin.setRole("ADMIN"); // importante: bater com hasRole("ADMIN")
            admin.setSenha(passwordEncoder.encode("123456"));

            usuarioRepository.save(admin);

            System.out.println("✅ Admin criado: admin@admin.com / 123456");
        } else {
            System.out.println("ℹ️ Admin já existe...");
        }
    }
}