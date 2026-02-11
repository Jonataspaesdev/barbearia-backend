package com.barbearia.repository;

import com.barbearia.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    boolean existsByAgendamentoId(Long agendamentoId);
}