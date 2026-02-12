<!--# 💈 Sistema de Barbearia API-->

API REST desenvolvida em **Spring Boot 3** para gerenciamento completo de uma barbearia.

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Security
- JWT (Autenticação)
- Spring Data JPA
- PostgreSQL
- Swagger (OpenAPI)

---

## 🔐 Autenticação

A API utiliza autenticação via **JWT Token**.

### 🔑 Login

Endpoint:

```
POST /auth/login
```

Exemplo de requisição:

```json
{
  "email": "admin@admin.com",
  "senha": "123456"
}
```

Resposta:

```json
{
  "token": "SEU_TOKEN_AQUI",
  "email": "admin@admin.com",
  "nome": "Administrador",
  "role": "ADMIN"
}
```

---

## 🛡️ Como usar o Token no Swagger

1. Faça login em `/auth/login`
2. Copie o campo `"token"`
3. Clique em **Authorize**
4. Cole apenas o token (sem a palavra Bearer)
5. Clique em **Authorize**

---

## 👤 Usuário padrão criado automaticamente

Ao iniciar o sistema, um usuário administrador é criado:

- Email: `admin@admin.com`
- Senha: `123456`
- Role: `ADMIN`

---

## 📂 Estrutura do Projeto

```
controller/
service/
repository/
model/
security/
config/
```

---

## ▶️ Como executar o projeto

```bash
mvn clean install
mvn spring-boot:run
```

A API ficará disponível em:

```
http://localhost:8080
```

Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

## 📌 Autor

Desenvolvido por **Jonatas Paes**
