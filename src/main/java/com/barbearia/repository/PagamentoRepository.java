package com.barbearia.repository;

import com.barbearia.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    boolean existsByAgendamentoId(Long agendamentoId);

    @Query("select coalesce(sum(p.valorCobrado), 0) from Pagamento p " +
           "where p.dataPagamento >= :inicio and p.dataPagamento < :fim")
    java.math.BigDecimal somarTotalPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    @Query("select count(p) from Pagamento p " +
           "where p.dataPagamento >= :inicio and p.dataPagamento < :fim")
    long contarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}