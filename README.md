# 👤 Tech Challenge RepairShop - Register Service

> **Microsserviço de Cadastro, Identidade e Gestão Cadastral**  
> **Tech Challenge SOAT (Fase 4)**  
> **Organização GitHub:** `tech-challenge-fiap-repairshop`

---

## 📌 Visão Geral

O **`register-service`** é o microsserviço responsável pelo gerenciamento de entidades cadastrais da oficina mecânica (Usuários/Mecânicos, Clientes e Veículos), autenticação integrada à função `lambda-auth` e cache de sessões em memória via Redis.

Ele faz parte da arquitetura distribuída e poliglota da Fase 4 e se comunica de forma síncrona/assíncrona com o serviço de ordens de serviço (`service_order_execution-service`).

---

## 🏛️ Arquitetura e Persistência

- **Padrão de Arquitetura:** Clean Architecture / Domain-Driven Design (DDD).
- **Persistência Relacional (SQL):** PostgreSQL via AWS RDS (`register-db`).
  - `tb_user`: Gestão de operadores, atendentes e mecânicos.
  - `tb_customer`: Clientes cadastrados (CPF/CNPJ, dados de contato).
  - `tb_vehicle`: Veículos vinculados a clientes (Placa, modelo, histórico).
- **Cache de Sessão:** Redis (`user-login-cache`) para suporte a autenticação rápida e tokens JWT.
- **Autenticação:** Integrada ao AWS Lambda Authorizer (`tech-challenge-repairshop-lambda-auth`).

---

## 🔀 Integrações no Fluxo da Saga

1. **Recepção de Cliente:** Cadastra o cliente e veículo no balcão da oficina.
2. **Disparo de Ordem de Serviço:** Dispara a abertura inicial de OS chamando `POST /create_service_order` no `service_order_execution-service`.

---

## 📂 Estrutura de Diretórios

```
tech-challenge-repairshop-register-service/
├── .github/workflows/         # Pipelines de CI/CD (GitHub Actions)
├── k8s/                       # Manifestos Kubernetes (Deployment, Service, HPA, ConfigMap)
├── src/
│   ├── main/
│   │   ├── kotlin/com/cao/repairshop/register/
│   │   │   ├── domain/        # Entidades de Domínio e Regras de Negócio Puras
│   │   │   ├── application/   # Casos de Uso (Use Cases) e DTOs
│   │   │   ├── infrastructure/# Repositórios JPA, Cliente Redis, Integração HTTP
│   │   │   └── interfaces/    # Controllers REST e Endpoints OpenAPI
│   │   └── resources/         # application.properties e Migrations Flyway
│   └── test/                  # Testes Unitários e Testes BDD (Cucumber)
├── Dockerfile                 # Multi-stage Docker build
└── README.md
```

---

## 🛡️ Governança de Branches
- `main`: Branch de produção (protegida contra push direto).
- `homolog`: Branch de homologação e testes integrados.
- `develop`: Branch de desenvolvimento ativo.
- `feat/*`, `fix/*`, `chore/*`: Branches de trabalho.
