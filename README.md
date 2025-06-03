# GameSync API

## Descrição
GameSync é uma API RESTful em Java com Spring Boot que agrega informações de jogos, permitindo que o usuário visualize seus jogos cadastrados em uma plataforma unificada.

## Funcionalidades Principais
* **Gerenciamento de Usuários**:
    * Registro de novos usuários com validação de dados (username, email, senha).
    * Autenticação de usuários para acesso aos endpoints protegidos.
    * Visualização do perfil do usuário autenticado (`/users/me`).
    * Visualização de perfis de usuário por ID (com restrições de acesso).
    * Atualização dos dados do perfil do usuário (username, email, senha).
    * Exclusão da conta de usuário (remove o usuário e todos os seus jogos associados).
* **Gerenciamento de Jogos**:
    * Adição de novos jogos à biblioteca pessoal do usuário, com detalhes como nome, desenvolvedor, descrição, status, horas jogadas, etc.
    * Suporte para jogos adicionados manualmente ou com informações da Steam.
    * Listagem de todos os jogos da biblioteca do usuário autenticado.
    * Visualização de detalhes de um jogo específico por ID.
    * Atualização dos detalhes de um jogo existente.
    * Exclusão de jogos da biblioteca do usuário.
* **Segurança**:
    * Endpoints protegidos que requerem autenticação.
    * Codificação de senhas usando BCrypt.
* **Validação de Dados**:
    * Validação de dados de entrada para DTOs (Data Transfer Objects) usando Jakarta Bean Validation.
* **Tratamento de Erros Padronizado**:
    * Respostas de erro consistentes em formato JSON para diferentes tipos de exceções.
* **Documentação da API**:
    * Geração automática de documentação interativa da API com Swagger/OpenAPI.

## Tecnologias Utilizadas
* **Linguagem**: Java 17
* **Framework Principal**: Spring Boot 3.2.5
* **Segurança**: Spring Security
* **Acesso a Dados**: Spring Data MongoDB
* **Banco de Dados**: MongoDB
* **API Web**: Spring MVC (REST Controllers)
* **Validação**: Jakarta Bean Validation (via `spring-boot-starter-validation`)
* **Documentação da API**: Springdoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
* **Build e Gerenciamento de Dependências**: Apache Maven
* **Gerenciamento de Variáveis de Ambiente (Local)**: `java-dotenv` (opcional, para desenvolvimento local)

## Pré-requisitos
Antes de executar o projeto, certifique-se de ter o seguinte instalado:
* JDK 17 ou superior.
* Apache Maven 3.6.x ou superior.
* MongoDB Server (versão 4.x ou superior recomendada, com suporte a transações se a funcionalidade `@Transactional` for crítica para todas as operações).

## Configuração e Execução do Projeto

### 1. Clonar o Repositório
```bash
git clone https://github.com/FernaandoJr/gamesync.git
cd gamesync
````

### 2\. Configurar Variáveis de Ambiente

A aplicação espera uma URI de conexão com o MongoDB. Esta pode ser configurada através da variável de ambiente `SPRING_DATA_MONGODB_URI`.

Crie um arquivo `.env` na raiz do projeto (se estiver usando `java-dotenv` para desenvolvimento local, conforme configurado na classe `GameSyncApiApplication`) com o seguinte conteúdo:

```env
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/gamesync_db
```

Substitua `mongodb://localhost:27017/gamesync_db` pela sua string de conexão do MongoDB, se for diferente. `gamesync_db` é um exemplo de nome de banco de dados.

Alternativamente, se a variável de ambiente `SPRING_DATA_MONGODB_URI` não for definida e o `java-dotenv` não for usado, o Spring Boot tentará usar qualquer URI definida diretamente no `application.properties`. A configuração padrão que estabelecemos anteriormente no `application.properties` para desenvolvimento local é:
`spring.data.mongodb.uri=${SPRING_DATA_MONGODB_URI:mongodb://localhost:27017/gamesync_refactored}`
Isso significa que se `SPRING_DATA_MONGODB_URI` não estiver no ambiente, ele usará `mongodb://localhost:27017/gamesync_refactored`.

### 3\. Construir o Projeto

Use o Maven para construir o projeto e baixar todas as dependências:

```bash
mvn clean install
```

### 4\. Executar a Aplicação

Você pode executar a aplicação usando o plugin do Maven para Spring Boot:

```bash
mvn spring-boot:run
```

Ou, após construir o JAR com `mvn install`, você pode executá-lo diretamente:

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

Por padrão, a API estará disponível em `http://localhost:8080`.

## Documentação da API (Swagger)

Após iniciar a aplicação, a documentação interativa da API (Swagger UI) estará disponível em:

