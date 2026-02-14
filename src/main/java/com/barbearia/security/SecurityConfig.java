package com.barbearia.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ✅ libera o pré-flight do navegador
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ Auth público
                        .requestMatchers("/auth/**").permitAll()

                        // ✅ Swagger público
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ✅ Serviços (listar público)
                        .requestMatchers(HttpMethod.GET, "/servicos").permitAll()

                        // ✅ Serviços (somente ADMIN)
                        .requestMatchers(HttpMethod.POST, "/servicos").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/servicos/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/servicos/**").hasAuthority("ROLE_ADMIN")

                        // ✅ Barbeiros (somente ADMIN)
                        .requestMatchers("/barbeiros/**").hasAuthority("ROLE_ADMIN")

                        // ✅ Clientes (somente ADMIN)
                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/clientes").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasAuthority("ROLE_ADMIN")

                        // ✅ Agendamentos
                        // - Cliente logado pode criar e ver os próprios (vamos reforçar no Controller também)
                        .requestMatchers(HttpMethod.POST, "/agendamentos").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/cliente/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/agendamentos").hasAnyAuthority("ROLE_ADMIN", "ROLE_BARBEIRO")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/barbeiro/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BARBEIRO")

                        // ✅ Atualizar agendamento (ADMIN ou BARBEIRO) - mantém sua regra
                        .requestMatchers(HttpMethod.PUT, "/agendamentos/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_BARBEIRO")

                        // Resto precisa estar logado
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}