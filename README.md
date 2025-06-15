# GameSync API

## Gerenciamento de Biblioteca de Jogos

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.5-green" alt="Spring Boot 3.2.5"/>
  <img src="https://img.shields.io/badge/MongoDB-Database-brightgreen" alt="MongoDB"/>
  <img src="https://img.shields.io/badge/RESTful-API-blue" alt="RESTful API"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="MIT License"/>
</p>

### Integrantes do Projeto:

-   [Fernando Divino de Moraes Júnior](https://github.com/FernaandoJr)
-   [Ian Camargo](https://github.com/IanCamargo)
-   [Luis Felipe Piasentini](https://github.com/LuisPiasentini)
-   [Marcus Fernandes](https://github.com/marcusfernandes)

## 📋 Sumário

-   [Descrição](#-descrição)
-   [Funcionalidades](#-funcionalidades)
-   [Arquitetura](#-arquitetura)
-   [Endpoints da API](#-endpoints-da-api)
-   [Tecnologias Utilizadas](#-tecnologias-utilizadas)
-   [Instalação e Configuração](#-instalação-e-configuração)
-   [Documentação da API](#-documentação-da-api)
-   [Contribuição](#-contribuição)
-   [Licença](#-licença)

## 🎮 Descrição

A GameSync API é uma aplicação RESTful robusta desenvolvida em Java com Spring Boot, que oferece uma solução centralizada para que usuários possam gerenciar suas bibliotecas de jogos digitais. Atuando como uma plataforma unificada, ela permite que cada usuário cadastre, organize e acompanhe seus jogos de forma personalizada.

Esta API utiliza conceitos modernos de design e desenvolvimento, incluindo Programação Orientada a Objetos (POO), arquitetura em camadas, e segue as melhores práticas de desenvolvimento de APIs RESTful. O projeto implementa autenticação segura, validação de dados e manipulação adequada de erros para garantir uma experiência de usuário consistente e confiável.

## 🚀 Funcionalidades

A GameSync API oferece um conjunto abrangente de funcionalidades para gerenciar bibliotecas de jogos, organizadas por entidades e operações CRUD:

### 👤 Gerenciamento de Usuários (`/users`)

-   **Registro de Novos Usuários**

    -   Criação de contas com validação completa de dados (username, email, senha)
    -   Validação para prevenir duplicatas de username e email
    -   Criptografia segura de senhas

-   **Autenticação e Autorização**

    -   Sistema de autenticação via HTTP Basic
    -   Controle de acesso baseado em funções (ROLE_USER, ROLE_ADMIN)
    -   Tokens de autenticação para sessões seguras

-   **Gerenciamento de Perfil**
    -   Visualização do perfil do usuário autenticado (`/users/me`)
    -   Busca de perfis por ID (com restrições de acesso)
    -   Atualização de dados do perfil (username, email, senha)
    -   Exclusão de conta com remoção em cascata dos dados associados

### 🎮 Gerenciamento de Jogos (`/games`)

-   **Biblioteca Personalizada**
    -   Adição de jogos com informações detalhadas:
        -   Nome, desenvolvedor, descrição
        -   Status (jogando, completado, abandonado, lista de desejos)
        -   Horas jogadas, marcação de favoritos
        -   Gêneros, tags e plataformas personalizáveis
    -   Origem do jogo (adição manual)
-   **Organização e Controle**
    -   Listagem completa da biblioteca pessoal
    -   Busca de detalhes de jogos específicos
    -   Atualização de informações dos jogos
    -   Exclusão de jogos da biblioteca

## 🏗️ Arquitetura

A GameSync API segue uma arquitetura moderna em camadas, implementando o padrão **MVC (Model-View-Controller)** adaptado para APIs REST, com forte aderência aos princípios SOLID e de Programação Orientada a Objetos.

### 📚 Camadas da Aplicação

<p align="center">
  <img src="https://i.imgur.com/vFGZeKs.png" alt="Arquitetura em Camadas" width="600"/>
</p>

-   **Controllers** (`com.gamesync.api.controller`)

    -   Recebem e respondem às requisições HTTP
    -   Delegam processamento para a camada de serviços
    -   Gerenciam status codes e headers HTTP
    -   Mapeiam endpoints e métodos HTTP (GET, POST, PUT, DELETE)

-   **Services** (`com.gamesync.api.service`)

    -   Implementam a lógica de negócios da aplicação
    -   Orquestram interações entre múltiplos repositórios
    -   Aplicam regras de validação e manipulação de dados
    -   Gerenciam transações e consistência de dados

-   **Repositories** (`com.gamesync.api.repository`)

    -   Interfaces para operações CRUD nas entidades
    -   Abstraem detalhes de persistência e queries
    -   Implementação automática pelo Spring Data MongoDB
    -   Fornecem métodos personalizados de busca

-   **Models** (`com.gamesync.api.model`)

    -   Entidades de domínio (`User`, `Game`)
    -   Classes POJO com encapsulamento de dados
    -   Mapeamento para documentos MongoDB
    -   Implementação de interfaces de framework (ex: `UserDetails`)

-   **DTOs** (`com.gamesync.api.dto`)

    -   Objetos de transferência de dados entre camadas
    -   Validação de entrada com Jakarta Bean Validation
    -   Separação entre modelos de domínio e contratos de API
    -   Versionamento implícito da API

-   **Exception Handling** (`com.gamesync.api.exception`)
    -   Exceções customizadas por tipo de erro
    -   Manipulador global de exceções para respostas HTTP consistentes
    -   Mensagens de erro padronizadas
    -   Logs detalhados para diagnóstico

### 🧩 Princípios de POO Aplicados

-   **Encapsulamento**

    -   Atributos privados com getters e setters controlados
    -   Validação interna de estado dos objetos
    -   Ocultação da implementação interna dos objetos

-   **Herança e Interfaces**

    -   Implementação de interfaces do Spring (`UserDetails`, `MongoRepository`)
    -   Extensão de classes base para funcionalidades comuns
    -   Separação de contrato e implementação

-   **Polimorfismo**

    -   Implementações substituíveis de interfaces
    -   Comportamento específico por tipo de entidade
    -   Flexibilidade para extensões futuras

-   **Abstração**

    -   Modelagem de conceitos do mundo real (User, Game)
    -   Exposição apenas de operações relevantes
    -   Ocultação de detalhes de implementação

-   **SOLID**
    -   **S**: Classes com responsabilidade única
    -   **O**: Extensibilidade sem modificação
    -   **L**: Substitutibilidade de implementações
    -   **I**: Interfaces específicas e coesas
    -   **D**: Dependência em abstrações

## 🔌 Endpoints da API

Todos os endpoints requerem **Autenticação HTTP Basic**, exceto o de registro de usuário.

### 👤 Usuários (`/users`)

| Método   | Endpoint          | Descrição                                 | Autenticação | Corpo da Requisição   | Respostas                                                                                |
| -------- | ----------------- | ----------------------------------------- | ------------ | --------------------- | ---------------------------------------------------------------------------------------- |
| `POST`   | `/users/register` | Registra um novo usuário                  | Não          | `UserRegistrationDTO` | `201` Created<br>`400` Bad Request<br>`409` Conflict                                     |
| `GET`    | `/users/me`       | Retorna o perfil do usuário logado        | Sim          | -                     | `200` OK<br>`401` Unauthorized                                                           |
| `GET`    | `/users/{id}`     | Retorna detalhes de um usuário específico | Sim          | -                     | `200` OK<br>`401` Unauthorized<br>`404` Not Found                                        |
| `PUT`    | `/users/{id}`     | Atualiza dados de usuário                 | Sim          | `UserUpdateDTO`       | `200` OK<br>`400` Bad Request<br>`401` Unauthorized<br>`404` Not Found<br>`409` Conflict |
| `DELETE` | `/users/{id}`     | Exclui um usuário                         | Sim          | -                     | `200` OK<br>`401` Unauthorized<br>`404` Not Found                                        |

#### Estrutura do `UserRegistrationDTO`:

```json
{
	"username": "example_user",
	"email": "user@example.com",
	"password": "securePass123"
}
```

#### Estrutura do `UserUpdateDTO`:

```json
{
	"username": "new_username",
	"email": "new_email@example.com",
	"newPassword": "newSecurePass456"
}
```

### 🎮 Jogos (`/games`)

| Método   | Endpoint      | Descrição                   | Autenticação | Corpo da Requisição | Respostas                                                                                |
| -------- | ------------- | --------------------------- | ------------ | ------------------- | ---------------------------------------------------------------------------------------- |
| `POST`   | `/games`      | Adiciona um novo jogo       | Sim          | `GameCreateDTO`     | `201` Created<br>`400` Bad Request<br>`401` Unauthorized<br>`409` Conflict               |
| `GET`    | `/games`      | Lista biblioteca do usuário | Sim          | -                   | `200` OK<br>`401` Unauthorized                                                           |
| `GET`    | `/games/{id}` | Retorna detalhes de um jogo | Sim          | -                   | `200` OK<br>`401` Unauthorized<br>`404` Not Found                                        |
| `PUT`    | `/games/{id}` | Atualiza um jogo            | Sim          | `GameUpdateDTO`     | `200` OK<br>`400` Bad Request<br>`401` Unauthorized<br>`404` Not Found<br>`409` Conflict |
| `DELETE` | `/games/{id}` | Remove um jogo              | Sim          | -                   | `200` OK<br>`401` Unauthorized<br>`404` Not Found                                        |

#### Estrutura do `GameCreateDTO`:

```json
{
	"name": "The Legend of Zelda: Breath of the Wild",
	"description": "Action-adventure game set in an open world.",
	"developer": "Nintendo EPD",
	"hoursPlayed": 120,
	"favorite": true,
	"genres": ["Action", "Adventure", "Open World"],
	"tags": ["Nintendo", "Fantasy", "RPG Elements"],
	"platforms": ["Nintendo Switch", "Wii U"],
	"status": "COMPLETED",
	"source": "MANUAL"
}
```

#### Estrutura do `GameUpdateDTO`:

```json
{
	"name": "The Legend of Zelda: BotW",
	"hoursPlayed": 150,
	"status": "COMPLETED",
	"favorite": true,
	"tags": ["Nintendo", "Fantasy", "RPG Elements", "Masterpiece"]
}
```

## 🛠️ Tecnologias Utilizadas

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/MongoDB-4.4+-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB" />
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger" />
</p>

### Backend

-   **Java 17**: Linguagem de programação moderna com recursos avançados
-   **Spring Boot 3.2.5**: Framework para desenvolvimento simplificado de aplicações Java
-   **Spring Security**: Framework para autenticação e controle de acesso
-   **Spring Data MongoDB**: Abstração para acesso a dados MongoDB
-   **MongoDB**: Banco de dados NoSQL orientado a documentos
-   **Spring MVC**: Controladores REST para a API web
-   **Jakarta Bean Validation**: Validação declarativa de dados
-   **Springdoc OpenAPI 2.1**: Documentação automatizada da API (Swagger UI)
-   **Apache Maven**: Gerenciamento de dependências e build

### Ferramentas de Desenvolvimento

-   **Spring Initializr**: Configuração inicial do projeto
-   **Maven Wrapper**: Execução consistente em diferentes ambientes
-   **java-dotenv**: Gerenciamento de variáveis de ambiente locais

## 🔧 Instalação e Configuração

### Pré-requisitos

<p align="center">
  <img src="https://img.shields.io/badge/JDK-17+-007396?style=for-the-badge&logo=java&logoColor=white" alt="JDK 17+" />
  <img src="https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven 3.6+" />
  <img src="https://img.shields.io/badge/MongoDB-4.4+-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB 4.4+" />
</p>

Antes de iniciar, certifique-se de ter instalado:

-   **JDK 17** ou superior
-   **Apache Maven 3.6.x** ou superior
-   **MongoDB Server** (versão 4.4+ recomendada)

### Passos para Execução

#### 1. Clone o Repositório

```bash
git clone https://github.com/FernaandoJr/gamesync.git
cd gamesync
```

#### 2. Configure o Banco de Dados

Crie um arquivo `.env` na raiz do projeto:

```env
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/gamesync_db
```

> 💡 **Dica:** Substitua a URI conforme a configuração do seu ambiente MongoDB. Se a variável não estiver definida, a aplicação usará a URI padrão `mongodb://localhost:27017/gamesync`.

#### 3. Construa o Projeto

```bash
./mvnw clean install
```

#### 4. Execute a Aplicação

**Usando Maven:**

```bash
./mvnw spring-boot:run
```

**Ou usando o JAR:**

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

**Verificação:** Acesse http://localhost:8080/actuator/health para confirmar que a API está funcionando.

## 📚 Documentação da API

A API é completamente documentada usando o Springdoc OpenAPI (Swagger), permitindo visualização e teste interativo dos endpoints.

### Acessando a Documentação

Após iniciar a aplicação, acesse:

-   **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
-   **Especificação OpenAPI v3 (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Testando Endpoints

1. Acesse o Swagger UI
2. Clique no botão **Authorize** no canto superior direito
3. Insira suas credenciais (para endpoints protegidos)
4. Expanda o endpoint desejado e clique em **Try it out**
5. Preencha os parâmetros necessários e execute

## 👥 Contribuição

Contribuições são bem-vindas! Se você deseja contribuir com o projeto, siga estas etapas:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Implemente suas alterações
4. Execute os testes para garantir que tudo está funcionando
5. Faça commit das alterações (`git commit -m 'Adiciona nova feature'`)
6. Envie para a branch (`git push origin feature/nova-feature`)
7. Abra um Pull Request

### Convenções de Código

-   Siga o padrão de código Java do Google
-   Adicione testes para novas funcionalidades
-   Mantenha a documentação atualizada

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE) - veja o arquivo LICENSE para detalhes.

---

<p align="center">
  <b>GameSync API</b> - Desenvolvido com ❤️ pela equipe GameSync
</p>
