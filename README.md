# Satsflow - P2P Bitcoin Trading Platform

Uma plataforma de trading peer-to-peer de Bitcoin em tempo real com chat integrado, desenvolvida em **Kotlin** com **Spring Boot 3.5** usando programação reativa.

## 🎯 Objetivo
Facilitar a negociação de Bitcoin entre usuários (vendedores e compradores) com criação de ordens, busca de preços em tempo real e chat instantâneo.

## 🛠️ Stack Tecnológico
- **Kotlin** + **Spring Boot 3.5** - Framework principal
- **Spring WebFlux** - API reativa e não-bloqueante
- **R2DBC + PostgreSQL** - Banco de dados reativo
- **Redis** - Cache e pub/sub para mensagens
- **WebSocket** - Comunicação bidirecional em tempo real
- **Kotlin Coroutines** - Programação assíncrona

## 📊 Fluxo Principal

```
1. POST /orders
   → Comprador cria ordem de venda
   → Sistema busca preço BTC atual
   → Ordem salva no banco de dados

2. GET /orders/stream
   → Vendedor recebe ordens em tempo real via SSE
   
3. POST /orders/{id}/accept
   → Vendedor aceita a ordem
   → Status muda para MATCHED
   → Redis notifica as partes
   
4. WS /chat/{orderId}
   → Chat abre entre as partes
   → Histórico de mensagens carregado
   → Mensagens em tempo real via Redis pub/sub
```

## 🗂️ Arquitetura do Projeto

```
src/main/kotlin/br/com/vinisantosdev/satsflow/
├── domain/              # Modelos de dados
│   ├── Order.kt
│   ├── User.kt
│   └── ChatMessage.kt
├── repository/          # Acesso a dados (R2DBC)
│   ├── OrderRepository.kt
│   └── ChatMessageRepository.kt
├── service/             # Lógica de negócio
│   ├── OrderService.kt
│   ├── ChatService.kt
│   └── BitcoinPriceService.kt
└── web/                 # Rotas e handlers
    ├── Router.kt
    ├── OrderHandler.kt
    └── ChatWebSocketHandler.kt
```

## 🚀 Como Começar

### Pré-requisitos
- Java 21+
- PostgreSQL 12+
- Redis 6+
- Gradle

### Instalação e Execução

1. **Clone o repositório**
   ```bash
   git clone <repo>
   cd satsflow
   ```

2. **Configure o banco de dados**
   - PostgreSQL rodando em `localhost:5432`
   - Redis rodando em `localhost:6379`
   - Atualize `application.yml` conforme necessário

3. **Execute a aplicação**
   ```bash
   ./gradlew bootRun
   ```

A aplicação estará disponível em `http://localhost:8080`

## 📚 Referência de Documentação

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.10/gradle-plugin)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/6.2.15/languages/kotlin/coroutines.html)
* [Spring Data R2DBC](https://docs.spring.io/spring-boot/3.5.10/reference/data/sql.html#data.sql.r2dbc)
* [Spring WebFlux Documentation](https://spring.io/projects/spring-webflux)
* [R2DBC Homepage](https://r2dbc.io)
* [Accessing data with R2DBC](https://spring.io/guides/gs/accessing-data-r2dbc/)
