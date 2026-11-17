# FIAP Tech Challenge — Fase 4

## Sistema Serverless de Avaliação de Aulas

Projeto desenvolvido para a Pós Tech FIAP — Arquitetura e Desenvolvimento em Java.

A aplicação permite registrar avaliações de aulas, classificar sua urgência, processar notificações críticas e gerar relatórios semanais.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Maven
- Azure Functions
- Azure Cosmos DB
- Azure Storage Queue
- Azure Communication Services — Email
- Azure Application Insights

## Arquitetura da solução

A aplicação utiliza uma arquitetura serverless baseada em eventos.

**Fluxo de avaliação:**

1. O cliente envia uma avaliação por HTTP.
2. A Azure Function `avaliacaoHttp` processa a solicitação.
3. A avaliação é persistida no Azure Cosmos DB.
4. Quando necessário, uma mensagem é enviada para a fila `notificacoes-criticas`.
5. A Azure Function `notificacaoCriticaQueue` processa a mensagem e envia uma notificação por e-mail.

**Fluxo do relatório semanal:**

1. A Azure Function `relatorioSemanalTimer` é acionada por agendamento.
2. O serviço de relatórios consulta as avaliações.
3. São calculados o total de avaliações, a média das notas e as quantidades por dia e urgência.
4. Os resultados são registrados nos logs, disponíveis para consulta pelo Application Insights.

## Azure Functions

| Function | Gatilho | Responsabilidade |
|---|---|---|
| `avaliacaoHttp` | HTTP Trigger | Receber e consultar avaliações |
| `notificacaoCriticaQueue` | Queue Trigger | Processar notificações críticas por e-mail |
| `relatorioSemanalTimer` | Timer Trigger | Gerar estatísticas semanais |

O relatório semanal está agendado para segunda-feira, às 12h UTC.

## Segurança

- Credenciais sensíveis são fornecidas por variáveis de ambiente.
- O arquivo `local.settings.json` não é versionado.
- A comunicação com o Azure Storage Queue utiliza `DefaultAzureCredential`.
- A aplicação utiliza controles de autenticação e autorização dos serviços Azure, conforme a configuração de implantação.

## Monitoramento

O Azure Application Insights permite acompanhar a execução das Functions, os registros de aplicação e as falhas de processamento.

## Compilação

Na raiz do projeto, execute:

```bash
./mvnw clean package azure-functions:package -DskipTests
```

## Implantação

Com as credenciais e configurações Azure devidamente provisionadas:

```bash
./mvnw azure-functions:deploy
```

## Configuração

A execução depende de recursos Azure previamente configurados, incluindo Cosmos DB, Storage Queue e Communication Services.

As configurações e credenciais devem ser fornecidas por variáveis de ambiente ou configurações da Function App. Nunca devem ser publicadas no repositório.

## Status do projeto

- Registro e consulta de avaliações implementados.
- Notificação crítica por fila e e-mail testada.
- Relatório semanal implementado e testado.
- Monitoramento pelo Application Insights verificado.

## Contexto acadêmico

Projeto desenvolvido como parte do Tech Challenge — Fase 4 da Pós Tech FIAP, com foco em arquitetura serverless, integração entre serviços de nuvem e processamento orientado a eventos.