Perfeito 👏🔥
Vou te entregar ele organizado, profissional e pronto pra copiar e substituir inteiro seu README.md.

Só copiar tudo abaixo 👇

💈 Sistema de Barbearia - API REST

API REST desenvolvida em Spring Boot 3 para gerenciamento completo de uma barbearia:

👥 Clientes

✂️ Serviços

💈 Barbeiros

📅 Agendamentos

💳 Pagamentos

📊 Relatório Financeiro

Com autenticação JWT (Stateless) e documentação via Swagger (OpenAPI).

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

A API utiliza autenticação via JWT Token.

O login gera um token que deve ser enviado nos endpoints protegidos.

🔑 Login
Endpoint
POST /auth/login
📥 Exemplo de Requisição
{
  "email": "admin@admin.com",
  "senha": "123456"
}
📤 Resposta
{
  "token": "SEU_TOKEN_AQUI",
  "email": "admin@admin.com",
  "nome": "Administrador",
  "role": "ROLE_ADMIN"
}
🛡️ Como usar o Token no Swagger

Faça login em POST /auth/login

Copie o campo token

Clique em Authorize

Cole o token com o prefixo:

Bearer SEU_TOKEN_AQUI

Clique em Authorize

Agora você pode acessar endpoints protegidos.

👤 Usuário Administrador Padrão

Ao iniciar o sistema, um usuário administrador é criado automaticamente:

Email: admin@admin.com

Senha: 123456

Role: ROLE_ADMIN

📌 Funcionalidades Implementadas
👥 Clientes

Criar cliente

Listar clientes

Buscar cliente por ID

Atualizar cliente

Remover cliente

Endpoints
POST   /clientes
GET    /clientes
GET    /clientes/{id}
PUT    /clientes/{id}
DELETE /clientes/{id}
✂️ Serviços

Criar serviço

Listar serviços ativos

Buscar serviço por ID

Atualizar serviço

Desativar serviço (soft delete)

Regras

Nome obrigatório

Nome não pode duplicar

Preço > 0

Duração > 0

Endpoints
POST   /servicos
GET    /servicos
GET    /servicos/{id}
PUT    /servicos/{id}
DELETE /servicos/{id}   (soft delete)
💈 Barbeiros

Criar barbeiro (cria automaticamente usuário ROLE_BARBEIRO)

Listar barbeiros

Buscar barbeiro por ID

Atualizar barbeiro

Desativar barbeiro (soft delete)

Reativar barbeiro

Vincular serviços via servicoIds

Endpoints
POST   /barbeiros
GET    /barbeiros
GET    /barbeiros/{id}
PUT    /barbeiros/{id}
DELETE /barbeiros/{id}
PUT    /barbeiros/{id}/reativar
📅 Agendamentos

Criar agendamento

Listar todos

Listar por cliente

Listar por barbeiro

Atualizar (dataHora/status/observacao)

Cancelar agendamento

Regras de Negócio

❌ Não permite agendar no passado

❌ Não permite fora do horário do barbeiro

❌ Não permite conflito de horário (considerando duração do serviço)

✔ Ao realizar pagamento, o status do agendamento é atualizado automaticamente para CONCLUIDO

Endpoints
POST   /agendamentos
GET    /agendamentos
GET    /agendamentos/cliente/{clienteId}
GET    /agendamentos/barbeiro/{barbeiroId}
PUT    /agendamentos/{id}
DELETE /agendamentos/{id}/cancelar
💳 Pagamentos

Realizar pagamento de um agendamento

Marca automaticamente o agendamento como CONCLUIDO

Impede pagamento duplicado

Valida regras de negócio

Endpoint
POST /pagamentos
Exemplo
{
  "agendamentoId": 2,
  "valor": 35.0,
  "formaPagamento": "PIX"
}
📊 Relatório Financeiro

Relatório financeiro por período.

Endpoint
GET /pagamentos/relatorio?dataInicio=2026-02-01&dataFim=2026-02-28
Retorna

Total faturado

Quantidade de pagamentos

Período consultado

🔒 Controle de Acesso (Resumo)
Públicos
/auth/**
GET /servicos
Protegidos (JWT obrigatório)

Clientes

Barbeiros

Agendamentos

Pagamentos

Permissões específicas dependem da SecurityConfig.

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
1️⃣ Clonar o repositório
git clone https://github.com/Jonataspaesdev/barbearia-backend.git
2️⃣ Entrar na pasta
cd barbearia-backend
3️⃣ Executar
mvn clean install
mvn spring-boot:run
🌐 Acesso

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui/index.html

📈 Status do Projeto

✔ Backend funcional
🚧 Frontend em desenvolvimento

👨‍💻 Autor

Jonatas Paes
Backend Developer | Java | Spring Boot

