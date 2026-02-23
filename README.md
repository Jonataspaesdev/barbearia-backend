💈 Sistema de Barbearia - API REST
API REST desenvolvida em Spring Boot 3 para gerenciamento completo de uma barbearia.

Sistema Fullstack com autenticação JWT (Stateless), controle de acesso por Roles e regras de negócio reais.

🚀 Funcionalidades do Sistema
👥 Clientes
✂️ Serviços
💈 Barbeiros
📅 Agendamentos
🕒 Disponibilidade dinâmica
💳 Pagamentos
📊 Relatório Financeiro

🚀 Tecnologias Utilizadas
Java 17

Spring Boot 3

Spring Security

JWT (Autenticação Stateless)

Spring Data JPA

PostgreSQL

Swagger (OpenAPI)

Maven

🔐 Autenticação (JWT)
A API utiliza autenticação via JWT Token (Stateless).

Após login, o token deve ser enviado nos endpoints protegidos no header:

Authorization: Bearer SEU_TOKEN_AQUI
O sistema extrai a role diretamente do banco e aplica controle de acesso real.

👤 Cadastro de Cliente
Permite que clientes criem conta no sistema.

Endpoint
POST /auth/register
Exemplo de Requisição
{
  "nome": "Cliente Teste",
  "email": "cliente1@gmail.com",
  "telefone": "11999990000",
  "senha": "123456"
}
Resposta
{
  "usuarioId": 10,
  "clienteId": 4,
  "nome": "Cliente Teste",
  "email": "cliente1@gmail.com",
  "role": "ROLE_CLIENTE"
}
Regras
❌ Email não pode duplicar

🔐 Senha criptografada com BCrypt

✔ Cria automaticamente:

Usuario com ROLE_CLIENTE

Cliente vinculado (OneToOne)

🔑 Login
Endpoint
POST /auth/login
Exemplo
{
  "email": "admin@admin.com",
  "senha": "123456"
}
Resposta
{
  "token": "SEU_TOKEN_AQUI",
  "email": "admin@admin.com",
  "nome": "Administrador",
  "role": "ROLE_ADMIN"
}
🛡️ Como usar o Token no Swagger
Faça login em /auth/login

Copie o campo token

Clique em Authorize

Cole:

Bearer SEU_TOKEN_AQUI
Clique em Authorize

Agora você pode acessar endpoints protegidos.

👤 Usuário Administrador Padrão
Criado automaticamente ao iniciar o sistema:

Email: admin@admin.com
Senha: 123456
Role: ROLE_ADMIN

📌 Funcionalidades Implementadas
👥 Clientes
⚠️ Apenas ADMIN pode gerenciar clientes manualmente.

Endpoints
POST   /clientes
GET    /clientes
GET    /clientes/{id}
PUT    /clientes/{id}
DELETE /clientes/{id}
✂️ Serviços
Criar serviço

Listar serviços ativos (público)

Atualizar

Soft delete

Regras
Nome obrigatório

Nome não pode duplicar

Preço > 0

Duração > 0

Endpoints
POST   /servicos        (ADMIN)
GET    /servicos        (Público)
GET    /servicos/{id}
PUT    /servicos/{id}   (ADMIN)
DELETE /servicos/{id}   (Soft delete - ADMIN)
💈 Barbeiros
Criar barbeiro (cria automaticamente usuário ROLE_BARBEIRO)

Listar barbeiros

Atualizar

Soft delete

Reativar

Vincular serviços via servicoIds

Endpoints
POST   /barbeiros
GET    /barbeiros
GET    /barbeiros/{id}
PUT    /barbeiros/{id}
DELETE /barbeiros/{id}
PUT    /barbeiros/{id}/reativar
📅 Agendamentos
Regras de Negócio
❌ Não permite agendar no passado
❌ Não permite fora do horário do barbeiro
❌ Não permite conflito de horário
✔ Calcula automaticamente dataHoraFim
✔ ClienteId associado via token
✔ Status: AGENDADO / CANCELADO / CONCLUIDO

Endpoints
POST   /agendamentos
GET    /agendamentos                     (ADMIN / BARBEIRO)
GET    /agendamentos/cliente/{clienteId}
GET    /agendamentos/barbeiro/{barbeiroId}
PUT    /agendamentos/{id}
DELETE /agendamentos/{id}/cancelar
🕒 Disponibilidade Dinâmica (NOVO)
Permite que o frontend consulte horários ocupados de um barbeiro em um dia específico.

Endpoint
GET /agendamentos/disponibilidade?barbeiroId=1&data=2026-02-23
Retorno
{
  "barbeiroId": 1,
  "data": "2026-02-23",
  "duracaoMin": 30,
  "horaEntrada": "09:00",
  "horaSaida": "18:30",
  "ocupados": ["10:00", "10:30", "14:00"]
}
Regras
Respeita horário de trabalho do barbeiro

Considera duração fixa de 30 minutos

Não retorna dados sensíveis

Ignora agendamentos CANCELADOS

Esse endpoint permite que o frontend desabilite horários ocupados de forma segura.

💳 Pagamentos
Realiza pagamento

Marca automaticamente agendamento como CONCLUIDO

Impede pagamento duplicado

Endpoint
POST /pagamentos
Exemplo
{
  "agendamentoId": 2,
  "valor": 35.0,
  "formaPagamento": "PIX"
}
📊 Relatório Financeiro
GET /pagamentos/relatorio?dataInicio=2026-02-01&dataFim=2026-02-28
Retorna:

Total faturado

Quantidade de pagamentos

Período consultado

🔒 Controle de Acesso
Públicos
/auth/**
GET /servicos
GET /barbeiros
Protegidos (JWT obrigatório)
Clientes

Agendamentos

Pagamentos

Serviços (exceto GET)

Barbeiros (exceto GET)

🏷 Roles
Role	Permissões
ROLE_ADMIN	Controle total
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
▶️ Como Executar o Projeto
1️⃣ Clonar repositório
git clone https://github.com/Jonataspaesdev/barbearia-backend.git
2️⃣ Entrar na pasta
cd barbearia-backend
3️⃣ Executar
mvn clean install
mvn spring-boot:run
🌐 Acesso
API:

http://localhost:8080
Swagger:

http://localhost:8080/swagger-ui/index.html
📈 Status do Projeto
✔ Backend funcional
✔ Autenticação JWT com Roles reais do banco
✔ Cadastro automático de cliente
✔ Endpoint de disponibilidade dinâmica
✔ Controle de acesso por perfil
✔ Regras de negócio completas de agendamento
✔ Integração total com frontend React

🎯 Objetivo do Projeto
Projeto desenvolvido para estudo e prática de:

Arquitetura REST profissional

Segurança com JWT

Controle de acesso por roles

Regras de negócio reais

Integração Fullstack

👨‍💻 Autor
Jonatas Paes
Backend Developer | Java | Spring Boot | React
