# 💈 Sistema de Barbearia - API REST


API REST desenvolvida em **Spring Boot 3** para gerenciamento completo de uma barbearia.


---


## 🚀 Tecnologias Utilizadas


- Java 17
- Spring Boot 3
- Spring Security
- JWT (Autenticação Stateless)
- Spring Data JPA
- PostgreSQL
- Swagger (OpenAPI)
- Maven


---


## 🔐 Autenticação


A API utiliza autenticação via **JWT Token**.


O login gera um token que deve ser enviado nos endpoints protegidos.


---


## 🔑 Login


### Endpoint



POST /auth/login



### Exemplo de requisição


```json
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

Copie o campo "token"

Clique em Authorize

Cole o token precedido de Bearer

Exemplo:

Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Clique em Authorize

Agora você pode acessar endpoints protegidos

👤 Usuário Administrador padrão

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

✂️ Serviços

Criar serviço (somente ROLE_ADMIN)

Listar serviços

Atualizar serviço (somente ROLE_ADMIN)

Remover serviço (somente ROLE_ADMIN)

🔒 Controle de Acesso
Endpoints Públicos

/auth/**

GET /servicos

POST /clientes

Endpoints Protegidos

CRUD completo de serviços (ROLE_ADMIN)

CRUD de barbeiros (ROLE_ADMIN)

Atualização de agendamentos (ROLE_ADMIN ou ROLE_BARBEIRO)

A API utiliza SessionCreationPolicy.STATELESS e autenticação via JWT.

📂 Estrutura do Projeto
controller/
service/
repository/
model/
security/
config/
▶️ Como executar o projeto
1️⃣ Clonar o repositório
git clone https://github.com/Jonataspaesdev/barbearia-backend.git
2️⃣ Entrar na pasta do projeto
cd barbearia-backend
3️⃣ Executar
mvn clean install
mvn spring-boot:run
🌐 Acesso

API:

http://localhost:8080

Swagger:

http://localhost:8080/swagger-ui/index.html
📌 Autor

Desenvolvido por Jonatas Paes
Backend Developer | Java | Spring Boot
