package com.barbearia.service;

import com.barbearia.dto.DTOs.*;
import com.barbearia.exception.BusinessException;
import com.barbearia.exception.ResourceNotFoundException;
import com.barbearia.model.*;
import com.barbearia.model.Agendamento.StatusAgendamento;
import com.barbearia.repository.AgendamentoRepository;
import com.barbearia.repository.BarbeiroRepository;
import com.barbearia.repository.ClienteRepository;
import com.barbearia.repository.ServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AgendamentoService {

    private static final Logger log = LoggerFactory.getLogger(AgendamentoService.class);

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              ClienteRepository clienteRepository,
                              BarbeiroRepository barbeiroRepository,
                              ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
    }

    public AgendamentoResponse criar(AgendamentoRequest request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + request.getClienteId()));

        Barbeiro barbeiro = barbeiroRepository.findById(request.getBarbeiroId())
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado: " + request.getBarbeiroId()));

        Servico servico = servicoRepository.findById(request.getServicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + request.getServicoId()));

        LocalDateTime inicio = request.getDataHora();
        if (inicio == null) throw new BusinessException("dataHora é obrigatório.");

        LocalDateTime fim = inicio.plusMinutes(servico.getDuracaoMinutos());

        if (inicio.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é permitido agendar em data passada.");
        }

        validarHorarioTrabalho(barbeiro, inicio, fim);
        validarConflito(barbeiro.getId(), null, inicio, fim);

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setDataHora(inicio);
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setObservacao(request.getObservacao());

        agendamento = agendamentoRepository.save(agendamento);
        return toResponse(agendamento);
    }

    public AgendamentoResponse atualizar(Long id, AgendamentoUpdateRequest request) {

        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO)
            throw new BusinessException("Não é possível atualizar um agendamento cancelado.");

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO)
            throw new BusinessException("Não é possível atualizar um agendamento concluído.");

        if (request.getDataHora() != null) {

            LocalDateTime novoInicio = request.getDataHora();
            LocalDateTime novoFim = novoInicio.plusMinutes(agendamento.getServico().getDuracaoMinutos());

            if (novoInicio.isBefore(LocalDateTime.now()))
                throw new BusinessException("Não é permitido remarcar para data passada.");

            validarHorarioTrabalho(agendamento.getBarbeiro(), novoInicio, novoFim);
            validarConflito(agendamento.getBarbeiro().getId(), id, novoInicio, novoFim);

            agendamento.setDataHora(novoInicio);
        }

        if (request.getStatus() != null && !request.getStatus().isBlank())
            agendamento.setStatus(request.getStatus());

        if (request.getObservacao() != null)
            agendamento.setObservacao(request.getObservacao());

        return toResponse(agendamentoRepository.save(agendamento));
    }

    public void cancelar(Long id) {

        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO)
            throw new BusinessException("Não é possível cancelar um agendamento já concluído.");

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO)
            throw new BusinessException("Agendamento já está cancelado.");

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarTodos() {
        return agendamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorBarbeiro(Long barbeiroId) {
        return agendamentoRepository.findByBarbeiroId(barbeiroId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void validarHorarioTrabalho(Barbeiro barbeiro, LocalDateTime inicio, LocalDateTime fim) {

        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFim = fim.toLocalTime();

        if (horaInicio.isBefore(barbeiro.getHoraEntrada())
                || horaFim.isAfter(barbeiro.getHoraSaida())
                || horaInicio.equals(barbeiro.getHoraSaida())) {

            throw new BusinessException("Horário fora do expediente do barbeiro.");
        }
    }

    private void validarConflito(Long barbeiroId, Long agendamentoIdExcluir,
                                 LocalDateTime inicio, LocalDateTime fim) {

        LocalDate dia = inicio.toLocalDate();

        List<Agendamento> agendamentosDoDia = agendamentoRepository.findByBarbeiroAndDia(
                barbeiroId,
                dia.atStartOfDay(),
                dia.plusDays(1).atStartOfDay()
        );

        boolean temConflito = agendamentosDoDia.stream()
                .filter(a -> agendamentoIdExcluir == null || !a.getId().equals(agendamentoIdExcluir))
                .anyMatch(a -> {
                    LocalDateTime aInicio = a.getDataHora();
                    LocalDateTime aFim = a.getDataHoraFim();
                    return aInicio != null && aFim != null
                            && inicio.isBefore(aFim)
                            && fim.isAfter(aInicio);
                });

        if (temConflito)
            throw new BusinessException("Já existe um agendamento neste horário.");
    }

    private Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado: " + id));
    }

    private AgendamentoResponse toResponse(Agendamento a) {

        AgendamentoResponse r = new AgendamentoResponse();

        r.setId(a.getId());
        r.setClienteId(a.getCliente().getId());
        r.setNomeCliente(a.getCliente().getNome());
        r.setBarbeiroId(a.getBarbeiro().getId());
        r.setNomeBarbeiro(a.getBarbeiro().getNome());
        r.setServicoId(a.getServico().getId());
        r.setNomeServico(a.getServico().getNome());
        r.setPreco(a.getServico().getPreco());
        r.setDataHora(a.getDataHora());
        r.setDataHoraFim(a.getDataHoraFim());
        r.setStatus(a.getStatus() != null ? a.getStatus().name() : null);
        r.setObservacao(a.getObservacao());

        return r;
    }
}
