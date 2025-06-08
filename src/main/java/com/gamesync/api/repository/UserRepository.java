package com.gamesync.api.repository;

import com.gamesync.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Interface de repositório para a entidade User.
 * Estende MongoRepository para herdar automaticamente métodos CRUD (Create, Read, Update, Delete)
 * e outras funcionalidades de busca para interagir com a coleção 'users' no MongoDB.
 * Esta interface permite a definição de métodos de busca customizados, que o Spring Data MongoDB
 * implementará em tempo de execução com base em suas assinaturas (nomes dos métodos).
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
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
}