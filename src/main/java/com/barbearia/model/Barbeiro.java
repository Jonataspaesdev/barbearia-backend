package com.barbearia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "barbeiros")
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "hora_entrada", nullable = false)
    private LocalTime horaEntrada = LocalTime.of(8, 0);

    @Column(name = "hora_saida", nullable = false)
    private LocalTime horaSaida = LocalTime.of(18, 0);

    @Column(nullable = false)
    private boolean ativo = true;

    /**
     * Serviços que o barbeiro é capaz de realizar.
     */
    @JsonIgnoreProperties({"barbeiros"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "barbeiros_servicos",
            joinColumns = @JoinColumn(name = "barbeiro_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "barbeiro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Agendamento> agendamentos = new ArrayList<>();

    public Barbeiro() {
    }

    // =====================
    // GETTERS
    // =====================

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public LocalTime getHoraEntrada() { return horaEntrada; }
    public LocalTime getHoraSaida() { return horaSaida; }
    public boolean isAtivo() { return ativo; }
    public List<Servico> getServicos() { return servicos; }
    public List<Agendamento> getAgendamentos() { return agendamentos; }

    // =====================
    // SETTERS
    // =====================

    public void setId(Long id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email) { this.email = email; }
    public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }
    public void setHoraSaida(LocalTime horaSaida) { this.horaSaida = horaSaida; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void setServicos(List<Servico> servicos) { this.servicos = servicos; }
    public void setAgendamentos(List<Agendamento> agendamentos) { this.agendamentos = agendamentos; }
}