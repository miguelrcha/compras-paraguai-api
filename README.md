# Compras Paraguai API

API Spring Boot que faz scraping de preços de produtos em lojas do Paraguai (comprasparaguai.com.br), compara preços em USD/BRL e envia alertas via Telegram.

## Requisitos

- Docker e Docker Compose

## Rodando com Docker

1. Copie o arquivo de variáveis de ambiente:

   ```bash
   cp .env.example .env
   ```

2. Preencha `TELEGRAM_TOKEN` e `TELEGRAM_CHAT_ID` em `.env` (opcional — sem eles, os endpoints de scraping/API funcionam normalmente, só o envio de notificações falha).

3. Suba a aplicação e o banco Postgres:

   ```bash
   docker compose up --build
   ```

A API sobe em `http://localhost:8080` e o Postgres fica disponível em `localhost:5432` (dados persistidos no volume `postgres_data`).

## Variáveis de ambiente

| Variável           | Descrição                          | Padrão              |
|--------------------|-------------------------------------|----------------------|
| `DB_NAME`          | Nome do banco Postgres              | `compras_paraguai`   |
| `DB_USER`          | Usuário do Postgres                 | `postgres`           |
| `DB_PASSWORD`      | Senha do Postgres                   | `postgres`           |
| `DB_PORT`          | Porta exposta do Postgres no host   | `5432`               |
| `TELEGRAM_TOKEN`   | Token do bot do Telegram            | -                     |
| `TELEGRAM_CHAT_ID` | Chat ID para envio das notificações | -                     |
| `SERVER_PORT`      | Porta exposta da API no host        | `8080`                |

## Rodando localmente sem Docker

Necessário um Postgres rodando localmente (ou aponte `DB_HOST`/`DB_PORT` para uma instância existente) e Java 21 + Maven.

```bash
./mvnw spring-boot:run
```

As mesmas variáveis de ambiente da tabela acima podem ser exportadas antes de rodar.

## Endpoints principais

- `GET /api/products?name={produto}&limit={n}&currency={USD|BRL}` — busca produtos.
- `GET /api/products/monitor` — dispara o monitoramento manualmente.
- `GET /api/products/telegram-test` — testa o envio de mensagem no Telegram.

## Envio diário automático (GitHub Actions)

O workflow `.github/workflows/daily-monitor.yml` roda todo dia às 9h (América/São Paulo), sobe os containers, chama `/api/products/monitor` e desliga tudo em seguida. Não precisa de servidor nem de rodar Docker manualmente — o próprio GitHub executa.

Para funcionar, cadastre os secrets do repositório (**Settings → Secrets and variables → Actions → New repository secret**):

| Secret              | Valor                          |
|---------------------|----------------------------------|
| `TELEGRAM_TOKEN`    | Token do bot (via @BotFather)    |
| `TELEGRAM_CHAT_ID`  | Chat ID de destino das mensagens |

Para testar sem esperar o horário agendado, vá em **Actions → Daily Product Monitor → Run workflow** (disparo manual via `workflow_dispatch`).
