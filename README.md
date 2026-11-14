# Register Service

Microsserviço de cadastro de clientes, veículos e usuários da oficina, desenvolvido em Kotlin com Spring Boot para o Tech Challenge FIAP — Fase 4.

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de clientes e veículos, com persistência em PostgreSQL e migração de esquema pelo Flyway.
- Registro e autenticação de usuários por `POST /auth/register` e `POST /auth/login`.
- Autenticação JWT stateless implementada neste serviço. As senhas são armazenadas com hash BCrypt; os papéis disponíveis são `CUSTOMER` e `ATTENDANT`. As rotas de clientes e veículos exigem o papel `ATTENDANT`.
- Validação de dados de entrada e documentação da API via Swagger/OpenAPI.
- Campo `last_maintenance` para registrar a data/hora da última manutenção do veículo. O modelo atual não mantém um histórico de manutenções.

O registro de usuário é público e recebe o papel (`CUSTOMER` ou `ATTENDANT`) no próprio pedido. O segredo JWT padrão é destinado somente ao desenvolvimento local; configure `JWT_SECRET` com um valor forte e exclusivo em outros ambientes.

## Arquitetura e integrações

O código separa domínio, casos de uso e infraestrutura, com adaptadores JPA para PostgreSQL. O `docker-compose.yml` também inicia Redis e a aplicação recebe as configurações de conexão, mas não há uso de Redis para cache ou sessões demonstrado no código atual.

O `ServiceOrderClient` consulta o serviço de ordens de serviço de forma síncrona ao excluir clientes ou veículos, para verificar se há ordens associadas. O cadastro de cliente não dispara automaticamente a criação de uma ordem de serviço; embora exista um método de criação no cliente, ele não está conectado aos casos de uso de cadastro.

## Executar localmente

É necessário ter Docker Desktop com Docker Compose disponível.

1. Na raiz do repositório, execute:

   ```sh
   docker compose up --build
   ```

2. Aguarde o PostgreSQL e o Redis ficarem saudáveis e a aplicação iniciar.
3. Abra o Swagger em [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html). A verificação de saúde fica em [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health).
4. No Swagger, registre um usuário em `POST /auth/register` com papel `ATTENDANT`, autentique em `POST /auth/login` e use o token JWT para acessar as rotas protegidas.
5. Para encerrar os containers, execute `docker compose down`. Os dados do PostgreSQL permanecem no volume local do Compose.

### Atenção à integração com ordens de serviço

O Compose deste repositório não fornece o serviço de ordens de serviço. Ele configura `SERVICE_ORDER_SERVICE_URL=http://localhost:8080`, mas, dentro do container, `localhost` aponta para o próprio container do `register-service`, não para outro serviço nem para o computador host. Se essa URL não responder, as verificações de ordens associadas falham com resultado “não existe”; assim, uma exclusão pode prosseguir sem confirmar se há ordens vinculadas. Para validar corretamente essa regra, conecte um serviço de ordens acessível pela rede Docker e configure `SERVICE_ORDER_SERVICE_URL` com o endereço alcançável a partir do container.

## Tecnologias

- Kotlin 2.2 e Java 24
- Spring Boot 4: Web, Security, Validation, Data JPA e Actuator
- PostgreSQL 16 e Flyway
- JWT e BCrypt
- Redis 7 provisionado no Compose
- Docker multi-stage e manifests Kubernetes
- Testes automatizados Kotlin/JUnit e MockK em `src/test/kotlin`

## Estrutura do repositório

```text
.
├── .github/workflows/ci.yml
├── k8s/                         # Manifests de deployment, service, probes e configuração
├── src/
│   ├── main/kotlin/com/cao/repairshop/
│   │   ├── core/                # Configuração, segurança, validação e erros
│   │   ├── register/            # Cadastro, domínio, casos de uso e persistência
│   │   └── user/                # Usuários e autenticação
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── db/migration/        # Migrações Flyway
│   └── test/kotlin/             # Testes automatizados
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```
