package com.gamesync.api.repository;
import com.gamesync.api.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interface de repositório para a entidade Game.
 * Estende MongoRepository para herdar métodos CRUD (Create, Read, Update, Delete)
 * e outras funcionalidades de busca para interagir com a coleção 'games' no MongoDB.
 * Métodos de busca customizados também podem ser definidos aqui, e o Spring Data MongoDB
 * os implementará automaticamente com base em suas assinaturas (nomes dos métodos).
 */
@Repository
public interface GameRepository extends MongoRepository<Game, String> {

    /**
     * Busca e retorna uma lista de todos os jogos associados a um ID de usuário específico.
     * O Spring Data MongoDB criará automaticamente a implementação deste método
     * com base no nome do método ("findBy" seguido pelo nome do campo "UserId").
     * @param userId O ID do usuário cujos jogos devem ser recuperados.
     * @return Uma lista de objetos Game pertencentes ao usuário especificado. Pode ser uma lista vazia se o usuário não tiver jogos.
     */
    List<Game> findByUserId(String userId); //

    /**
     * Verifica se existe um jogo com um nome específico associado a um ID de usuário específico.
     * Útil para evitar nomes de jogos duplicados para o mesmo usuário.
     * O Spring Data MongoDB criará automaticamente a implementação deste método
     * com base no nome do método ("existsBy" seguido pelos nomes dos campos "Name" e "UserId").
     * @param name O nome do jogo a ser verificado.
     * @param userId O ID do usuário ao qual o jogo estaria associado.
     * @return true se um jogo com o nome e userId fornecidos existir, false caso contrário.
     */
    boolean existsByNameAndUserId(String name, String userId); //
}