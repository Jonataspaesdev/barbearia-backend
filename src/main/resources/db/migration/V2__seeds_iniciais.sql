-- ============================================================
-- V2__seeds_iniciais.sql
-- Dados iniciais: admin + serviços padrão de barbearia
-- Senha do admin: admin123 (BCrypt)
-- ============================================================

-- USUÁRIO ADMIN PADRÃO
INSERT INTO usuarios (nome, email, senha, role)
VALUES (
    'Administrador',
    'admin@barbearia.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lSF6', -- admin123
    'ADMIN'
);

-- SERVIÇOS PADRÃO DA BARBEARIA
INSERT INTO servicos (nome, descricao, preco, duracao_minutos) VALUES
    ('Corte Simples',    'Corte de cabelo masculino clássico',          25.00, 30),
    ('Corte + Barba',    'Corte de cabelo com aparação de barba',        45.00, 60),
    ('Barba',            'Aparação e alinhamento de barba com navalha', 30.00, 30),
    ('Corte Degradê',    'Degradê moderno com acabamento perfeito',     35.00, 45),
    ('Hidratação',       'Hidratação capilar profissional',             40.00, 40),
    ('Sobrancelha',      'Design e alinhamento de sobrancelha',         15.00, 15),
    ('Pigmentação',      'Coloração e pigmentação de barba',            50.00, 60),
    ('Corte Infantil',   'Corte especial para crianças até 12 anos',    20.00, 25);
