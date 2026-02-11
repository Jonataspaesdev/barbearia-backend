-- ============================================================
-- V1__criar_tabelas.sql
-- Migration inicial: criação de todas as tabelas do sistema
-- ============================================================

-- USUARIOS
CREATE TABLE usuarios (
    id          BIGSERIAL       PRIMARY KEY,
    nome        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    senha       VARCHAR(255)    NOT NULL,
    role        VARCHAR(20)     NOT NULL CHECK (role IN ('ADMIN', 'BARBEIRO', 'CLIENTE')),
    ativo       BOOLEAN         NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- CLIENTES
CREATE TABLE clientes (
    id          BIGSERIAL       PRIMARY KEY,
    usuario_id  BIGINT          NOT NULL UNIQUE REFERENCES usuarios(id),
    nome        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    telefone    VARCHAR(20)
);

-- BARBEIROS
CREATE TABLE barbeiros (
    id          BIGSERIAL       PRIMARY KEY,
    usuario_id  BIGINT          NOT NULL UNIQUE REFERENCES usuarios(id),
    nome        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    telefone    VARCHAR(20),
    hora_entrada TIME            NOT NULL DEFAULT '08:00',
    hora_saida   TIME            NOT NULL DEFAULT '18:00',
    ativo       BOOLEAN         NOT NULL DEFAULT TRUE
);

-- SERVICOS
CREATE TABLE servicos (
    id                BIGSERIAL       PRIMARY KEY,
    nome              VARCHAR(100)    NOT NULL UNIQUE,
    descricao         VARCHAR(255),
    preco             DECIMAL(10,2)   NOT NULL,
    duracao_minutos   INT             NOT NULL,
    ativo             BOOLEAN         NOT NULL DEFAULT TRUE
);

-- BARBEIROS_SERVICOS (tabela de junção ManyToMany)
CREATE TABLE barbeiros_servicos (
    barbeiro_id BIGINT NOT NULL REFERENCES barbeiros(id),
    servico_id  BIGINT NOT NULL REFERENCES servicos(id),
    PRIMARY KEY (barbeiro_id, servico_id)
);

-- AGENDAMENTOS
CREATE TABLE agendamentos (
    id           BIGSERIAL       PRIMARY KEY,
    cliente_id   BIGINT          NOT NULL REFERENCES clientes(id),
    barbeiro_id  BIGINT          NOT NULL REFERENCES barbeiros(id),
    servico_id   BIGINT          NOT NULL REFERENCES servicos(id),
    data_hora    TIMESTAMP       NOT NULL,
    status       VARCHAR(20)     NOT NULL DEFAULT 'AGENDADO'
                                 CHECK (status IN ('AGENDADO','CONFIRMADO','CONCLUIDO','CANCELADO')),
    observacao   VARCHAR(500),
    criado_em    TIMESTAMP       NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP
);

-- Índice para performance na busca de conflitos
CREATE INDEX idx_agendamentos_barbeiro_data ON agendamentos(barbeiro_id, data_hora);
CREATE INDEX idx_agendamentos_cliente ON agendamentos(cliente_id);

-- PAGAMENTOS
CREATE TABLE pagamentos (
    id              BIGSERIAL       PRIMARY KEY,
    agendamento_id  BIGINT          NOT NULL UNIQUE REFERENCES agendamentos(id),
    valor_cobrado   DECIMAL(10,2)   NOT NULL,
    forma_pagamento VARCHAR(30)     NOT NULL
                    CHECK (forma_pagamento IN ('DINHEIRO','CARTAO_CREDITO','CARTAO_DEBITO','PIX')),
    data_pagamento  TIMESTAMP       NOT NULL DEFAULT NOW(),
    observacao      VARCHAR(255)
);

CREATE INDEX idx_pagamentos_data ON pagamentos(data_pagamento);
