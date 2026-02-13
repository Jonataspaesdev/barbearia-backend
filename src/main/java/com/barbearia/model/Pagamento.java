package com.barbearia.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Pagamento.
 * Registra o valor cobrado, forma de pagamento e data de cada atendimento concluído.
 * Vinculada ao Agendamento (OneToOne) para garantir 1 pagamento por agendamento.
 */
@Entity
@Table(
        name = "pagamentos",
        uniqueConstraints = @UniqueConstraint(name = "uk_pagamento_agendamento", columnNames = "agendamento_id")
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1 pagamento para 1 agendamento
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

    @Column(name = "valor_cobrado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorCobrado;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 30)
    private FormaPagamento formaPagamento;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @Column(length = 255)
    private String observacao;

    @PrePersist
    protected void prePersist() {
        if (dataPagamento == null) dataPagamento = LocalDateTime.now();
    }

    // ============================================================
    // Enum de formas de pagamento (mantido aqui, do jeito que você pediu)
    // ============================================================
    public enum FormaPagamento {
        DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX
    }
}