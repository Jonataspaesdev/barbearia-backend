package com.barbearia.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

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
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/servicos", "/servicos/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/servicos").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/servicos/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/servicos/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/barbeiros", "/barbeiros/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")

                        .requestMatchers(HttpMethod.POST, "/barbeiros/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/barbeiros/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/barbeiros/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/clientes").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.POST, "/agendamentos").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/cliente/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/disponibilidade").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/agendamentos").hasAnyAuthority("ROLE_ADMIN", "ROLE_BARBEIRO")
                        .requestMatchers(HttpMethod.GET, "/agendamentos/barbeiro/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BARBEIRO")
                        .requestMatchers(HttpMethod.PUT, "/agendamentos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_BARBEIRO")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();

        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
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