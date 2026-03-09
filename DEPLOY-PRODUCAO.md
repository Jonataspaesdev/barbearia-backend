# 🚀 Deploy em Produção – Barbearia (Railway + Vercel)

## 📌 Arquitetura em Produção

O sistema está dividido em dois serviços:

### 🔹 Backend (API)

Hospedado na **Railway**

**URL:**

https://barbearia-backend-production-8882.up.railway.app

**Banco de dados:** PostgreSQL (Railway)  
**Perfil ativo:** local  
**Autenticação:** JWT Stateless  

**Documentação Swagger:**

https://barbearia-backend-production-8882.up.railway.app/swagger-ui/index.html

---

### 🔹 Frontend

Hospedado na **Vercel**

**URL:**

https://barbearia-frontend-two.vercel.app

**Framework:** React + Vite  
**Comunicação com a API:** Axios

---

## 🔐 Configuração de Variáveis de Ambiente

### Backend (Railway)

No serviço **barbearia-backend** foram configuradas as seguintes variáveis:

#### Banco de Dados

SPRING_DATASOURCE_URL

SPRING_DATASOURCE_USERNAME

SPRING_DATASOURCE_PASSWORD

#### JWT

JWT_SECRET

JWT_EXPIRATION

#### CORS

CORS_ALLOWED_ORIGINS

**Valor configurado:**

https://barbearia-frontend-two.vercel.app,http://localhost:5173

---

### Banco PostgreSQL (Railway)

No serviço **Postgres** a Railway gera automaticamente variáveis como:

PGHOST  
PGPORT  
PGDATABASE  
PGUSER  
PGPASSWORD

Essas variáveis são usadas para montar a conexão do backend com o banco.

---

### Frontend (Vercel)

No projeto da Vercel foi configurada a variável:

VITE_API_URL

**Valor configurado:**

https://barbearia-backend-production-8882.up.railway.app

---

## 🌍 Configuração de CORS (Backend)

O backend usa configuração de CORS para permitir requisições do frontend da Vercel e também do ambiente local.

### Origins permitidas

- https://barbearia-frontend-two.vercel.app
- http://localhost:5173

### Métodos permitidos

- GET
- POST
- PUT
- DELETE
- PATCH
- OPTIONS

### Headers permitidos

- Authorization
- Content-Type
- Accept
- Origin

### Outras configurações

- Credentials habilitado
- Exposed headers: Authorization
- Cache de preflight: 3600 segundos

---

## 🔐 Autenticação

### Endpoint de login

POST /auth/login

### Exemplo de requisição

```json
{
  "email": "admin@admin.com",
  "senha": "123456"
}
Exemplo de resposta
{
  "token": "JWT_TOKEN",
  "email": "admin@admin.com",
  "nome": "Administrador",
  "role": "ADMIN"
}

O frontend salva o token no localStorage e envia automaticamente nas próximas requisições usando interceptor do Axios:

Authorization: Bearer TOKEN
👤 Usuário ADMIN padrão

Na inicialização da aplicação, o sistema garante a existência do usuário administrador padrão.

Credenciais padrão

Email:

admin@admin.com

Senha:

123456

Se o usuário já existir, a senha e a role são atualizadas automaticamente no startup da aplicação.

📊 Banco de Dados em Produção

⚠ Importante: o banco de produção da Railway é separado do banco local.

Isso significa que:

dados criados localmente não aparecem em produção

dados criados em produção não aparecem localmente

Ao subir o sistema em produção:

o banco começa vazio

o usuário ADMIN padrão é criado automaticamente

clientes, barbeiros, serviços e agendamentos devem ser cadastrados manualmente via frontend ou Swagger

🔄 Processo de Atualização (Deploy)
Backend
git add .
git commit -m "descrição da alteração"
git push origin main

A Railway detecta o push e faz o deploy automático.

Frontend
git add .
git commit -m "descrição da alteração"
git push origin main

A Vercel detecta o push e faz o deploy automático.

🛠 Problemas resolvidos durante o deploy

Configuração do backend na Railway

Configuração do PostgreSQL na Railway

Ajuste das variáveis de ambiente do banco

Ajuste do application.properties para produção

Geração do domínio público da API

Configuração de CORS entre Vercel e Railway

Ajuste de conflito de configuração de CORS no Spring

Configuração da variável VITE_API_URL na Vercel

Integração JWT entre frontend e backend

Correção de erro de autenticação tratado como 500

Swagger funcionando em produção

✅ Status Atual do Sistema

Backend online na Railway

PostgreSQL online na Railway

Frontend online na Vercel

Swagger funcionando

Login ADMIN funcionando

Integração JWT funcionando

CORS funcionando corretamente

Frontend conectado à API em produção

📌 Observações Importantes
1. Produção e ambiente local usam bancos diferentes

Os dados do seu computador não vão automaticamente para a Railway.

2. O frontend depende da variável VITE_API_URL

Se essa variável estiver errada ou desatualizada na Vercel, o frontend não consegue acessar a API.

3. O backend depende das variáveis de banco

Se SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME ou SPRING_DATASOURCE_PASSWORD estiverem erradas, a aplicação não sobe.

4. O CORS depende da variável CORS_ALLOWED_ORIGINS

Se essa variável não estiver configurada corretamente, o frontend pode receber erro de CORS ao tentar logar ou chamar a API.

🔗 Links finais do sistema
Frontend

https://barbearia-frontend-two.vercel.app

Backend

https://barbearia-backend-production-8882.up.railway.app

Swagger

https://barbearia-backend-production-8882.up.railway.app/swagger-ui/index.html
