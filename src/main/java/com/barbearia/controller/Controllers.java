package com.barbearia.controller;

import com.barbearia.dto.DTOs.*;
import com.barbearia.model.Cliente;
import com.barbearia.model.Servico;
import com.barbearia.model.Usuario;
import com.barbearia.repository.ServicoRepository;
import com.barbearia.repository.UsuarioRepository;
import com.barbearia.security.JwtUtil;
import com.barbearia.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// =================================================================
// AUTH CONTROLLER (SEM LOMBOK)
// =================================================================

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UsuarioRepository usuarioRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza login e retorna token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.gerarToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setEmail(usuario.getEmail());
        response.setNome(usuario.getNome());
        response.setRole(usuario.getRole());

        return ResponseEntity.ok(response);
    }
}

// =================================================================
// CLIENTE CONTROLLER (SEM LOMBOK) - usando ENTITY para compilar
// =================================================================

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "CRUD de clientes")
class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo cliente")
    public ResponseEntity<Cliente> criar(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.criar(request));
    }

    @GetMapping
    @Operation(summary = "Lista todos os clientes")
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca cliente por ID")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza dados do cliente")
    public ResponseEntity<Cliente> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request
    ) {
        return ResponseEntity.ok(clienteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove cliente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

// =================================================================
// SERVICO CONTROLLER (SEM LOMBOK)
// =================================================================

@RestController
@RequestMapping("/servicos")
@Tag(name = "Serviços", description = "CRUD de serviços")
class ServicoController {

    private final ServicoRepository servicoRepository;

    public ServicoController(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @PostMapping
    @Operation(summary = "Cria um novo serviço")
    public ResponseEntity<?> criar(@RequestBody Servico servico) {

        if (servico.getNome() == null || servico.getNome().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome do serviço é obrigatório.");
        }

        if (servicoRepository.existsByNome(servico.getNome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um serviço com esse nome.");
        }

        if (servico.getPreco() == null || servico.getPreco() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Preço precisa ser maior que 0.");
        }

        if (servico.getDuracaoMinutos() == null || servico.getDuracaoMinutos() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Duração precisa ser maior que 0.");
        }

        // default se vier null
        // (se seu entity já inicializa ativo=true, isso aqui é só proteção)
        if (!servico.isAtivo()) {
            // deixa como está
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoRepository.save(servico));
    }

    @GetMapping
    @Operation(summary = "Lista serviços ativos")
    public ResponseEntity<List<Servico>> listarAtivos() {
        return ResponseEntity.ok(servicoRepository.findByAtivoTrue());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca serviço por ID")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicoRepository.findById(id).orElseThrow());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza serviço")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Servico servico) {

        Servico existente = servicoRepository.findById(id).orElseThrow();

        // Atualiza só o que veio preenchido
        if (servico.getNome() != null && !servico.getNome().isBlank()) {
            // se mudar o nome, checa conflito
            if (!existente.getNome().equalsIgnoreCase(servico.getNome())
                    && servicoRepository.existsByNome(servico.getNome())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Já existe um serviço com esse nome.");
            }
            existente.setNome(servico.getNome());
        }

        if (servico.getDescricao() != null) {
            existente.setDescricao(servico.getDescricao());
        }

        if (servico.getPreco() != null && servico.getPreco() > 0) {
            existente.setPreco(servico.getPreco());
        }

        if (servico.getDuracaoMinutos() != null && servico.getDuracaoMinutos() > 0) {
            existente.setDuracaoMinutos(servico.getDuracaoMinutos());
        }

        // Permite ativar/desativar
        existente.setAtivo(servico.isAtivo());

        return ResponseEntity.ok(servicoRepository.save(existente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa serviço (soft delete)")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        Servico servico = servicoRepository.findById(id).orElseThrow();
        servico.setAtivo(false);
        servicoRepository.save(servico);
        return ResponseEntity.noContent().build();
    }
}