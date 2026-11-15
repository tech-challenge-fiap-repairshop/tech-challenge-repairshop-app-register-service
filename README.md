# 🔐👤 Repair Shop — Register Service (`app-register-service`)

[![CI/CD Pipeline](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-register-service/actions/workflows/ci.yml/badge.svg)](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-register-service/actions/workflows/ci.yml)
[![Java 24](https://img.shields.io/badge/Java-24-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Kotlin 2.2](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Spring Boot 4](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)

Microsserviço responsável pelo **domínio de Cadastro e Identidade** da plataforma **Repair Shop (Tech Challenge FIAP — 15SOAT)**, centralizando a gestão de **Clientes (`CUSTOMER`)**, **Veículos (`VEHICLE`)**, **Usuários/Funcionários (`USER`)**, emissão de **tokens JWT** e validação síncrona de integridade antes de exclusões.

---

## 📋 Índice

- [Responsabilidades e Endpoints](#responsabilidades-e-endpoints)
- [Arquitetura Atual — Motivação e Benefícios dos Componentes](#arquitetura-atual--motivação-e-benefícios-dos-componentes)
- [Por que RDS aqui e DynamoDB nos Demais Serviços?](#por-que-rds-aqui-e-dynamodb-nos-demais-serviços)
- [Modelo de Dados e Integrações](#modelo-de-dados-e-integrações)
- [Como Executar Localmente](#como-executar-localmente)
- [Como Executar em Produção (Esteira CI/CD GitHub Actions)](#como-executar-em-produção-esteira-cicd-github-actions)
- [Documentação Técnica e Collection do Postman](#documentação-técnica-e-collection-do-postman)

---

## Responsabilidades e Endpoints

O serviço executa localmente na porta **`8081`** (mapeado na porta padrão Kubernetes conforme [`k8s/`](k8s/service.yaml)) e expõe os seguintes recursos documentados no Swagger (`/swagger-ui.html`):

| Controller | Rotas | Descrição |
| :--- | :--- | :--- |
| **`AuthController`** | `POST /auth/login`<br>`POST /auth/register` | Autenticação por CPF + senha (BCrypt) com emissão de JWT (`86400000 ms` / 24h) e registro de usuários com perfis `CUSTOMER` ou `ATTENDANT`. |
| **`CustomerController`** | `POST /customers`, `GET /customers`, `GET /customers/{id}`, `PUT /customers/{id}`, `DELETE /customers/{id}` | CRUD completo de clientes. Antes de excluir, consulta o `service-order-execution-service` (`ServiceOrderClient`) para impedir exclusão com Ordens de Serviço vinculadas. |
| **`VehicleController`** | `POST /vehicles`, `GET /vehicles`, `GET /vehicles/{id}`, `PUT /vehicles/{id}`, `DELETE /vehicles/{id}` | CRUD completo de veículos vinculados a clientes, também protegido por verificação prévia em `ServiceOrderClient` na exclusão. |

- **Health Probes (Actuator)**: `/actuator/health/liveness` (`livenessState`) e `/actuator/health/readiness` (`readinessState,db`).
- **Logs Correlacionados**: Padrão estruturado compatível com W3C Trace Context (`%X{trace_id}`, `%X{span_id}`) consumido pelo Loki.

---

## Arquitetura Atual — Motivação e Benefícios dos Componentes

Este microsserviço integra uma arquitetura **Cloud Native e orientada a eventos na AWS**, projetada seguindo **Clean Architecture**, **DDD (Bounded Contexts)** e os pilares do **AWS Well-Architected Framework**:

| Pilar / Tecnologia | Papel neste Microsserviço e no Ecossistema | Motivação e Pontos Positivos |
| :--- | :--- | :--- |
| **RDS (PostgreSQL 16)** | **Banco principal do `register-service`** ([`infra-db-rds`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-db-rds)) | Domínio cadastral exige **ACID**, Foreign Keys rigorosas (`tb_vehicle.customer_id -> tb_customer` com `ON DELETE CASCADE`) e restrições `UNIQUE` (`document`, `plate`, `cpf`, `email`). O **Flyway** garante versionamento previsível de DDL em sub-redes privadas criptografadas. |
| **DynamoDB (NoSQL)** | Persistência dos contextos irmãos `service-order` e `payment` ([`infra-db-dynamo`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-db-dynamo)) | Evita `JOIN`s pesados, gargalos de pool de conexões em escala elástica (HPA) e migrações bloqueantes nos domínios transacionais de OS e Pagamentos com alta concorrência e schema evolutivo. |
| **ElastiCache (Redis 7.0)** | Provisionado via [`infra-redis`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-redis) e configurado neste serviço | O serviço possui `spring-boot-starter-data-redis`, variáveis `SPRING_DATA_REDIS_HOST`/`PORT` em `application.properties`, container `redis:7.0-alpine` no Compose e resolução dinâmica na esteira do cluster EKS (`repairshop-user-login-cache-{env}`). *(Nota transparente: no código Kotlin atual, os casos de uso ainda não realizam gravação/leitura ativa de sessão no Redis, estando a infraestrutura e a configuração prontas para cache distribuído de tokens/sessões em baixa latência).* |
| **Apps (EKS + Lambda Auth)** | Container no **EKS (`1.31`)** + integração com [`lambda-auth`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-lambda-auth) | O `lambda-auth` (Java 21) atua na borda recebendo `POST /auth/login`, validando entrada defensivamente e delegando a autenticação via NLB para o `register-service`, enquanto o HPA no EKS escala os pods de cadastro conforme demanda. |
| **Amazon SQS (Mensageria + DLQ)** | Barramento assíncrono do ecossistema ([`infra-sqs`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-sqs)) | Enquanto exclusões no `register-service` exigem consistência imediata (verificação síncrona via `ServiceOrderClient`), os fluxos de aprovação de orçamento, notificação e liquidação de pagamentos operam desacoplados em 5 filas SQS + 5 DLQs em Saga Coreografada. |
| **API Gateway (HTTP API v2)** | Ponto único de entrada ([`infra-apigateway`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-apigateway)) | Expõe `POST /auth/register`, `/customers/**` e `/vehicles/**` via `HTTP_PROXY` para o NLB interno do `register-service` e direciona `POST /auth/login` para o `lambda-auth`, centralizando roteamento, CORS (`traceparent`/`tracestate`) e isolamento da VPC privada. |
| **Observabilidade (Grafana)** | Dashboards dedicados `register-service-observability` e `register-service-logs-traces` | Coleta de métricas JVM/HTTP via OpenTelemetry + Prometheus, rastreamento distribuído no Jaeger propagado desde o API Gateway/Lambda Auth e logs estruturados no Loki consolidados no Grafana. |

---

## Por que RDS aqui e DynamoDB nos Demais Serviços?

Uma decisão arquitetural deliberada da plataforma é usar **banco relacional (RDS PostgreSQL)** no `register-service` e **NoSQL (DynamoDB)** no `service-order-execution-service` e no `payment-service`:

- **Por que RDS no `register-service`?** Entidades mestre (`Cliente`, `Veículo`, `Usuário`) têm schema estável, baixo volume de escrita comparado a eventos de execução e exigem regras relacionais estritas garantidas pelo próprio motor de banco (unicidade simultânea de CPF/CNPJ, placa e e-mail, integridade referencial cliente-veículo e buscas relacionais paginadas).
- **Por que NÃO usamos banco relacional em OS e Pagamentos?**
  1. **Saturação de conexões sob auto-scaling**: Picos de processamento paralelo (pods com HPA + consumidores SQS + webhooks) esgotam rapidamente conexões JDBC persistentes em um banco relacional centralizado, ao passo que o DynamoDB responde via API HTTPS *stateless* sem limite prático de conexões.
  2. **Agregados polimórficos sem `JOIN`s e sem *ALTER TABLE***: Ordens de Serviço agregam metadados, histórico de 12 estados e tarefas extras (`PK=ORDER#<id>`), e Pagamentos unem cobrança + fatura/contingência (`PK=BILLING#<id>`). Em SQL, cada leitura exigiria múltiplos `JOIN`s e toda adaptação a respostas externas do gateway de pagamento exigiria migrações DDL arriscadas.
  3. **Atomicidade local com escalabilidade `PAY_PER_REQUEST`**: O DynamoDB entrega `TransactWriteItems` para consistência do agregado com latência constante em milissegundos de um dígito e **GSIs dedicados** (`pending-dispatch-index`, `dispatch-notification-index`) para consultas de fila de despacho.

---

## Modelo de Dados e Integrações

- **Migrations Flyway**: [`src/main/resources/db/migration/V1__init_register_schema.sql`](src/main/resources/db/migration/V1__init_register_schema.sql)
  - `tb_customer` (`id_tb_customer`, `name`, `document` UNIQUE, `email` UNIQUE, `phone`, `birth_date`)
  - `tb_vehicle` (`id_tb_vehicle`, `customer_id` FK ON DELETE CASCADE, `plate` UNIQUE, `brand`, `model`, `color`, `manufacturing_date`, `last_maintenance`)
  - `tb_user` (`id_tb_user`, `name`, `function`, `cpf` UNIQUE, `email` UNIQUE, `phone`, `password` BCrypt)
- **Integração HTTP síncrona (`ServiceOrderClient`)**: Configurada via variável `SERVICE_ORDER_SERVICE_URL` (padrão local `http://localhost:8080`), protegendo integridade referencial distribuída antes da exclusão de clientes ou veículos.

> **Nota sobre build Maven**: Este repositório **não utiliza Maven Wrapper** (`mvnw`). Utilize o comando `mvn` instalado no sistema com **JDK 24**.

---

## Como Executar Localmente

### Pré-requisitos
- **Docker** e **Docker Compose**
- **JDK 24** e **Apache Maven 3.9+** (`mvn`) *(para execução ou testes fora do Docker)*

### Opção 1 — Via Docker Compose (Recomendada)

O arquivo [`docker-compose.yml`](docker-compose.yml) inicializa `postgres:16`, `redis:7.0-alpine` e o container do `register-service` na porta `8081`:

```bash
# Subir PostgreSQL 16, Redis 7.0 e o Register Service
docker compose up -d --build

# Acompanhar os logs
docker compose logs -f register-service
```

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **Healthcheck**: `http://localhost:8081/actuator/health`

Para derrubar os containers e volumes:
```bash
docker compose down -v
```

### Opção 2 — Via Maven na Máquina Local

```bash
# Subir apenas PostgreSQL e Redis pelo Compose
docker compose up -d postgres redis

# Rodar suíte completa de testes unitários, Cucumber e relatório JaCoCo
mvn clean verify

# Iniciar o microsserviço localmente (porta 8081)
mvn spring-boot:run
```

Principais variáveis de ambiente ([`application.properties`](src/main/resources/application.properties)):
- `SPRING_DATASOURCE_URL` (padrão `jdbc:postgresql://localhost:5432/db_repairshop`)
- `SPRING_DATA_REDIS_HOST` (`localhost`), `SPRING_DATA_REDIS_PORT` (`6379`)
- `JWT_SECRET` / `JWT_EXPIRATION` (`86400000`)
- `SERVICE_ORDER_SERVICE_URL` (`http://localhost:8080`)

---

## Como Executar em Produção (Esteira CI/CD GitHub Actions)

O pipeline está definido em [`.github/workflows/ci.yml`](.github/workflows/ci.yml) com disparo em push/PR para `main`, `homolog` e `develop`, além de execução manual (`workflow_dispatch`).

### Etapas da Esteira
1. **`build`**: Compila com JDK 24 (`mvn clean package -DskipTests`), inicia PostgreSQL 16 e Redis 7 no runner para um *smoke test* real do JAR (`target/register-service-0.0.1-SNAPSHOT.jar`) validando `/actuator/health` na porta `8081`.
2. **`test`**: Executa `mvn clean verify` e publica relatórios **JaCoCo** e **Cucumber**.
3. **`quality`**: Executa análise estática no **SonarCloud**.
4. **`docker-build-push`**: Constrói a imagem para o **Amazon ECR** (`register-service-{env}`), realiza scan de segurança com **Trivy** e executa smoke test no container.
5. **`deploy`**:
   - Conecta ao cluster EKS (`repairshop-eks-{env}`) e lê secrets dinâmicos do ambiente.
   - Descobre dinamicamente o endpoint do **RDS PostgreSQL** (`${cluster_name}-postgres`) e o endpoint primário do **ElastiCache Redis** (`repairshop-user-login-cache-${ENV_NAME}`).
   - Aplica os manifestos [`k8s/`](k8s/) (`namespace.yaml`, `secret.yaml`, `deployment.yaml`, `service.yaml`, `hpa.yaml`) e valida o `rollout status`.

---

## Documentação Técnica e Collection do Postman

- **Postman Collections & Environments (Organização)**:
  - Collection Principal: [`Repair Shop (Tech Challenge FIAP)`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/docs/postman/Repair%20Shop%20%28Tech%20Challenge%20FIAP%29.postman_collection.json)
  - Collection Completa: [`Tech Challenge FIAP - Completo`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/docs/postman/Tech_Challenge_Fiap_-_Completo.postman_collection.json)
  - Environment Local: [`Local.postman_environment.json`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/docs/postman/Local.postman_environment.json)
  - Guia do Postman: [`docs/postman/README.md`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/docs/postman/README.md)
- **Dashboards Grafana deste Serviço**:
  - Observabilidade Geral: [`repairshop-register-service-observability.json`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/local/observability/grafana/dashboards/repairshop-register-service-observability.json)
  - Logs e Traces: [`repairshop-register-service-logs-traces.json`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/local/observability/grafana/dashboards/repairshop-register-service-logs-traces.json)
- **ADRs, RFCs, DDD e Diagramas da Arquitetura**:
  - [ADRs (`ADR-001` a `ADR-013`)](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/ADRs/README.md)
  - [RFCs (`RFC-001` a `RFC-014`)](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/RFCs/README.md)
  - [`Dicionário de Linguagem Ubíqua (DDD)`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/docs/delivery/domain_driven_design/dicionario-linguagem-ubiqua.md)
  - [`Diagrama de Sequência — Auth e Abertura de OS`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-app-service-order-execution-service/blob/main/docs/delivery/sequence/diagrama_sequencia_auth_abertura_os.drawio.png)
- **Repositórios de Infraestrutura relacionados**: [`infra-db-rds`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-db-rds) | [`infra-redis`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-redis) | [`lambda-auth`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-lambda-auth) | [`infra-apigateway`](https://github.com/tech-challenge-fiap-repairshop/tech-challenge-repairshop-infra-apigateway)
