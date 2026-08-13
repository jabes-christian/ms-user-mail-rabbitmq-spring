# ms-email

Microsserviço responsável por **consumir eventos de usuários cadastrados** e processar o envio/registro de e-mails. Atua como **Consumer** no fluxo de mensageria com o RabbitMQ.

## Tecnologias

- Java 21
- Spring Boot 4.1.0 (Web, Data JPA)
- PostgreSQL
- RabbitMQ (Spring AMQP)
- Docker
- Maven

## Estrutura do projeto

```
com.ms.email
├── config
│   └── RabbitMQConfig      -> Declaração da fila e do MessageConverter (JSON) do RabbitMQ
├── consumer
│   └── EmailConsumer        -> Listener que consome mensagens da fila
├── dto
│   └── EmailRecordDto        -> Payload recebido da fila
├── entity
│   └── EmailEntity           -> Entidade JPA (registro do e-mail processado)
├── enums
│   └── StatusEmail            -> Status do processamento do e-mail (ex.: PENDENTE, ENVIADO, ERRO)
├── repository
│   └── EmailRepository       -> Acesso a dados (Spring Data JPA)
└── service
    └── EmailService           -> Regras de negócio de processamento
```

## Fluxo de consumo

1. `EmailConsumer` escuta a fila configurada no RabbitMQ.
2. Ao receber uma mensagem (`EmailDto` publicado pelo `ms-user`), converte para `EmailRecordDto`.
3. `EmailService` processa o registro, define um `StatusEmail` e persiste via `EmailRepository`.

## Configuração do RabbitMQ

O `RabbitMQConfig` declara a fila consumida e o `MessageConverter` (JSON) usado para desserializar as mensagens recebidas:

```java
@Configuration
public class RabbitMQConfig {

    @Value("${broker.queue.email.name}")
    private String queue;

    @Bean
    public Queue queue() {
        return new Queue(queue, true);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
```

## Variáveis de ambiente

Configuradas via `.env` / `application.properties`:

| Variável                  | Descrição                                       |
|---------------------------|-------------------------------------------------|
| `POSTGRES_DB`             | URL de conexão com o PostgreSQL                 |
| `POSTGRES_USER`           | Usuário do banco                                |
| `POSTGRES_PASSWORD`       | Senha do banco                                  |
| `POSTGRES_PORT`           | Porta do banco                                  |
| `SERVER_PORT`             | Porta do Servidor da Aplicação                  |
| `URL_CLOUD_AMQP`    | String de conexão com o cloud AMQP com RabbitMQ |
| `PORTA_EMAIL_SMTP`    | Porta do SMTP do Google                         |
| `USER_EMAIL_SMTP` | Usuário do email                                |
| `PASSWORD_EMAIL_SMTP` | Senha do App do Google                          |

> Veja `.env.example` para o modelo completo.

## Como executar

```bash
docker-compose up -d
./mvnw spring-boot:run
```

> Deve ser executado com o `ms-user` também no ar, já que este serviço apenas reage a eventos publicados por ele.

## Status do e-mail (`StatusEmail`)

O enum controla o ciclo de vida do processamento de cada e-mail recebido (ex.: `PENDENTE`, `ENVIADO`, `ERRO`), permitindo rastrear o que aconteceu com cada mensagem consumida.

## Roadmap / Próximos passos

- [ ] **Spring Security + JWT**: caso o serviço ganhe endpoints REST próprios, protegê-los com autenticação
- [ ] Retry e Dead Letter Queue (DLQ) para mensagens que falham no processamento
- [ ] Testes unitários e de integração (JUnit, Testcontainers)
- [ ] Documentação de API com OpenAPI/Swagger (se expuser endpoints)