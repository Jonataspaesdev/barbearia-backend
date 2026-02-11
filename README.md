# ✂️ Sistema de Barbearia — Backend

API REST completa para gerenciamento de barbearia, construída com **Java 17 + Spring Boot 3**.

---

## 🛠️ Tecnologias

| Camada         | Tecnologia              |
|---------------|-------------------------|
| Linguagem      | Java 17                 |
| Framework      | Spring Boot 3.2.1       |
| Persistência   | Spring Data JPA         |
| Banco          | PostgreSQL 15           |
| Migrations     | Flyway                  |
| Segurança      | Spring Security + JWT   |
| Documentação   | Swagger / OpenAPI 3     |
| Build          | Maven                   |
| Testes         | JUnit 5 + Mockito       |

---

## 🚀 Como executar

### Pré-requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL 15+ rodando

### 1. Criar banco de dados

```sql
CREATE DATABASE barbearia_db;
```

### 2. Configurar credenciais

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/barbearia_db
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 3. Executar

```bash
mvn spring-boot:run
```

O Flyway criará as tabelas automaticamente e populará os dados iniciais.

### 4. Acessar o Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Autenticação

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@barbearia.com",
  "senha": "admin123"
}
```

Retorna um token JWT. Use-o em todas as requisições:

```http
Authorization: Bearer <seu_token_jwt>
```

### Perfis de acesso

| Role            | Permissões                                      |
|----------------|-------------------------------------------------|
| `ROLE_ADMIN`    | Acesso total                                   |
| `ROLE_BARBEIRO` | Ver agenda, marcar como concluído              |
| `ROLE_CLIENTE`  | Criar conta, agendar, ver histórico            |

---

## 📋 Endpoints

### Autenticação
| Método | Endpoint        | Descrição            | Auth |
|--------|----------------|----------------------|------|
| POST   | `/auth/login`   | Login (gera JWT)     | ❌   |

### Clientes
| Método | Endpoint            | Descrição                  | Auth  |
|--------|--------------------|-----------------------------|-------|
| POST   | `/clientes`         | Criar cliente               | ❌    |
| GET    | `/clientes`         | Listar clientes             | ✅    |
| GET    | `/clientes/{id}`    | Buscar por ID               | ✅    |
| PUT    | `/clientes/{id}`    | Atualizar                   | ✅    |
| DELETE | `/clientes/{id}`    | Desativar                   | ADMIN |

### Barbeiros
| Método | Endpoint            | Descrição                  | Auth  |
|--------|--------------------|-----------------------------|-------|
| POST   | `/barbeiros`        | Cadastrar barbeiro          | ADMIN |
| GET    | `/barbeiros`        | Listar barbeiros            | ✅    |
| GET    | `/barbeiros/{id}`   | Buscar por ID               | ✅    |
| PUT    | `/barbeiros/{id}`   | Atualizar                   | ADMIN |

### Serviços
| Método | Endpoint           | Descrição                   | Auth  |
|--------|-------------------|-----------------------------|-------|
| POST   | `/servicos`        | Criar serviço               | ADMIN |
| GET    | `/servicos`        | Listar serviços ativos       | ❌    |
| PUT    | `/servicos/{id}`   | Atualizar                   | ADMIN |
| DELETE | `/servicos/{id}`   | Inativar                    | ADMIN |

### Agendamentos
| Método | Endpoint                          | Descrição                    | Auth  |
|--------|----------------------------------|-------------------------------|-------|
| POST   | `/agendamentos`                   | Criar agendamento             | ✅    |
| GET    | `/agendamentos`                   | Listar todos                  | ✅    |
| GET    | `/agendamentos/{id}`              | Buscar por ID                 | ✅    |
| GET    | `/agendamentos/cliente/{id}`      | Por cliente                   | ✅    |
| GET    | `/agendamentos/barbeiro/{id}`     | Por barbeiro                  | ✅    |
| GET    | `/agendamentos/horarios-disponiveis` | Horários disponíveis       | ✅    |
| PUT    | `/agendamentos/{id}`              | Atualizar / remarcar          | ✅    |
| DELETE | `/agendamentos/{id}`              | Cancelar                      | ✅    |

### Financeiro
| Método | Endpoint                       | Descrição               | Auth  |
|--------|-------------------------------|--------------------------|-------|
| POST   | `/financeiro/pagamentos`       | Registrar pagamento      | ADMIN |
| GET    | `/financeiro/relatorio/diario` | Relatório diário         | ADMIN |
| GET    | `/financeiro/relatorio/mensal` | Relatório mensal         | ADMIN |

---

## 🏗️ Arquitetura

```
src/
└── main/java/com/barbearia/
    ├── BarbeariaApplication.java       ← Ponto de entrada
    ├── controller/
    │   └── Controllers.java            ← REST Controllers
    ├── service/
    │   ├── AgendamentoService.java     ← Regras de negócio
    │   └── Services.java               ← Cliente, Barbeiro, Serviço, Financeiro
    ├── repository/
    │   └── Repositories.java           ← Spring Data JPA
    ├── model/
    │   ├── Usuario.java
    │   ├── Cliente.java
    │   ├── Barbeiro.java
    │   ├── Servico.java
    │   ├── Agendamento.java
    │   └── Pagamento.java
    ├── dto/
    │   └── DTOs.java                   ← Request/Response objects
    ├── security/
    │   └── SecurityConfig.java         ← JWT + Spring Security
    ├── exception/
    │   └── Exceptions.java             ← Handler global de erros
    └── config/
        └── SwaggerConfig.java          ← OpenAPI docs

resources/
├── application.properties
└── db/migration/
    ├── V1__criar_tabelas.sql           ← Criação das tabelas
    └── V2__seeds_iniciais.sql          ← Dados iniciais
```

---

## 📐 Regras de Negócio — Agendamentos

1. **Data futura** — não é permitido agendar em data/hora passada
2. **Expediente** — o horário deve estar dentro do horário de trabalho do barbeiro
3. **Sem conflito** — dois agendamentos não podem se sobrepor para o mesmo barbeiro (considerando a duração do serviço)
4. **Status flow**: `AGENDADO → CONFIRMADO → CONCLUIDO` (cancelamento possível em AGENDADO e CONFIRMADO)
5. **Pagamento** — só pode ser registrado para agendamentos `CONCLUIDO`

---

## 🧱 Modelo de dados

```
usuarios (1) ──── (1) clientes ──── (N) agendamentos
usuarios (1) ──── (1) barbeiros ─── (N) agendamentos
barbeiros (N) ─── (N) servicos  (via barbeiros_servicos)
servicos  (1) ──── (N) agendamentos
agendamentos (1) ─ (1) pagamentos
```

---

## 🧪 Rodando os testes

```bash
mvn test
```

---

## 🔮 Funcionalidades futuras (arquitetura preparada)

- [ ] Integração com WhatsApp (webhook)
- [ ] Pagamento online (Stripe / Pagar.me)
- [ ] Sistema de fidelidade (pontos por atendimento)
- [ ] Dashboard administrativo
- [ ] Multi-barbearias (SaaS com tenant_id)
- [ ] Notificações por email/SMS de lembrete

---

## 📄 Licença

MIT — uso livre para estudo e produção.
