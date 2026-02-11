package com.barbearia.repository;

import com.barbearia.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Lista por cliente
    List<Agendamento> findByClienteId(Long clienteId);

    // Lista por barbeiro
    List<Agendamento> findByBarbeiroId(Long barbeiroId);

    // Conflitos de horário: existe agendamento do barbeiro que sobrepõe o intervalo?
    @Query("""
        select a from Agendamento a
        where a.barbeiro.id = :barbeiroId
          and a.status = 'AGENDADO'
          and a.dataHora < :fim
          and timestampadd(minute, a.servico.duracaoMinutos, a.dataHora) > :inicio
    """)
    List<Agendamento> findConflitos(
            @Param("barbeiroId") Long barbeiroId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    // Agendamentos do barbeiro dentro do dia (intervalo)
    @Query("""
        select a from Agendamento a
        where a.barbeiro.id = :barbeiroId
          and a.dataHora >= :inicioDia
          and a.dataHora < :fimDia
    """)
    List<Agendamento> findByBarbeiroAndDia(
            @Param("barbeiroId") Long barbeiroId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("fimDia") LocalDateTime fimDia
    );
}