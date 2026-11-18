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

1. A Azure Function `relatorioSemanalTimer` é executada automaticamente às segundas-feiras, às 12h UTC (9h em São Paulo).
2. O `RelatorioService` consulta as avaliações da semana anterior no Azure Cosmos DB.
3. O sistema calcula o total de avaliações, a média das notas, a quantidade por dia e a quantidade por urgência.
4. O relatório inclui os detalhes individuais de cada avaliação: descrição, nota, urgência e data de criação.
5. Os resultados são registrados nos logs da Azure Functions e podem ser consultados pelo Azure Application Insights.

**Período considerado:** segunda-feira a domingo da semana anterior, utilizando o fuso horário `America/Sao_Paulo`.

**Validação:** a execução manual da Function foi testada com sucesso na Azure, incluindo a consulta ao Cosmos DB e a geração dos indicadores e detalhes individuais.

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
### Implantação automatizada com GitHub Actions

O projeto utiliza GitHub Actions para automatizar a compilação e a implantação das Azure Functions.

O workflow está localizado em `.github/workflows/deploy-azure.yml`.

A automação é acionada por:
- Push na branch `main`.
- Execução manual pela opção `workflow_dispatch`.

**Etapas do pipeline:**

1. Checkout do código-fonte.
2. Configuração do Java 21.
3. Autenticação na Azure por OpenID Connect (OIDC).
4. Compilação e empacotamento com Maven.
5. Implantação na Azure Functions.

**Autenticação e permissões:**

A integração utiliza um Service Principal do Microsoft Entra ID, com permissão `Contributor` limitada ao Resource Group `fiap-feedback-functions_group`.

O GitHub armazena os identificadores de autenticação em Repository Secrets:

- `AZURE_CLIENT_ID`
- `AZURE_TENANT_ID`
- `AZURE_SUBSCRIPTION_ID`

A autenticação OIDC utiliza credenciais temporárias, sem necessidade de armazenar senhas permanentes do Service Principal no repositório.

**Validação:** o pipeline foi executado com sucesso em 17/11/2026, realizando a implantação automatizada das Azure Functions.

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