* [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

A especificação OpenAPI v3 em formato JSON pode ser acessada em:

* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

A autenticação HTTP Basic pode ser configurada diretamente na interface do Swagger UI através do botão "Authorize" para testar os endpoints protegidos.

## Endpoints da API

Todos os endpoints requerem autenticação HTTP Basic, exceto o de registro de usuário.

### Usuários (`/users`)

* **`POST /users/register`**: Registra um novo usuário.
    * Corpo da Requisição: `UserRegistrationDTO`
    * Resposta: `201 Created` com o `User` criado (sem senha).
* **`GET /users/me`**: Retorna os detalhes do perfil do usuário autenticado.
    * Resposta: `200 OK` com o `User` (sem senha).
* **`GET /users/{id}`**: Retorna os detalhes de um usuário específico por ID.
    * Resposta: `200 OK` com o `User` (sem senha), ou `404 Not Found`.
* **`PUT /users/{id}`**: Atualiza os dados de um usuário.
    * Corpo da Requisição: `UserUpdateDTO`
    * Resposta: `200 OK` com o `User` atualizado (sem senha), ou `404 Not Found`.
* **`DELETE /users/{id}`**: Exclui um usuário.
    * Resposta: `200 OK` (sem corpo), ou `404 Not Found`.

### Jogos (`/games`)

* **`POST /games`**: Adiciona um novo jogo à biblioteca do usuário autenticado.
    * Corpo da Requisição: `GameCreateDTO`
    * Resposta: `201 Created` com o `Game` criado.
* **`GET /games`**: Lista todos os jogos da biblioteca do usuário autenticado.
    * Resposta: `200 OK` com uma lista de `Game`.
* **`GET /games/{id}`**: Retorna os detalhes de um jogo específico por ID, pertencente ao usuário autenticado.
    * Resposta: `200 OK` com o `Game`, ou `404 Not Found`.
* **`PUT /games/{id}`**: Atualiza os detalhes de um jogo existente, pertencente ao usuário autenticado.
    * Corpo da Requisição: `GameUpdateDTO`
    * Resposta: `200 OK` com o `Game` atualizado, ou `404 Not Found`.
* **`DELETE /games/{id}`**: Exclui um jogo da biblioteca do usuário autenticado.
    * Resposta: `200 OK` (sem corpo), ou `404 Not Found`.

## Autenticação

A API utiliza **Autenticação HTTP Basic**. Os clientes devem enviar um cabeçalho `Authorization` com as credenciais codificadas em Base64 (formato `Basic <base64_encoded_username:password>`) para acessar os endpoints protegidos. A configuração de segurança é gerenciada pela classe `SecurityConfig.java`.

## Tratamento de Erros

A aplicação utiliza um `GlobalExceptionHandler` para fornecer respostas de erro padronizadas em formato JSON, utilizando o `ErrorResponse` DTO. Os códigos de status HTTP comuns incluem:

* `400 Bad Request`: Para dados de entrada inválidos ou falhas de validação.
* `401 Unauthorized`: Se a autenticação falhar ou for necessária.
* `404 Not Found`: Se um recurso solicitado não existir.
* `409 Conflict`: Se houver uma tentativa de criar um recurso que já existe (ex: username duplicado).
* `500 Internal Server Error`: Para erros inesperados no servidor.

## Estrutura do Projeto

O projeto segue uma estrutura padrão para aplicações Spring Boot:

* `com.gamesync.api`
    * `config`: Classes de configuração (ex: `SecurityConfig`).
    * `controller`: Controladores REST que manipulam as requisições HTTP (ex: `UserController`, `GameController`).
    * `dto`: Data Transfer Objects para encapsular dados de requisição e resposta (ex: `UserRegistrationDTO`, `GameCreateDTO`, `ErrorResponse`).
    * `exception`: Classes de exceção customizadas e o `GlobalExceptionHandler`.
    * `model`: Entidades de domínio que são mapeadas para o banco de dados (ex: `User`, `Game`).
    * `repository`: Interfaces do Spring Data MongoDB para acesso a dados (ex: `UserRepository`, `GameRepository`).
    * `service`: Classes de serviço que contêm a lógica de negócios (ex: `UserService`, `GameService`, `CustomUserDetailsService`).
* `src/main/resources`: Contém arquivos de configuração como `application.properties` e o arquivo `.env` (se usado).

## Configurações Adicionais

* **java-dotenv**: A dependência `io.github.cdimascio:java-dotenv` está incluída para facilitar o carregamento de variáveis de ambiente de um arquivo `.env` durante o desenvolvimento local. A classe `GameSyncApiApplication` do seu projeto original continha lógica para carregar essas variáveis; se você manteve essa lógica no projeto refeito, o arquivo `.env` será utilizado.
