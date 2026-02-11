package com.barbearia.service;

import com.barbearia.dto.DTOs.*;
import com.barbearia.exception.BusinessException;
import com.barbearia.exception.ResourceNotFoundException;
import com.barbearia.model.*;
import com.barbearia.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

// =================================================================
// BARBEIRO SERVICE (SEM LOMBOK + DTOs ATUAIS)
// =================================================================

@Service
@Transactional
class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;
    private final PasswordEncoder passwordEncoder;

    public BarbeiroService(BarbeiroRepository barbeiroRepository,
                           UsuarioRepository usuarioRepository,
                           ServicoRepository servicoRepository,
                           PasswordEncoder passwordEncoder) {
        this.barbeiroRepository = barbeiroRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public BarbeiroResponse criar(BarbeiroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + request.getEmail());
        }

        validarHorario(request);

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode("123456"));
        usuario.setRole("BARBEIRO");
        usuarioRepository.save(usuario);

        List<Servico> servicos = (request.getServicoIds() != null && !request.getServicoIds().isEmpty())
                ? servicoRepository.findAllById(request.getServicoIds())
                : List.of();

        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setUsuario(usuario);
        barbeiro.setNome(request.getNome());
        barbeiro.setEmail(request.getEmail());
        barbeiro.setTelefone(request.getTelefone());
        if (request.getHoraEntrada() != null) barbeiro.setHoraEntrada(request.getHoraEntrada());
        if (request.getHoraSaida() != null) barbeiro.setHoraSaida(request.getHoraSaida());
        barbeiro.setServicos(servicos);

        return toResponse(barbeiroRepository.save(barbeiro));
    }

    @Transactional(readOnly = true)
    public List<BarbeiroResponse> listarTodos() {
        return barbeiroRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BarbeiroResponse buscarPorId(Long id) {
        return toResponse(getById(id));
    }

    public BarbeiroResponse atualizar(Long id, BarbeiroRequest request) {
        Barbeiro barbeiro = getById(id);
        validarHorario(request);

        if (request.getNome() != null) barbeiro.setNome(request.getNome());
        if (request.getTelefone() != null) barbeiro.setTelefone(request.getTelefone());
        if (request.getHoraEntrada() != null) barbeiro.setHoraEntrada(request.getHoraEntrada());
        if (request.getHoraSaida() != null) barbeiro.setHoraSaida(request.getHoraSaida());

        if (request.getServicoIds() != null) {
            barbeiro.setServicos(servicoRepository.findAllById(request.getServicoIds()));
        }

        return toResponse(barbeiroRepository.save(barbeiro));
    }

    private void validarHorario(BarbeiroRequest request) {
        if (request.getHoraEntrada() != null && request.getHoraSaida() != null
                && !request.getHoraEntrada().isBefore(request.getHoraSaida())) {
            throw new BusinessException("Hora de entrada deve ser antes da hora de saída.");
        }
    }

    private Barbeiro getById(Long id) {
        return barbeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado: " + id));
    }

    private BarbeiroResponse toResponse(Barbeiro b) {
        BarbeiroResponse r = new BarbeiroResponse();
        r.setId(b.getId());
        r.setNome(b.getNome());
        r.setEmail(b.getEmail());
        r.setTelefone(b.getTelefone());
        return r;
    }
}

// =================================================================
// SERVIÇO SERVICE (SEM BUILDER, ALINHADO COM DTOs)
// =================================================================

@Service
@Transactional
class ServicoServiceImpl {

    private final ServicoRepository servicoRepository;

    public ServicoServiceImpl(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public ServicoResponse criar(ServicoRequest request) {
        if (servicoRepository.existsByNome(request.getNome())) {
            throw new BusinessException("Serviço com nome '" + request.getNome() + "' já existe.");
        }

        Servico servico = new Servico();
        servico.setNome(request.getNome());
        servico.setPreco(request.getPreco());
        servico.setDuracaoMinutos(request.getDuracaoMinutos());
        if (request.getAtivo() != null) servico.setAtivo(request.getAtivo());

        return toResponse(servicoRepository.save(servico));
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listarAtivos() {
        return servicoRepository.findAll().stream()
                .filter(Servico::isAtivo)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listarTodos() {
        return servicoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ServicoResponse atualizar(Long id, ServicoRequest request) {
        Servico servico = getById(id);

        if (request.getNome() != null) servico.setNome(request.getNome());
        if (request.getPreco() != null) servico.setPreco(request.getPreco());
        if (request.getDuracaoMinutos() != null) servico.setDuracaoMinutos(request.getDuracaoMinutos());
        if (request.getAtivo() != null) servico.setAtivo(request.getAtivo());

        return toResponse(servicoRepository.save(servico));
    }

    public void inativar(Long id) {
        Servico servico = getById(id);
        servico.setAtivo(false);
        servicoRepository.save(servico);
    }

    private Servico getById(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + id));
    }

    private ServicoResponse toResponse(Servico s) {
        ServicoResponse r = new ServicoResponse();
        r.setId(s.getId());
        r.setNome(s.getNome());
        r.setPreco(s.getPreco());
        r.setDuracaoMinutos(s.getDuracaoMinutos());
        r.setAtivo(s.isAtivo());
        return r;
    }
}

// =================================================================
// FINANCEIRO SERVICE (SIMPLIFICADO PARA COMPILAR COM DTOs ATUAIS)
// =================================================================

@Service
@Transactional
class FinanceiroService {

    private final PagamentoRepository pagamentoRepository;

    public FinanceiroService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public PagamentoResponse registrarPagamento(PagamentoRequest request) {
        PagamentoResponse r = new PagamentoResponse();
        r.setAgendamentoId(request.getAgendamentoId());
        r.setValor(request.getValor());
        r.setFormaPagamento(request.getFormaPagamento());
        r.setPagoEm(LocalDateTime.now());
        return r;
    }

    @Transactional(readOnly = true)
    public RelatorioFinanceiroResponse relatorioDiario(LocalDate data) {
        RelatorioFinanceiroResponse r = new RelatorioFinanceiroResponse();
        r.setDataInicio(data);
        r.setDataFim(data);
        r.setTotal(0.0);
        r.setQuantidadePagamentos(0);
        return r;
    }

    @Transactional(readOnly = true)
    public RelatorioFinanceiroResponse relatorioMensal(int ano, int mes) {
        YearMonth ym = YearMonth.of(ano, mes);
        RelatorioFinanceiroResponse r = new RelatorioFinanceiroResponse();
        r.setDataInicio(ym.atDay(1));
        r.setDataFim(ym.atEndOfMonth());
        r.setTotal(0.0);
        r.setQuantidadePagamentos(0);
        return r;
    }
}