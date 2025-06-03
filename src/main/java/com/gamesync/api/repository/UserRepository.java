package com.gamesync.api.repository;

import com.gamesync.api.model.User; // Entidade que representa um usuário no sistema.

import org.springframework.data.mongodb.repository.MongoRepository; // Interface do Spring Data que fornece operações CRUD e de busca para MongoDB.

import org.springframework.stereotype.Repository; // Indica que esta interface é um bean de repositório gerenciado pelo Spring.

import java.util.Optional; // Contêiner que pode ou não conter um valor não-nulo, usado para retornos de busca que podem não encontrar um resultado.

/**
 * Interface de repositório para a entidade User.
 * Estende MongoRepository para herdar automaticamente métodos CRUD (Create, Read, Update, Delete)
 * e outras funcionalidades de busca para interagir com a coleção 'users' no MongoDB.
 * Esta interface permite a definição de métodos de busca customizados, que o Spring Data MongoDB
 * implementará em tempo de execução com base em suas assinaturas (nomes dos métodos).
 */
@Repository // Anotação que marca esta interface como um componente de repositório (um Bean) do Spring.
// Embora seja opcional quando se estende uma interface do Spring Data como MongoRepository
// (pois já é detectado como um repositório), é uma boa prática incluí-la para clareza e consistência.
public interface UserRepository extends MongoRepository<User, String> { //
    // MongoRepository<User, String>:
    //   - 'User': É a classe de domínio (entidade) que este repositório gerencia.
    //   - 'String': É o tipo do campo ID da entidade User (o ID do usuário é uma String).

    /**
     * Busca um usuário pelo seu nome de usuário (username).
     * O Spring Data MongoDB gera automaticamente a query para este método com base no nome "findByUsername".
     * @param username O nome de usuário a ser procurado.
     * @return Um Optional contendo o objeto User se um usuário com o username fornecido for encontrado,
     * ou um Optional vazio caso contrário.
     */
    Optional<User> findByUsername(String username); //

    /**
     * Busca um usuário pelo seu endereço de email.
     * O Spring Data MongoDB gera automaticamente a query para este método com base no nome "findByEmail".
     * @param email O endereço de email a ser procurado.
     * @return Um Optional contendo o objeto User se um usuário com o email fornecido for encontrado,
     * ou um Optional vazio caso contrário.
     */
    Optional<User> findByEmail(String email); //

    /**
     * Busca um usuário pelo seu Steam ID.
     * O Spring Data MongoDB gera automaticamente a query para este método com base no nome "findBySteamId".
     * @param steamId O Steam ID a ser procurado.
     * @return Um Optional contendo o objeto User se um usuário com o Steam ID fornecido for encontrado,
     * ou um Optional vazio caso contrário.
     */
    Optional<User> findBySteamId(String steamId); //

    // Além desses, a interface UserRepository herda vários métodos de MongoRepository, como:
    // - save(User entity): Salva ou atualiza um usuário.
    // - findById(String id): Busca um usuário pelo seu ID.
    // - findAll(): Busca todos os usuários.
    // - deleteById(String id): Deleta um usuário pelo seu ID.
    // - existsById(String id): Verifica se um usuário com um determinado ID existe.
    // - count(): Conta o número total de usuários.
    // E muitos outros.
}