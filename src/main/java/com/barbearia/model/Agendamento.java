package com.barbearia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    public enum StatusAgendamento {
        AGENDADO,
        CANCELADO,
        CONCLUIDO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    @Column(length = 255)
    private String observacao;

    public Agendamento() {
    }

    // =====================
    // GETTERS
    // =====================

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Barbeiro getBarbeiro() {
        return barbeiro;
    }

    public Servico getServico() {
        return servico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public String getObservacao() {
        return observacao;
    }

    /**
     * Calcula a data/hora final baseado na duração do serviço.
     * Não persiste no banco.
     */
    @Transient
    public LocalDateTime getDataHoraFim() {
        if (dataHora == null || servico == null || servico.getDuracaoMinutos() == null) {
            return null;
        }
        return dataHora.plusMinutes(servico.getDuracaoMinutos());
    }

    // =====================
    // SETTERS
    // =====================

    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setBarbeiro(Barbeiro barbeiro) {
        this.barbeiro = barbeiro;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    /**
     * Se seu Service estiver passando String (ex: "CANCELADO"),
     * este setter ajuda a compilar e converter.
     */
    public void setStatus(String status) {
        if (status == null) {
            return;
        }
        this.status = StatusAgendamento.valueOf(status.trim().toUpperCase());
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
