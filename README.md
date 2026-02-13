💈 Sistema de Barbearia - API REST

API REST desenvolvida em Spring Boot 3 para gerenciamento completo de uma barbearia (clientes, serviços, barbeiros e agendamentos), com autenticação JWT e documentação via Swagger.

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
Exemplo de requisição `json { "email": "admin@admin.com", "senha": "123456" }

Resposta

{ "token": "SEU_TOKEN_AQUI", "email": "admin@admin.com", "nome": "Administrador", "role": "ROLE_ADMIN" } 🛡️ Como usar o Token no Swagger

Faça login em POST /auth/login

Copie o campo token

Clique em Authorize

Cole o token com o prefixo Bearer

Exemplo:

Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Clique em Authorize

Agora você pode acessar endpoints protegidos

👤 Usuário Administrador padrão

Ao iniciar o sistema, um usuário administrador é criado automaticamente:

Email: admin@admin.com

Senha: 123456

Role: ROLE_ADMIN

📌 Funcionalidades Implementadas 👥 Clientes

Criar cliente

Listar clientes

Buscar cliente por ID

Atualizar cliente

Remover cliente

✂️ Serviços

Criar serviço (somente ROLE_ADMIN, se sua segurança estiver assim)

Listar serviços

Buscar serviço por ID

Atualizar serviço (somente ROLE_ADMIN, se sua segurança estiver assim)

Desativar serviço (soft delete)

💈 Barbeiros

Criar barbeiro

Listar barbeiros

Buscar barbeiro por ID

Atualizar barbeiro

Remover barbeiro

Vincular serviços ao barbeiro via servicoIds

Endpoints principais

POST /barbeiros

GET /barbeiros

GET /barbeiros/{id}

PUT /barbeiros/{id}

DELETE /barbeiros/{id}

📅 Agendamentos

Criar agendamento

Listar todos

Listar por cliente

Listar por barbeiro

Atualizar (dataHora/status/observacao)

Cancelar agendamento

Regras de negócio:

Não permite agendar no passado

Não permite fora do horário de trabalho do barbeiro

Não permite conflito de horário para o mesmo barbeiro (considerando duração do serviço)

Endpoints principais

POST /agendamentos

GET /agendamentos

GET /agendamentos/cliente/{clienteId}

GET /agendamentos/barbeiro/{barbeiroId}

PUT /agendamentos/{id}

DELETE /agendamentos/{id}/cancelar

🔒 Controle de Acesso (resumo)

Públicos:

/auth/**

GET /servicos

POST /clientes (se estiver permitido na sua SecurityConfig)

Protegidos (exemplos comuns):

CRUD de serviços (ROLE_ADMIN)

CRUD de barbeiros (ROLE_ADMIN ou autenticado, depende da sua regra)

Agendamentos (autenticado; update/cancel pode depender de ROLE)

Observação: as permissões exatas dependem da sua SecurityConfig.

📂 Estrutura do Projeto

controller/

service/

repository/

model/

security/

config/

▶️ Como executar o projeto 1️⃣ Clonar o repositório git clone https://github.com/Jonataspaesdev/barbearia-backend.git 2️⃣ Entrar na pasta do projeto cd barbearia-backend 3️⃣ Executar mvn clean install mvn spring-boot:run 🌐 Acesso

API: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui/index.html

📌 Autor

Desenvolvido por Jonatas Paes Backend Developer | Java | Spring Boot
