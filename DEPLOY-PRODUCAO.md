🚀 Deploy em Produção – Barbearia (Render + Vercel)
📌 Arquitetura em Produção

O sistema está dividido em dois serviços:

🔹 Backend (API)

Hospedado no Render

URL:

https://barbearia-backend-h7da.onrender.com

Banco de dados: PostgreSQL (Render)

Perfil ativo: prod

Autenticação: JWT (Stateless)

Documentação: Swagger disponível em:

/swagger-ui/index.html
🔹 Frontend

Hospedado na Vercel

URL:

https://barbearia-frontend-two.vercel.app

Framework: React + Vite

Comunicação via Axios com API do Render

🔐 Configuração de Variáveis de Ambiente (Render)

No Render → Environment foram configuradas as seguintes variáveis:

Banco de Dados

SPRING_DATASOURCE_URL

SPRING_DATASOURCE_USERNAME

SPRING_DATASOURCE_PASSWORD

JWT

JWT_SECRET

JWT_EXPIRATION

CORS

CORS_ALLOWED_ORIGINS

Valor configurado:

https://barbearia-frontend-two.vercel.app
🌍 Configuração CORS (Backend)

Arquivo: CorsConfig.java

Permite múltiplas origins via variável cors.allowed-origins

Permite métodos:

GET

POST

PUT

DELETE

PATCH

OPTIONS

Permite headers:

Authorization

Content-Type

Permite credentials

Cache preflight: 3600 segundos

Teste manual realizado via:

curl -i -X OPTIONS "https://barbearia-backend-h7da.onrender.com/auth/login" \
  -H "Origin: https://barbearia-frontend-two.vercel.app" \
  -H "Access-Control-Request-Method: POST"

Resultado esperado:

access-control-allow-origin: https://barbearia-frontend-two.vercel.app
🔐 Autenticação

Endpoint:

POST /auth/login

Exemplo:

{
  "email": "admin@admin.com",
  "senha": "123456"
}

Resposta:

{
  "token": "JWT_TOKEN",
  "email": "admin@admin.com",
  "nome": "Administrador",
  "role": "ROLE_ADMIN"
}

O frontend salva o token no localStorage e envia automaticamente via interceptor Axios:

Authorization: Bearer TOKEN
📊 Banco de Dados em Produção

⚠ O banco do Render é separado do banco local.

Ao subir para produção:

O banco inicia vazio.

Apenas o usuário ADMIN padrão é criado automaticamente (se configurado no projeto).

Clientes, barbeiros, serviços e agendamentos devem ser cadastrados manualmente via Swagger ou Frontend.

🔄 Processo de Atualização (Deploy)
Backend
git add .
git commit -m "descrição da alteração"
git push origin main

Render detecta push e faz deploy automático.

Frontend
git add .
git commit -m "descrição da alteração"
git push origin main

Vercel detecta push e faz deploy automático.

🛠 Problemas resolvidos durante deploy

✔ Erro de CORS entre Vercel e Render
✔ Configuração de variáveis JWT em produção
✔ Configuração dinâmica de múltiplas origins
✔ Erro 404 em rotas do React (corrigido via vercel.json)
✔ Problema de preflight OPTIONS
✔ Atualização de build cache no Vercel

✅ Status Atual do Sistema

✔ Backend online (Render)
✔ Frontend online (Vercel)
✔ Login ADMIN funcionando
✔ Login CLIENTE funcionando
✔ Dashboard ADMIN funcionando
✔ Integração JWT funcionando
✔ CORS funcionando corretamente

📌 Observação Importante

Produção e ambiente local utilizam bancos diferentes.

Dados criados localmente não aparecem no Render.
