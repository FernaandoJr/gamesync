# GameSync API

## Projeto de POO: Gerenciamento de Biblioteca de Jogos

### Integrantes do Projeto:
* [Fernando Divino de Moraes Júnior](https://github.com/FernaandoJr)
* [Ian Camargo](https://github.com/IanCamargo)
* [Luis Felipe Piasentini](https://github.com/LuisPiasentini)
* Marcus Fernandes

### 1. Descrição do Projeto

A GameSync API é uma aplicação RESTful desenvolvida em Java com Spring Boot, que oferece uma solução centralizada para que usuários possam gerenciar suas bibliotecas de jogos digitais. Atuando como uma plataforma unificada, ela permite que cada usuário cadastre, organize e acompanhe seus jogos, seja manualmente ou integrando informações de plataformas como o Steam. Este projeto demonstra a aplicação de conceitos de Programação Orientada a Objetos (POO), arquitetura em camadas e boas práticas de desenvolvimento de APIs.

### 2. Funcionalidades Principais

A GameSync API oferece as seguintes funcionalidades, organizadas por entidades e operações CRUD:

* **Gerenciamento de Usuários (`/users`)**:
    * **Registro de Novos Usuários**: Criação de contas com validação de dados (username, email, senha, Steam ID opcional).
    * **Autenticação**: Autenticação de usuários via HTTP Basic para acesso a endpoints protegidos.
    * **Consulta de Perfil**: Visualização do perfil do usuário autenticado (`/users/me`).
    * **Consulta por ID**: Busca de perfis de usuário por ID (com restrições de acesso: apenas o próprio usuário ou ADMIN).
    * **Atualização de Dados**: Modificação de informações do perfil (username, email, senha).
    * **Exclusão de Conta**: Remoção completa da conta do usuário e de todos os jogos associados.

* **Gerenciamento de Jogos (`/games`)**:
    * **Adição de Jogos**: Cadastro de novos jogos com detalhes como nome, desenvolvedor, descrição, status (jogando, completado, abandonado, lista de desejos), horas jogadas, favoritismo, gêneros, tags e plataformas.
    * **Origem do Jogo**: Suporte para jogos adicionados manualmente ou com informações específicas do Steam (App ID, URL da loja, etc.).
    * **Listagem de Jogos**: Visualização da biblioteca completa de jogos do usuário autenticado.
    * **Consulta por ID**: Busca de detalhes de um jogo específico por ID, garantindo que pertença ao usuário.
    * **Atualização de Jogos**: Modificação de qualquer informação de um jogo existente na biblioteca do usuário.
    * **Exclusão de Jogos**: Remoção de jogos da biblioteca do usuário.

### 3. Arquitetura e Princípios de POO Aplicados

A arquitetura da GameSync API segue o padrão **MVC (Model-View-Controller)** adaptado para APIs REST (Controller-Service-Repository-Model), com forte aderência aos princípios de POO.

* **Camadas da Aplicação**:
    * **Controllers (`com.gamesync.api.controller`)**: Responsáveis por receber as requisições HTTP, delegar a lógica de negócios aos serviços e retornar as respostas HTTP. Mapeiam as URLs e os métodos HTTP.
    * **Services (`com.gamesync.api.service`)**: Contêm a lógica de negócios da aplicação. Coordenam as operações entre os repositórios e aplicam regras de negócio. Implementam a abstração da complexidade do domínio.
    * **Repositories (`com.gamesync.api.repository`)**: Interfaces que definem operações de acesso a dados (CRUD) para as entidades. O Spring Data MongoDB implementa essas interfaces em tempo de execução, abstraindo os detalhes de persistência.
    * **Models (`com.gamesync.api.model`)**: Representam as entidades de domínio (ex: `User`, `Game`, `Steam`). São classes POJO (Plain Old Java Objects) que encapsulam os dados e seu comportamento.
    * **DTOs (`com.gamesync.api.dto`)**: Data Transfer Objects. Classes usadas para transportar dados entre as camadas da aplicação (especialmente entre Controller e Service/Requisições HTTP). Garantem que apenas os dados necessários sejam expostos e validados.

* **Conceitos de POO em Destaque**:
    * **Encapsulamento**: Dados das entidades (Models) são protegidos por modificadores de acesso (`private`) e acessados/modificados via métodos `getters` e `setters`.
    * **Herança/Implementação de Interfaces**: A classe `User` implementa a interface `UserDetails` do Spring Security, demonstrando herança de contrato para integrar com o framework de segurança.
    * **Polimorfismo**: Utilizado indiretamente pelo Spring com interfaces como `MongoRepository` e `UserDetailsService`, onde diferentes implementações podem ser "plugadas".
    * **Abstração**: As camadas de serviço e repositório abstraem a complexidade da lógica de negócios e da persistência de dados, respectivamente, do controller.
    * **Injeção de Dependência**: O Spring Framework é amplamente utilizado para gerenciar as dependências entre os componentes (controllers recebem serviços, serviços recebem repositórios), promovendo baixo acoplamento e alta coesão.
    * **Tratamento de Exceções**: Uso de exceções customizadas (`ResourceNotFoundException`, `DuplicateResourceException`, `BadRequestException`) e um `GlobalExceptionHandler` para centralizar e padronizar as respostas de erro, aplicando o princípio de *fail-fast* e robustez.

### 4. Endpoints da API

Todos os endpoints requerem **Autenticação HTTP Basic**, exceto o de registro de usuário.

#### Usuários (`/users`)

* **`POST /users/register`**: Registra um novo usuário.
    * **Descrição**: Cria uma nova conta de usuário. Não requer autenticação.
    * **Corpo da Requisição**: `UserRegistrationDTO`
    * **Respostas**: `201 Created` (Sucesso), `400 Bad Request` (Validação), `409 Conflict` (Usuário/Email/Steam ID duplicado).
* **`GET /users/me`**: Retorna os detalhes do perfil do usuário autenticado.
    * **Descrição**: Obtém os dados do usuário logado. Requer autenticação.
    * **Respostas**: `200 OK` (Sucesso), `401 Unauthorized`.
* **`GET /users/{id}`**: Retorna os detalhes de um usuário específico por ID.
    * **Descrição**: Busca um usuário por ID. Apenas o próprio usuário ou ADMIN pode acessar. Requer autenticação.
    * **Respostas**: `200 OK` (Sucesso), `401 Unauthorized`, `404 Not Found` (Usuário não encontrado ou acesso negado).
* **`PUT /users/{id}`**: Atualiza os dados de um usuário.
    * **Descrição**: Atualiza o perfil do usuário. Apenas o próprio usuário autenticado pode atualizar. Requer autenticação.
    * **Corpo da Requisição**: `UserUpdateDTO`
    * **Respostas**: `200 OK` (Sucesso), `400 Bad Request`, `401 Unauthorized`, `404 Not Found` (Falha ao atualizar ou acesso negado), `409 Conflict` (Dados duplicados).
* **`DELETE /users/{id}`**: Exclui um usuário.
    * **Descrição**: Remove a conta do usuário e todos os jogos associados. Apenas o próprio usuário autenticado pode excluir. Requer autenticação.
    * **Respostas**: `200 OK` (Sucesso), `401 Unauthorized`, `404 Not Found` (Falha ao excluir ou acesso negado).

#### Jogos (`/games`)

* **`POST /games`**: Adiciona um novo jogo à biblioteca do usuário autenticado.
    * **Descrição**: Cria um novo jogo para o usuário logado. Requer autenticação.
    * **Corpo da Requisição**: `GameCreateDTO`
    * **Respostas**: `201 Created` (Sucesso), `400 Bad Request`, `401 Unauthorized`, `409 Conflict` (Jogo com nome duplicado para o usuário).
* **`GET /games`**: Lista todos os jogos da biblioteca do usuário autenticado.
    * **Descrição**: Retorna todos os jogos do usuário logado. Requer autenticação.
    * **Respostas**: `200 OK` (Sucesso), `401 Unauthorized`.
* **`GET /games/{id}`**: Retorna os detalhes de um jogo específico por ID, pertencente ao usuário autenticado.
    * **Descrição**: Busca um jogo por ID. Apenas o proprietário pode acessar. Requer autenticação.
    * **Respostas**: `200 OK` (Sucesso), `401 Unauthorized`, `404 Not Found` (Jogo não encontrado ou acesso negado).
* **`PUT /games/{id}`**: Atualiza os detalhes de um jogo existente, pertencente ao usuário autenticado.
    * **Descrição**: Atualiza os dados de um jogo. Apenas o proprietário pode modificar. Requer autenticação.
    * **Corpo da Requisição**: `GameUpdateDTO`
    * **Respostas**: `200 OK` (Sucesso), `400 Bad Request`, `401 Unauthorized`, `404 Not Found` (Falha ao atualizar ou acesso negado), `409 Conflict` (Nome de jogo duplicado para o usuário).
* **`DELETE /games/{id}`**: Exclui um jogo da biblioteca do usuário autenticado.
    * **Descrição**: Remove um jogo. Apenas o proprietário pode excluir. Requer autenticação.
    * **Respostas**: `200 OK` (Sucesso), `401 Unauthorized`, `404 Not Found` (Falha ao excluir ou acesso negado).

### 6. Tecnologias Utilizadas

* **Linguagem**: Java 17
* **Framework Principal**: Spring Boot 3.2.5
* **Segurança**: Spring Security (com autenticação HTTP Basic)
* **Acesso a Dados**: Spring Data MongoDB
* **Banco de Dados**: MongoDB
* **API Web**: Spring MVC (REST Controllers)
* **Validação**: Jakarta Bean Validation
* **Documentação da API**: Springdoc OpenAPI (Swagger UI)
* **Build e Gerenciamento de Dependências**: Apache Maven
* **Gerenciamento de Variáveis de Ambiente (Local)**: `java-dotenv` (opcional)

### 7. Pré-requisitos

Antes de executar o projeto, certifique-se de ter o seguinte instalado:

* JDK 17 ou superior.
* Apache Maven 3.6.x ou superior.
* MongoDB Server (versão 4.x ou superior recomendada).

### 8. Configuração e Execução do Projeto

#### 8.1. Clonar o Repositório

```bash
git clone https://github.com/FernaandoJr/gamesync.git
cd gamesync
````

#### 8.2. Configurar Variáveis de Ambiente

A aplicação espera uma URI de conexão com o MongoDB. Crie um arquivo `.env` na raiz do projeto com o seguinte conteúdo:

```env
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/gamesync_db
```

Substitua `mongodb://localhost:27017/gamesync_db` pela sua string de conexão do MongoDB, se for diferente. Caso a variável de ambiente não seja definida, a aplicação usará a URI padrão `mongodb://localhost:27017/gamesync_refactored` conforme configurado no `application.properties`.

#### 8.3. Construir o Projeto

Use o Maven para construir o projeto e baixar todas as dependências:

```bash
mvn clean install
```

#### 8.4. Executar a Aplicação

Você pode executar a aplicação usando o plugin do Maven para Spring Boot:

```bash
mvn spring-boot:run
```

Ou, após construir o JAR com `mvn install`, você pode executá-lo diretamente:

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

Por padrão, a API estará disponível em `http://localhost:8080`.

### 9\. Documentação da API (Swagger UI)

Após iniciar a aplicação, a documentação interativa da API (Swagger UI) estará disponível em:

* [http://localhost:8080/swagger-ui.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui.html)

A especificação OpenAPI v3 em formato JSON pode ser acessada em:

* [http://localhost:8080/v3/api-docs](https://www.google.com/search?q=http://localhost:8080/v3/api-docs)

A autenticação HTTP Basic pode ser configurada diretamente na interface do Swagger UI através do botão "Authorize" para testar os endpoints protegidos. Note que as rotas do Swagger foram configuradas para não exigir autenticação, facilitando o acesso à documentação.

-----

Obrigado por visitar nosso projeto! ❤️