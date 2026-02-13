package com.barbearia.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.dto.DTOs.PagamentoRequest;
import com.barbearia.dto.DTOs.PagamentoResponse;
import com.barbearia.dto.DTOs.RelatorioFinanceiroResponse;
import com.barbearia.exception.BusinessException;
import com.barbearia.exception.ResourceNotFoundException;
import com.barbearia.model.Agendamento;
import com.barbearia.model.Agendamento.StatusAgendamento;
import com.barbearia.model.Pagamento;
import com.barbearia.repository.AgendamentoRepository;
import com.barbearia.repository.PagamentoRepository;

@Service
@Transactional
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                            AgendamentoRepository agendamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.agendamentoRepository = agendamentoRepository;
    }

    public PagamentoResponse pagar(PagamentoRequest request) {

        if (request.getAgendamentoId() == null)
            throw new BusinessException("agendamentoId é obrigatório.");

        if (request.getValor() == null || request.getValor() <= 0)
            throw new BusinessException("valor precisa ser maior que 0.");

        if (request.getFormaPagamento() == null || request.getFormaPagamento().isBlank())
            throw new BusinessException("formaPagamento é obrigatório.");

        // 1) agendamento existe?
        Agendamento agendamento = agendamentoRepository.findById(request.getAgendamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado: " + request.getAgendamentoId()));

        // 2) não pagar cancelado/concluído
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO)
            throw new BusinessException("Não é possível pagar um agendamento cancelado.");

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO)
            throw new BusinessException("Este agendamento já está CONCLUÍDO.");

        // 3) já pago?
        if (pagamentoRepository.existsByAgendamentoId(agendamento.getId()))
            throw new BusinessException("Este agendamento já foi pago.");

        // 4) converter String do DTO -> enum que está DENTRO do Pagamento
        Pagamento.FormaPagamento forma;
        try {
            forma = Pagamento.FormaPagamento.valueOf(request.getFormaPagamento().trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Forma de pagamento inválida. Use: DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX");
        }

        BigDecimal valorCobrado = BigDecimal.valueOf(request.getValor());

        Pagamento pagamento = Pagamento.builder()
                .agendamento(agendamento)
                .valorCobrado(valorCobrado)
                .formaPagamento(forma)
                .dataPagamento(LocalDateTime.now())
                .build();

        pagamento = pagamentoRepository.save(pagamento);

        // 5) ao pagar, agendamento vira CONCLUIDO
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamentoRepository.save(agendamento);

        return toResponse(pagamento);
    }

    @Transactional(readOnly = true)
    public RelatorioFinanceiroResponse relatorio(LocalDate dataInicio, LocalDate dataFim) {

        if (dataInicio == null || dataFim == null)
            throw new BusinessException("dataInicio e dataFim são obrigatórios.");

        if (dataFim.isBefore(dataInicio))
            throw new BusinessException("dataFim não pode ser antes de dataInicio.");

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.plusDays(1).atStartOfDay(); // fim inclusivo

        BigDecimal total = pagamentoRepository.somarTotalPorPeriodo(inicio, fim);
        long quantidade = pagamentoRepository.contarPorPeriodo(inicio, fim);

        RelatorioFinanceiroResponse r = new RelatorioFinanceiroResponse();
        r.setDataInicio(dataInicio);
        r.setDataFim(dataFim);
        r.setTotal(total.doubleValue());
        r.setQuantidadePagamentos((int) quantidade);

        return r;
    }

    private PagamentoResponse toResponse(Pagamento p) {
        PagamentoResponse r = new PagamentoResponse();
        r.setId(p.getId());
        r.setAgendamentoId(p.getAgendamento().getId());
        r.setValor(p.getValorCobrado().doubleValue());
        r.setFormaPagamento(p.getFormaPagamento().name());
        r.setPagoEm(p.getDataPagamento());
        return r;
    }
}