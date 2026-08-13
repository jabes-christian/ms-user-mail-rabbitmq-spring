# ms-user

Microsserviço responsável pelo **cadastro de usuários**. Atua como **Producer** no fluxo de mensageria: ao salvar um novo usuário, publica um evento no RabbitMQ para que o `ms-email` processe o envio de e-mail.

## Tecnologias

- Java 21
- Spring Boot 4.1.0 (Web, Data JPA)
- PostgreSQL
- RabbitMQ (Spring AMQP)
- Docker
- Maven

## Estrutura do projeto

```
com.ms.user
├── config
│   └── RabbitMQConfig     -> Configuração do MessageConverter (JSON) do RabbitMQ
├── controller
│   └── UserController     -> Endpoints REST
├── dto
│   ├── UserRecordDto       -> Payload de entrada (requisição)
│   └── EmailDto            -> Payload publicado na fila para o ms-email
├── entity
│   └── UserEntity          -> Entidade JPA
├── producer
│   └── UserProducer        -> Publica mensagens no RabbitMQ
├── repository
│   └── UserRepository      -> Acesso a dados (Spring Data JPA)
└── service
    └── UserService          -> Regras de negócio
```

## Fluxo de cadastro

1. `POST /users` recebe os dados do usuário.
2. `UserService` salva a entidade no PostgreSQL via `UserRepository`.
3. `UserService` aciona o `UserProducer`, que publica um `EmailDto` na fila do RabbitMQ.
4. O `ms-email` consome essa mensagem de forma assíncrona.

## Configuração do RabbitMQ

O `RabbitMQConfig` define o `MessageConverter` usado pelo `RabbitTemplate` para serializar os payloads em JSON (necessário para o Spring AMQP conseguir enviar objetos que não sejam `String`/`byte[]`/`Serializable`):

```java
@Configuration
public class RabbitMQConfig {

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
```

## Variáveis de ambiente

Configuradas via `.env` / `application.properties`:

| Variável | Descrição |
|---|---|
| `POSTGRES_DB`             | URL de conexão com o PostgreSQL                 |
| `POSTGRES_USER`           | Usuário do banco                                |
| `POSTGRES_PASSWORD`       | Senha do banco                                  |
| `POSTGRES_PORT`           | Porta do banco                                  |
| `SERVER_PORT`             | Porta do Servidor da Aplicação                  |
| `URL_CLOUD_AMQP`    | String de conexão com o cloud AMQP com RabbitMQ |

> Veja `.env.example` para o modelo completo.

## Como executar

```bash
docker-compose up -d
./mvnw spring-boot:run
```

Aplicação disponível em: `http://localhost:8081`

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/users` | Cadastra um novo usuário e dispara o evento de e-mail |

Exemplo de requisição:

```json
{
  "name": "Jabes",
  "email": "jabescris123@mail.com"
}
```

## Roadmap / Próximos passos

- [ ] **Spring Security + JWT**: proteger os endpoints REST com autenticação baseada em token
- [ ] Validação de payload de entrada (Bean Validation)
- [ ] Tratamento global de exceções (`@RestControllerAdvice`)
- [ ] Testes unitários e de integração (JUnit, Testcontainers)
- [ ] Documentação de API com OpenAPI/Swagger