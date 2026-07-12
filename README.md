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

## Documentação da API (Swagger / OpenAPI)

A API é documentada com [springdoc-openapi](https://springdoc.org/). Com a aplicação rodando:

- **Swagger UI** (interativo, permite testar os endpoints pelo navegador): `http://localhost:8080/swagger-ui.html`
- **Spec OpenAPI (JSON)**: `http://localhost:8080/v3/api-docs`

Os endpoints, parâmetros e schemas (`ProductDTO`, `CurrencyType`) estão anotados diretamente no código (`@Operation`, `@Parameter`, `@Schema`), então a documentação exibida na Swagger UI sempre reflete os controllers atuais.

## Envio diário automático

O `comprasparaguai.com.br` bloqueia (HTTP 403) requisições vindas de IPs de datacenter/nuvem — inclusive dos runners do GitHub Actions — então o agendamento automático **precisa rodar de um IP residencial** (seu computador, por exemplo), não de um serviço de nuvem convencional.

Por enquanto, o jeito de garantir a mensagem diária às 9h (o cron interno já está configurado em `ProductScheduler`) é deixar o container rodando continuamente:

```bash
docker compose up -d
```

O `restart: unless-stopped` no `docker-compose.yml` garante que ele volta a subir sozinho se o Docker Desktop reiniciar — mas o computador precisa estar ligado e com o Docker ativo no horário. Vale configurar o Docker Desktop para iniciar junto com o sistema.

Existe um workflow em `.github/workflows/daily-monitor.yml` (desativado do agendamento automático, só roda manualmente via `Actions → Daily Product Monitor → Run workflow`) caso no futuro seja adicionado um proxy residencial — aí o agendamento automático na nuvem volta a ser viável.
