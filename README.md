# 💈 Sistema de Barbearia - API REST

API REST desenvolvida em **Spring Boot 3** para gerenciamento completo de uma barbearia.

Projeto **Fullstack** com autenticação **JWT Stateless**, controle de acesso por **roles**, regras de negócio reais e integração com frontend em **React**.

---

## 🚀 Visão Geral

O sistema foi criado para simular um cenário real de uma barbearia, com fluxo completo de:

- cadastro de clientes
- gestão de serviços
- gestão de barbeiros
- agendamentos com validações reais
- disponibilidade dinâmica
- pagamentos
- relatório financeiro

A API foi desenvolvida com foco em:

- arquitetura REST profissional
- segurança com JWT
- separação por camadas
- regras de negócio reais
- integração com frontend moderno

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Security
- JWT (Autenticação Stateless)
- Spring Data JPA
- PostgreSQL
- Swagger / OpenAPI
- Maven

---

## 🔐 Autenticação JWT

A API utiliza autenticação via **JWT Token Stateless**.

Após realizar login, o token deve ser enviado nos endpoints protegidos no header:

```http
Authorization: Bearer SEU_TOKEN_AQUI
O sistema extrai o usuário autenticado e aplica o controle de acesso com base na role salva no banco.

👤 Cadastro de Cliente
Permite que o cliente crie sua própria conta no sistema.

Endpoint
POST /auth/register
Exemplo de requisição
{
  "nome": "Cliente Teste",
  "email": "cliente1@gmail.com",
  "telefone": "11999990000",
  "senha": "123456"
}
Exemplo de resposta
{
  "usuarioId": 10,
  "clienteId": 4,
  "nome": "Cliente Teste",
  "email": "cliente1@gmail.com",
  "role": "ROLE_CLIENTE"
}
Regras
Email não pode duplicar

Senha criptografada com BCrypt

Cria automaticamente:

Usuario com ROLE_CLIENTE

Cliente vinculado via relacionamento

🔑 Login
Endpoint
POST /auth/login
Exemplo de requisição
{
  "email": "admin@admin.com",
  "senha": "123456"
}
Exemplo de resposta
{
  "token": "SEU_TOKEN_AQUI",
  "email": "admin@admin.com",
  "nome": "Administrador",
  "role": "ADMIN"
}
Observação: a role retornada no login vem normalizada para facilitar o uso no frontend (ADMIN, CLIENTE, BARBEIRO).

🛡️ Como usar o token no Swagger
Faça login em /auth/login

Copie o campo token

Clique em Authorize

Cole:

Bearer SEU_TOKEN_AQUI
Clique em Authorize

Agora você poderá acessar os endpoints protegidos.

👤 Usuário Administrador Padrão
Criado automaticamente ao iniciar a aplicação:

Email: admin@admin.com

Senha: 123456

Role: ROLE_ADMIN

📌 Funcionalidades Implementadas
👥 Clientes
Apenas usuários com ROLE_ADMIN podem gerenciar clientes manualmente.

Endpoints
POST   /clientes
GET    /clientes
GET    /clientes/{id}
PUT    /clientes/{id}
DELETE /clientes/{id}
✂️ Serviços
Permite criar, listar, atualizar e desativar serviços.

Regras
Nome obrigatório

Nome não pode duplicar

Preço deve ser maior que 0

Duração deve ser maior que 0

Soft delete para não perder histórico

Endpoints
POST   /servicos        (ADMIN)
GET    /servicos        (Público)
GET    /servicos/{id}
PUT    /servicos/{id}   (ADMIN)
DELETE /servicos/{id}   (Soft delete - ADMIN)
💈 Barbeiros
Permite cadastrar barbeiros, configurar horário de trabalho e vincular serviços.

Ao criar um barbeiro, o sistema cria automaticamente um usuário com ROLE_BARBEIRO.

Funcionalidades
criar barbeiro

listar barbeiros ativos

atualizar dados

soft delete

reativar barbeiro

vincular serviços via servicoIds

exibir horaEntrada e horaSaida na resposta

Endpoints
POST   /barbeiros
GET    /barbeiros
GET    /barbeiros/{id}
PUT    /barbeiros/{id}
DELETE /barbeiros/{id}
PUT    /barbeiros/{id}/reativar
📅 Agendamentos
Permite criar, atualizar, listar e cancelar agendamentos.

Regras de negócio
Não permite agendar no passado

Não permite remarcar para data passada

Não permite agendar fora do expediente do barbeiro

Não permite conflito de horário

Calcula automaticamente dataHoraFim

Associa clienteId via usuário autenticado quando a role é CLIENTE

Status suportados:

AGENDADO

CANCELADO

CONCLUIDO

Melhorias aplicadas
normalização de data/hora para evitar erro com segundos e nanos

timezone ajustado para America/Sao_Paulo

maior estabilidade para agendamentos no mesmo dia

logs de apoio para debug em produção

Endpoints
POST   /agendamentos
GET    /agendamentos
GET    /agendamentos/cliente/{clienteId}
GET    /agendamentos/barbeiro/{barbeiroId}
PUT    /agendamentos/{id}
DELETE /agendamentos/{id}/cancelar
🕒 Disponibilidade Dinâmica
Permite ao frontend consultar os horários ocupados de um barbeiro em uma data específica.

Endpoint
GET /agendamentos/disponibilidade?barbeiroId=1&data=2026-02-23
Exemplo de resposta
{
  "barbeiroId": 1,
  "data": "2026-02-23",
  "duracaoMin": 30,
  "horaEntrada": "09:00:00",
  "horaSaida": "18:30:00",
  "ocupados": ["10:00", "10:30", "14:00"]
}
Regras
respeita o horário de trabalho do barbeiro

considera duração fixa de 30 minutos para os slots

ignora agendamentos cancelados

não expõe dados sensíveis

permite ao frontend bloquear horários ocupados de forma segura

💳 Pagamentos
Realiza o pagamento de um agendamento.

Regras
impede pagamento duplicado

ao pagar, o agendamento é marcado automaticamente como CONCLUIDO

Endpoint
POST /pagamentos
Exemplo de requisição
{
  "agendamentoId": 2,
  "valor": 35.0,
  "formaPagamento": "PIX"
}
📊 Relatório Financeiro
Retorna dados consolidados de pagamentos por período.

Endpoint
GET /pagamentos/relatorio?dataInicio=2026-02-01&dataFim=2026-02-28
Retorna
total faturado

quantidade de pagamentos

período consultado

🔒 Controle de Acesso
Públicos
/auth/**
GET /servicos
GET /barbeiros
Protegidos com JWT
clientes

agendamentos

pagamentos

serviços (exceto GET)

barbeiros (exceto GET)

🏷 Roles
Role	Permissões
ROLE_ADMIN	Controle total do sistema
ROLE_BARBEIRO	Visualizar e atualizar seus agendamentos
ROLE_CLIENTE	Criar e visualizar seus próprios agendamentos
📂 Estrutura do Projeto
controller/
service/
repository/
model/
security/
config/
dto/
exception/
⚙️ Configuração de Timezone
Para evitar problemas de horário em produção, a aplicação utiliza timezone configurado para:

America/Sao_Paulo
Também foi adicionada uma configuração dedicada para padronizar o horário do sistema no backend.

▶️ Como Executar o Projeto
1. Clonar o repositório
git clone https://github.com/Jonataspaesdev/barbearia-backend.git
2. Entrar na pasta
cd barbearia-backend
3. Executar o projeto
mvn clean install
mvn spring-boot:run
🌐 Acesso Local
API
http://localhost:8080
Swagger
http://localhost:8080/swagger-ui/index.html
🌍 Deploy em Produção
O backend pode ser executado em plataformas como Railway ou Render utilizando variáveis de ambiente.

Variáveis esperadas
PORT

SPRING_PROFILES_ACTIVE

JWT_SECRET

JWT_EXPIRATION

CORS_ALLOWED_ORIGINS

PGHOST

PGPORT

PGDATABASE

PGUSER

PGPASSWORD

📈 Status do Projeto
Backend funcional

Autenticação JWT com roles reais do banco

Cadastro automático de cliente

Endpoint de disponibilidade dinâmica

Controle de acesso por perfil

Regras de negócio completas de agendamento

Timezone ajustado para produção

Fluxo de agendamento funcionando no mesmo dia

Integração total com frontend React

🎯 Objetivo do Projeto
Projeto desenvolvido para estudo e prática de:

arquitetura REST profissional

segurança com JWT

controle de acesso por roles

regras de negócio reais

integração fullstack

deploy em ambiente real

organização de backend em camadas

👨‍💻 Autor
Jonatas Paes
Backend Developer | Java | Spring Boot | React
