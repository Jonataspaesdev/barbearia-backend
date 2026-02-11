package com.barbearia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DTOs {

    // ==========================
    // AUTH
    // ==========================

    public static class LoginRequest {

        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String senha;

        public LoginRequest() {}

        public String getEmail() { return email; }
        public String getSenha() { return senha; }

        public void setEmail(String email) { this.email = email; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    public static class LoginResponse {

        private String token;
        private String email;
        private String nome;
        private String role;

        public LoginResponse() {}

        public String getToken() { return token; }
        public String getEmail() { return email; }
        public String getNome() { return nome; }
        public String getRole() { return role; }

        public void setToken(String token) { this.token = token; }
        public void setEmail(String email) { this.email = email; }
        public void setNome(String nome) { this.nome = nome; }
        public void setRole(String role) { this.role = role; }
    }

    // ==========================
    // CLIENTE
    // ==========================

    public static class ClienteRequest {

        private String nome;
        private String email;
        private String telefone;

        public ClienteRequest() {}

        public String getNome() { return nome; }
        public String getEmail() { return email; }
        public String getTelefone() { return telefone; }

        public void setNome(String nome) { this.nome = nome; }
        public void setEmail(String email) { this.email = email; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
    }

    // ==========================
    // BARBEIRO
    // ==========================

    public static class BarbeiroRequest {

        private String nome;
        private String email;
        private String telefone;

        private LocalTime horaEntrada;
        private LocalTime horaSaida;

        private List<Long> servicoIds;

        public BarbeiroRequest() {}

        public String getNome() { return nome; }
        public String getEmail() { return email; }
        public String getTelefone() { return telefone; }
        public LocalTime getHoraEntrada() { return horaEntrada; }
        public LocalTime getHoraSaida() { return horaSaida; }
        public List<Long> getServicoIds() { return servicoIds; }

        public void setNome(String nome) { this.nome = nome; }
        public void setEmail(String email) { this.email = email; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }
        public void setHoraSaida(LocalTime horaSaida) { this.horaSaida = horaSaida; }
        public void setServicoIds(List<Long> servicoIds) { this.servicoIds = servicoIds; }
    }

    public static class BarbeiroResponse {

        private Long id;
        private String nome;
        private String email;
        private String telefone;

        public BarbeiroResponse() {}

        public Long getId() { return id; }
        public String getNome() { return nome; }
        public String getEmail() { return email; }
        public String getTelefone() { return telefone; }

        public void setId(Long id) { this.id = id; }
        public void setNome(String nome) { this.nome = nome; }
        public void setEmail(String email) { this.email = email; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
    }

    // ==========================
    // SERVIÇO
    // ==========================

    public static class ServicoRequest {

        @NotBlank
        private String nome;

        @NotNull
        private Double preco;

        @NotNull
        private Integer duracaoMinutos;

        private Boolean ativo;

        public ServicoRequest() {}

        public String getNome() { return nome; }
        public Double getPreco() { return preco; }
        public Integer getDuracaoMinutos() { return duracaoMinutos; }
        public Boolean getAtivo() { return ativo; }

        public void setNome(String nome) { this.nome = nome; }
        public void setPreco(Double preco) { this.preco = preco; }
        public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
        public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    }

    public static class ServicoResponse {

        private Long id;
        private String nome;
        private Double preco;
        private Integer duracaoMinutos;
        private Boolean ativo;

        public ServicoResponse() {}

        public Long getId() { return id; }
        public String getNome() { return nome; }
        public Double getPreco() { return preco; }
        public Integer getDuracaoMinutos() { return duracaoMinutos; }
        public Boolean getAtivo() { return ativo; }

        public void setId(Long id) { this.id = id; }
        public void setNome(String nome) { this.nome = nome; }
        public void setPreco(Double preco) { this.preco = preco; }
        public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
        public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    }

    // ==========================
    // AGENDAMENTO (REQUESTS)
    // ==========================

    public static class AgendamentoRequest {

        @NotNull
        private Long clienteId;

        @NotNull
        private Long barbeiroId;

        @NotNull
        private Long servicoId;

        @NotNull
        private LocalDateTime dataHora;

        private String observacao;

        public AgendamentoRequest() {}

        public Long getClienteId() { return clienteId; }
        public Long getBarbeiroId() { return barbeiroId; }
        public Long getServicoId() { return servicoId; }
        public LocalDateTime getDataHora() { return dataHora; }
        public String getObservacao() { return observacao; }

        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
        public void setBarbeiroId(Long barbeiroId) { this.barbeiroId = barbeiroId; }
        public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
        public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
        public void setObservacao(String observacao) { this.observacao = observacao; }
    }

    public static class AgendamentoUpdateRequest {

        private LocalDateTime dataHora;
        private String status;
        private String observacao;

        public AgendamentoUpdateRequest() {}

        public LocalDateTime getDataHora() { return dataHora; }
        public String getStatus() { return status; }
        public String getObservacao() { return observacao; }

        public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
        public void setStatus(String status) { this.status = status; }
        public void setObservacao(String observacao) { this.observacao = observacao; }
    }

    // ==========================
    // AGENDAMENTO (RESPONSE)
    // ==========================

    public static class AgendamentoResponse {

        private Long id;

        private Long clienteId;
        private String clienteNome;

        private Long barbeiroId;
        private String barbeiroNome;

        private Long servicoId;
        private String servicoNome;

        private Double preco;

        private LocalDateTime dataHora;
        private LocalDateTime dataHoraFim;

        private String status;
        private String observacao;

        public AgendamentoResponse() {}

        public Long getId() { return id; }

        public Long getClienteId() { return clienteId; }
        public String getClienteNome() { return clienteNome; }

        public Long getBarbeiroId() { return barbeiroId; }
        public String getBarbeiroNome() { return barbeiroNome; }

        public Long getServicoId() { return servicoId; }
        public String getServicoNome() { return servicoNome; }

        public Double getPreco() { return preco; }

        public LocalDateTime getDataHora() { return dataHora; }
        public LocalDateTime getDataHoraFim() { return dataHoraFim; }

        public String getStatus() { return status; }
        public String getObservacao() { return observacao; }

        public void setId(Long id) { this.id = id; }

        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
        public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

        public void setBarbeiroId(Long barbeiroId) { this.barbeiroId = barbeiroId; }
        public void setBarbeiroNome(String barbeiroNome) { this.barbeiroNome = barbeiroNome; }

        public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
        public void setServicoNome(String servicoNome) { this.servicoNome = servicoNome; }

        public void setPreco(Double preco) { this.preco = preco; }

        public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
        public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }

        public void setStatus(String status) { this.status = status; }
        public void setObservacao(String observacao) { this.observacao = observacao; }

        // --- Aliases para compatibilidade com código antigo ---
        public void setNomeCliente(String nomeCliente) { this.clienteNome = nomeCliente; }
        public void setNomeBarbeiro(String nomeBarbeiro) { this.barbeiroNome = nomeBarbeiro; }
        public void setNomeServico(String nomeServico) { this.servicoNome = nomeServico; }
    }

    public static class HorariosDisponiveisResponse {

        private Long barbeiroId;
        private Long servicoId;
        private LocalDate data;
        private List<String> horarios;

        public HorariosDisponiveisResponse() {}

        public Long getBarbeiroId() { return barbeiroId; }
        public Long getServicoId() { return servicoId; }
        public LocalDate getData() { return data; }
        public List<String> getHorarios() { return horarios; }

        public void setBarbeiroId(Long barbeiroId) { this.barbeiroId = barbeiroId; }
        public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
        public void setData(LocalDate data) { this.data = data; }
        public void setHorarios(List<String> horarios) { this.horarios = horarios; }
    }

    // ==========================
    // FINANCEIRO
    // ==========================

    public static class PagamentoRequest {

        @NotNull
        private Long agendamentoId;

        @NotNull
        private Double valor;

        @NotBlank
        private String formaPagamento;

        public PagamentoRequest() {}

        public Long getAgendamentoId() { return agendamentoId; }
        public Double getValor() { return valor; }
        public String getFormaPagamento() { return formaPagamento; }

        public void setAgendamentoId(Long agendamentoId) { this.agendamentoId = agendamentoId; }
        public void setValor(Double valor) { this.valor = valor; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    }

    public static class PagamentoResponse {

        private Long id;
        private Long agendamentoId;
        private Double valor;
        private String formaPagamento;
        private LocalDateTime pagoEm;

        public PagamentoResponse() {}

        public Long getId() { return id; }
        public Long getAgendamentoId() { return agendamentoId; }
        public Double getValor() { return valor; }
        public String getFormaPagamento() { return formaPagamento; }
        public LocalDateTime getPagoEm() { return pagoEm; }

        public void setId(Long id) { this.id = id; }
        public void setAgendamentoId(Long agendamentoId) { this.agendamentoId = agendamentoId; }
        public void setValor(Double valor) { this.valor = valor; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        public void setPagoEm(LocalDateTime pagoEm) { this.pagoEm = pagoEm; }
    }

    public static class RelatorioFinanceiroResponse {

        private LocalDate dataInicio;
        private LocalDate dataFim;
        private Double total;
        private Integer quantidadePagamentos;

        public RelatorioFinanceiroResponse() {}

        public LocalDate getDataInicio() { return dataInicio; }
        public LocalDate getDataFim() { return dataFim; }
        public Double getTotal() { return total; }
        public Integer getQuantidadePagamentos() { return quantidadePagamentos; }

        public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
        public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
        public void setTotal(Double total) { this.total = total; }
        public void setQuantidadePagamentos(Integer quantidadePagamentos) { this.quantidadePagamentos = quantidadePagamentos; }
    }
}
