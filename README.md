# ms-spring-rabbitmq

Projeto de estudo com dois microsserviços independentes que se comunicam de forma assíncrona via **RabbitMQ**, aplicando os conceitos de **producer**, **broker** e **consumer**.

## Arquitetura

```
ms-user-mail/
├── user/    -> Microsserviço responsável pelo cadastro de usuários (Producer)
├── email/   -> Microsserviço responsável pelo envio/registro de e-mails (Consumer)
```

Fluxo:

1. O serviço **user** recebe uma requisição REST para cadastrar um usuário.
2. Após salvar no banco, o **user** publica uma mensagem na fila do RabbitMQ (Producer).
3. O **email** consome essa mensagem da fila (Consumer) e processa o envio/registro do e-mail.

```
[Cliente] -> [ms-user] -> (RabbitMQ) -> [ms-email]
```

## Tecnologias

- Java 21
- Spring Boot 4.1.0
- Spring Web
- Spring Data JPA
- PostgreSQL
- RabbitMQ (Spring AMQP)
- Docker / Docker Compose
- Maven

## Como executar

### Pré-requisitos
- Docker e Docker Compose instalados
- Java 21
- Maven (ou usar o `mvnw` incluso em cada serviço)

### Subindo a infraestrutura (RabbitMQ + Postgres)

Cada microsserviço possui seu próprio `docker-compose.yml`. Suba a infraestrutura necessária antes de iniciar as aplicações:

```bash
cd user
docker-compose up -d

cd ../email
docker-compose up -d
```

### Subindo as aplicações

Em cada pasta (`user` e `email`), rode:

```bash
./mvnw spring-boot:run
```

> Consulte o `README.md` de cada microsserviço para detalhes de portas, variáveis de ambiente e endpoints.

## Microsserviços

| Serviço | Papel no RabbitMQ | Responsabilidade |
|---|---|---|
| [`user`](./user/README.md) | Producer | Cadastro de usuários e publicação de eventos |
| [`email`](./email/README.md) | Consumer | Consumo de eventos e processamento de e-mails |

## Roadmap / Próximos passos

- [ ] **Spring Security + JWT** para autenticação e autorização entre cliente e `ms-user`
- [ ] Comunicação segura entre serviços (ex.: validação de token, API Gateway)
- [ ] Tratamento de falhas no consumo (Dead Letter Queue / retry)
- [ ] Testes automatizados (unitários e de integração)
- [ ] Observabilidade (logs estruturados, health checks, métricas)
- [ ] Documentação de API (OpenAPI/Swagger)

## Objetivo do projeto

Este repositório tem fins de **estudo**, com foco em fixar os conceitos fundamentais de mensageria assíncrona com RabbitMQ em uma arquitetura de microsserviços com Spring Boot.