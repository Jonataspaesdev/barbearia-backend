package com.barbearia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Serviço.
 * Representa os serviços oferecidos pela barbearia (corte, barba, etc.).
 * Possui duração em minutos usada para calcular conflitos de agenda.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Double preco;

    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    @Column(nullable = false)
    private boolean ativo = true;

    // ✅ evita erro 500 (proxy/loop) ao listar serviços no Swagger
    @JsonIgnore
    @ManyToMany(mappedBy = "servicos", fetch = FetchType.LAZY)
    private List<Barbeiro> barbeiros = new ArrayList<>();

    public Servico() {}

    // =====================
    // GETTERS
    // =====================

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Double getPreco() { return preco; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public boolean isAtivo() { return ativo; }
    public List<Barbeiro> getBarbeiros() { return barbeiros; }

    // =====================
    // SETTERS
    // =====================

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(Double preco) { this.preco = preco; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void setBarbeiros(List<Barbeiro> barbeiros) { this.barbeiros = barbeiros; }
}