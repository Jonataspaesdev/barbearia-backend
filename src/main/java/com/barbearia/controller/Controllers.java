package com.barbearia.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.dto.DTOs.AgendamentoRequest;
import com.barbearia.dto.DTOs.AgendamentoResponse;
import com.barbearia.dto.DTOs.AgendamentoUpdateRequest;
import com.barbearia.dto.DTOs.BarbeiroRequest;
import com.barbearia.dto.DTOs.BarbeiroResponse;
import com.barbearia.dto.DTOs.ClienteRequest;
import com.barbearia.dto.DTOs.LoginRequest;
import com.barbearia.dto.DTOs.LoginResponse;
import com.barbearia.dto.DTOs.PagamentoRequest;
import com.barbearia.dto.DTOs.PagamentoResponse;
import com.barbearia.dto.DTOs.RelatorioFinanceiroResponse;
import com.barbearia.model.Barbeiro;
import com.barbearia.model.Cliente;
import com.barbearia.model.Servico;
import com.barbearia.model.Usuario;
import com.barbearia.repository.BarbeiroRepository;
import com.barbearia.repository.ServicoRepository;
import com.barbearia.repository.UsuarioRepository;
import com.barbearia.security.JwtUtil;
import com.barbearia.service.AgendamentoService;
import com.barbearia.service.ClienteService;
import com.barbearia.service.PagamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
// CLIENTE CONTROLLER (SEM LOMBOK)
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

        if (servico.getNome() != null && !servico.getNome().isBlank()) {
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

// =================================================================
// BARBEIRO CONTROLLER (SEM LOMBOK) - CRIA USUARIO
// =================================================================

@RestController
@RequestMapping("/barbeiros")
@Tag(name = "Barbeiros", description = "CRUD de barbeiros")
class BarbeiroController {

    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public BarbeiroController(
            BarbeiroRepository barbeiroRepository,
            ServicoRepository servicoRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Operation(summary = "Cria um novo barbeiro (cria Usuario ROLE_BARBEIRO automaticamente)")
    public ResponseEntity<BarbeiroResponse> criar(@Valid @RequestBody BarbeiroRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setRole("ROLE_BARBEIRO");
            usuario.setSenha(passwordEncoder.encode("123456"));
            usuario = usuarioRepository.save(usuario);
        }

        Barbeiro b = new Barbeiro();
        b.setNome(request.getNome());
        b.setEmail(request.getEmail());
        b.setTelefone(request.getTelefone());
        b.setHoraEntrada(request.getHoraEntrada());
        b.setHoraSaida(request.getHoraSaida());
        b.setUsuario(usuario);
        b.setAtivo(true);

        if (request.getServicoIds() != null && !request.getServicoIds().isEmpty()) {
            List<Servico> servicos = servicoRepository.findAllById(request.getServicoIds());
            b.setServicos(new ArrayList<>(servicos));
        }

        b = barbeiroRepository.save(b);

        BarbeiroResponse resp = new BarbeiroResponse();
        resp.setId(b.getId());
        resp.setNome(b.getNome());
        resp.setEmail(b.getEmail());
        resp.setTelefone(b.getTelefone());

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // ✅ Lista SOMENTE barbeiros ativos (pra não confundir)
    @GetMapping
    @Operation(summary = "Lista todos os barbeiros ativos")
    public ResponseEntity<List<BarbeiroResponse>> listar() {

        List<BarbeiroResponse> lista = barbeiroRepository.findAll().stream()
                .filter(Barbeiro::isAtivo)
                .map(b -> {
                    BarbeiroResponse resp = new BarbeiroResponse();
                    resp.setId(b.getId());
                    resp.setNome(b.getNome());
                    resp.setEmail(b.getEmail());
                    resp.setTelefone(b.getTelefone());
                    return resp;
                }).toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca barbeiro por ID")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        Barbeiro b = barbeiroRepository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Barbeiro não encontrado: " + id);
        }

        BarbeiroResponse resp = new BarbeiroResponse();
        resp.setId(b.getId());
        resp.setNome(b.getNome());
        resp.setEmail(b.getEmail());
        resp.setTelefone(b.getTelefone());

        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza barbeiro")
    public ResponseEntity<BarbeiroResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody BarbeiroRequest request) {

        Barbeiro b = barbeiroRepository.findById(id).orElseThrow();

        if (request.getNome() != null) b.setNome(request.getNome());
        if (request.getTelefone() != null) b.setTelefone(request.getTelefone());
        if (request.getHoraEntrada() != null) b.setHoraEntrada(request.getHoraEntrada());
        if (request.getHoraSaida() != null) b.setHoraSaida(request.getHoraSaida());

        if (request.getServicoIds() != null) {
            List<Servico> servicos = servicoRepository.findAllById(request.getServicoIds());
            b.setServicos(new ArrayList<>(servicos));
        }

        b = barbeiroRepository.save(b);

        BarbeiroResponse resp = new BarbeiroResponse();
        resp.setId(b.getId());
        resp.setNome(b.getNome());
        resp.setEmail(b.getEmail());
        resp.setTelefone(b.getTelefone());

        return ResponseEntity.ok(resp);
    }

    // ✅ SOFT DELETE (não apaga do banco)
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa barbeiro (soft delete)")
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        Barbeiro b = barbeiroRepository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Barbeiro não encontrado: " + id);
        }

        b.setAtivo(false);
        barbeiroRepository.save(b);

        return ResponseEntity.noContent().build();
    }

    // ✅ Reativar barbeiro
    @PutMapping("/{id}/reativar")
    @Operation(summary = "Reativa barbeiro desativado")
    public ResponseEntity<?> reativar(@PathVariable Long id) {

        Barbeiro b = barbeiroRepository.findById(id).orElse(null);

        if (b == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Barbeiro não encontrado: " + id);
        }

        b.setAtivo(true);
        barbeiroRepository.save(b);

        return ResponseEntity.ok("Barbeiro reativado: " + id);
    }
}

// =================================================================
// AGENDAMENTO CONTROLLER (SEM LOMBOK)
// =================================================================

@RestController
@RequestMapping("/agendamentos")
@Tag(name = "Agendamentos", description = "CRUD de agendamentos")
class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo agendamento")
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agendamentoService.criar(request));
    }

    @GetMapping
    @Operation(summary = "Lista todos os agendamentos")
    public ResponseEntity<List<AgendamentoResponse>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Lista agendamentos por cliente")
    public ResponseEntity<List<AgendamentoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorCliente(clienteId));
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    @Operation(summary = "Lista agendamentos por barbeiro")
    public ResponseEntity<List<AgendamentoResponse>> listarPorBarbeiro(@PathVariable Long barbeiroId) {
        return ResponseEntity.ok(agendamentoService.listarPorBarbeiro(barbeiroId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza agendamento (dataHora/status/observacao)")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id,
                                                         @RequestBody AgendamentoUpdateRequest request) {
        return ResponseEntity.ok(agendamentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}/cancelar")
    @Operation(summary = "Cancela um agendamento")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        agendamentoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}

// =================================================================
// PAGAMENTO CONTROLLER (SEM LOMBOK) - PARA APARECER NO SWAGGER
// =================================================================

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Pagamento de agendamentos e relatório financeiro")
class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @Operation(summary = "Paga um agendamento (e marca o agendamento como CONCLUIDO)")
    public ResponseEntity<PagamentoResponse> pagar(@Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.pagar(request));
    }

    @GetMapping("/relatorio")
    @Operation(summary = "Relatório financeiro por período (dataInicio e dataFim)")
    public ResponseEntity<RelatorioFinanceiroResponse> relatorio(
            @RequestParam("dataInicio") String dataInicio,
            @RequestParam("dataFim") String dataFim
    ) {
        LocalDate ini = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);
        return ResponseEntity.ok(pagamentoService.relatorio(ini, fim));
    }
}