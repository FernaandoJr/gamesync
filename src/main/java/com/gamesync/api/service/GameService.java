// File: src/main/java/com/gamesync/api/service/GameService.java
package com.gamesync.api.service;

import com.gamesync.api.dto.GameCreateDTO; // DTO para criação de novos jogos.
import com.gamesync.api.dto.GameUpdateDTO; // DTO para atualização de jogos existentes.
import com.gamesync.api.exception.BadRequestException;          // Exceção para requisições malformadas ou dados inválidos.
import com.gamesync.api.exception.DuplicateResourceException;  // Exceção para tentativa de criar um recurso que já existe.
import com.gamesync.api.exception.ResourceNotFoundException;   // Exceção para quando um recurso solicitado não é encontrado.
import com.gamesync.api.model.Game;        // Entidade principal que representa um jogo.
import com.gamesync.api.model.GameSource;  // Enum para a origem do jogo (ex: STEAM, MANUAL).
import com.gamesync.api.model.GameStatus;  // Enum para o status do jogo (ex: PLAYING, COMPLETED).
import com.gamesync.api.model.Steam;      // Modelo para armazenar detalhes específicos do Steam.
import com.gamesync.api.model.User;        // Entidade que representa um usuário.
import com.gamesync.api.repository.GameRepository; // Interface para operações CRUD com jogos no MongoDB.
import org.springframework.context.annotation.Lazy; // Usada para resolver dependências circulares na injeção.
import org.springframework.security.core.Authentication; // Representa o token de autenticação do usuário.
import org.springframework.security.core.context.SecurityContextHolder; // Fornece acesso ao contexto de segurança atual.
import org.springframework.stereotype.Service; // Marca esta classe como um serviço gerenciado pelo Spring.
import org.springframework.transaction.annotation.Transactional; // Gerencia a atomicidade das operações de banco de dados.
import java.util.Date;     // Para registrar a data de adição de um jogo.
import java.util.List;     // Interface para coleções de listas.
import java.util.Optional; // Contêiner que pode ou não conter um valor não-nulo.

/**
 * Classe de serviço que encapsula a lógica de negócios para operações relacionadas a jogos.
 * Responsável por criar, buscar, atualizar e excluir jogos, garantindo as regras
 * de negócio e de segurança (ex: um usuário só pode modificar seus próprios jogos).
 */
@Service // Anotação que marca esta classe como um Bean de serviço do Spring.
public class GameService {

    // Dependência do repositório de jogos para interagir com a camada de persistência.
    private final GameRepository gameRepository;
    // Dependência do serviço de usuários, injetada de forma "lazy" para evitar dependência circular.
    private final UserService userService;

    /**
     * Construtor para injeção de dependências.
     * @param gameRepository Repositório para acesso aos dados dos jogos.
     * @param userService Serviço de usuários, injetado com @Lazy para quebrar dependências circulares
     * potenciais durante a inicialização do Spring.
     */
    public GameService(GameRepository gameRepository, @Lazy UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    /**
     * Método privado auxiliar para obter o objeto User do usuário atualmente autenticado
     * a partir do contexto de segurança do Spring.
     * @return O objeto User autenticado.
     * @throws IllegalStateException Se nenhum usuário estiver autenticado ou o tipo do principal for inválido.
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Nenhum usuário autenticado encontrado ou tipo de principal inválido.");
        }
        return (User) authentication.getPrincipal();
    }

    /**
     * Cria um novo jogo para o usuário autenticado.
     * @param createDTO DTO contendo os dados para a criação do jogo.
     * @return O objeto Game recém-criado e salvo no banco de dados.
     * @throws DuplicateResourceException Se o usuário já possuir um jogo com o mesmo nome.
     */
    @Transactional // Garante que a operação de criação seja atômica.
    public Game createGame(GameCreateDTO createDTO) {
        // Obtém o usuário atualmente autenticado.
        User currentUser = getAuthenticatedUser();

        // Verifica se o usuário já possui um jogo com o mesmo nome.
        if (gameRepository.existsByNameAndUserId(createDTO.getName(), currentUser.getId())) {
            throw new DuplicateResourceException("Jogo com o nome '" + createDTO.getName() + "' já existe para este usuário.");
        }

        // Cria uma nova instância da entidade Game.
        Game newGame = new Game();
        // Mapeia os dados do DTO para a entidade.
        newGame.setName(createDTO.getName());
        newGame.setDescription(createDTO.getDescription());
        newGame.setDeveloper(createDTO.getDeveloper());
        newGame.setUserId(currentUser.getId()); // Associa o jogo ao usuário atual.

        // Define horas jogadas, com valor padrão 0 se não fornecido.
        newGame.setHoursPlayed(createDTO.getHoursPlayed() != null ? createDTO.getHoursPlayed() : 0);
        newGame.setFavorite(createDTO.isFavorite());

        // Define listas de gêneros, tags e plataformas, se fornecidas.
        if (createDTO.getGenres() != null) {
            newGame.setGenres(createDTO.getGenres());
        }
        if (createDTO.getTags() != null) {
            newGame.setTags(createDTO.getTags());
        }
        if (createDTO.getPlatforms() != null) {
            newGame.setPlatforms(createDTO.getPlatforms());
        }

        newGame.setStatus(createDTO.getStatus());
        // Define a origem do jogo, com padrão MANUAL se não especificado.
        newGame.setSource(createDTO.getSource() != null ? createDTO.getSource() : GameSource.MANUAL);
        newGame.setAddedAt(new Date()); // Define a data de adição como a data atual.

        // Se a origem for STEAM e os detalhes do Steam forem fornecidos, mapeia-os.
        if (createDTO.getSource() == GameSource.STEAM && createDTO.getSteam() != null) {
            Steam steamDetails = new Steam();
            steamDetails.setAppId(createDTO.getSteam().getAppId());
            steamDetails.setStoreUrl(createDTO.getSteam().getStoreUrl());
            steamDetails.setHeaderImageUrl(createDTO.getSteam().getHeaderImageUrl());
            steamDetails.setAchievementCompletion(createDTO.getSteam().getAchievementCompletion());
            newGame.setSteam(steamDetails);
        } else {
            // Se não for STEAM ou os detalhes não forem fornecidos, garante que a origem seja MANUAL e steam seja nulo.
            newGame.setSource(GameSource.MANUAL);
            newGame.setSteam(null);
        }

        // Salva o novo jogo no banco de dados.
        return gameRepository.save(newGame);
    }

    /**
     * Busca todos os jogos pertencentes ao usuário atualmente autenticado.
     * @return Uma lista de objetos Game.
     */
    public List<Game> findAllGamesByCurrentUser() {
        User currentUser = getAuthenticatedUser();
        // Utiliza o método do repositório para buscar jogos pelo ID do usuário.
        return gameRepository.findByUserId(currentUser.getId());
    }

    /**
     * Busca um jogo específico pelo seu ID, garantindo que ele pertença ao usuário autenticado.
     * @param gameId O ID do jogo a ser buscado.
     * @return Um Optional contendo o Game, se encontrado e pertencente ao usuário, ou Optional.empty() caso contrário.
     */
    public Optional<Game> findGameByIdAndCurrentUser(String gameId) {
        User currentUser = getAuthenticatedUser();
        Optional<Game> gameOpt = gameRepository.findById(gameId);
        // Verifica se o jogo foi encontrado e se o userId do jogo corresponde ao ID do usuário autenticado.
        if (gameOpt.isPresent() && gameOpt.get().getUserId().equals(currentUser.getId())) {
            return gameOpt;
        }
        return Optional.empty(); // Retorna vazio se não atender às condições.
    }

    /**
     * Atualiza os dados de um jogo existente.
     * Apenas o usuário que possui o jogo pode atualizá-lo.
     * @param gameId O ID do jogo a ser atualizado.
     * @param updateDTO DTO contendo os dados a serem atualizados.
     * @return Um Optional contendo o Game atualizado, ou Optional.empty() se não for encontrado ou o acesso for negado.
     * @throws ResourceNotFoundException Se o jogo não pertencer ao usuário autenticado.
     * @throws DuplicateResourceException Se a alteração do nome resultar em um nome duplicado para o usuário.
     */
    @Transactional // Garante que a operação de atualização seja atômica.
    public Optional<Game> updateGame(String gameId, GameUpdateDTO updateDTO) {
        User currentUser = getAuthenticatedUser();
        // Busca o jogo pelo ID.
        return gameRepository.findById(gameId)
                .map(existingGame -> { // Se o jogo existir, processa a atualização.
                    // Verifica se o jogo pertence ao usuário autenticado.
                    if (!existingGame.getUserId().equals(currentUser.getId())) {
                        throw new ResourceNotFoundException("Jogo não encontrado ou acesso negado.");
                    }

                    // Atualiza o nome se fornecido, não estiver em branco e for diferente do nome atual.
                    // Também verifica duplicidade se o nome for alterado.
                    if (updateDTO.getName() != null && !updateDTO.getName().isBlank()) {
                        if (!existingGame.getName().equalsIgnoreCase(updateDTO.getName()) &&
                                gameRepository.existsByNameAndUserId(updateDTO.getName(), currentUser.getId())) {
                            throw new DuplicateResourceException("Outro jogo com o nome '" + updateDTO.getName() + "' já existe para este usuário.");
                        }
                        existingGame.setName(updateDTO.getName());
                    }
                    // Atualiza os demais campos se eles forem fornecidos no DTO.
                    if (updateDTO.getDescription() != null) {
                        existingGame.setDescription(updateDTO.getDescription());
                    }
                    if (updateDTO.getDeveloper() != null && !updateDTO.getDeveloper().isBlank()) {
                        existingGame.setDeveloper(updateDTO.getDeveloper());
                    }
                    if (updateDTO.getHoursPlayed() != null) {
                        existingGame.setHoursPlayed(updateDTO.getHoursPlayed());
                    }
                    if (updateDTO.getFavorite() != null) {
                        existingGame.setFavorite(updateDTO.getFavorite());
                    }
                    if (updateDTO.getGenres() != null) {
                        existingGame.setGenres(updateDTO.getGenres());
                    }
                    if (updateDTO.getTags() != null) {
                        existingGame.setTags(updateDTO.getTags());
                    }
                    if (updateDTO.getPlatforms() != null) {
                        existingGame.setPlatforms(updateDTO.getPlatforms());
                    }
                    if (updateDTO.getStatus() != null) {
                        existingGame.setStatus(updateDTO.getStatus());
                    }

                    // Atualiza os detalhes do Steam se fornecidos.
                    if (updateDTO.getSteam() != null) {
                        Steam steamDetails = existingGame.getSteam() != null ? existingGame.getSteam() : new Steam();
                        if (updateDTO.getSteam().getAppId() != null) steamDetails.setAppId(updateDTO.getSteam().getAppId());
                        if (updateDTO.getSteam().getStoreUrl() != null) steamDetails.setStoreUrl(updateDTO.getSteam().getStoreUrl());
                        if (updateDTO.getSteam().getHeaderImageUrl() != null) steamDetails.setHeaderImageUrl(updateDTO.getSteam().getHeaderImageUrl());
                        if (updateDTO.getSteam().getAchievementCompletion() != null) steamDetails.setAchievementCompletion(updateDTO.getSteam().getAchievementCompletion());
                        existingGame.setSteam(steamDetails);
                        // Se detalhes do Steam são atualizados, pode ser necessário reavaliar a origem do jogo.
                        // Esta lógica pode precisar de refinamento dependendo das regras de negócio.
                        if (existingGame.getSource() == null || existingGame.getSource() != GameSource.STEAM) {
                            // Se um appId for fornecido, idealmente a origem deveria ser STEAM.
                            // Por simplicidade, está como MANUAL, mas poderia ser alterado para STEAM se dados do Steam forem adicionados.
                            existingGame.setSource(GameSource.MANUAL);
                        }
                    }

                    // Salva o jogo atualizado no banco de dados.
                    return gameRepository.save(existingGame);
                });
    }

    /**
     * Exclui um jogo.
     * Apenas o usuário que possui o jogo pode excluí-lo.
     * @param gameId O ID do jogo a ser excluído.
     * @return true se o jogo foi excluído com sucesso, false se o jogo não foi encontrado.
     * @throws ResourceNotFoundException Se o jogo pertencer a outro usuário.
     */
    @Transactional // Garante que a operação de exclusão seja atômica.
    public boolean deleteGame(String gameId) {
        User currentUser = getAuthenticatedUser();
        Optional<Game> gameOpt = gameRepository.findById(gameId);

        if (gameOpt.isPresent()) { // Se o jogo existir.
            // Verifica se o jogo pertence ao usuário autenticado.
            if (gameOpt.get().getUserId().equals(currentUser.getId())) {
                gameRepository.deleteById(gameId); // Exclui o jogo.
                return true; // Retorna true indicando sucesso.
            } else {
                // Se o jogo não pertence ao usuário, lança exceção.
                throw new ResourceNotFoundException("Jogo não encontrado ou acesso negado para exclusão.");
            }
        }
        return false; // Retorna false se o jogo não foi encontrado.
    }

    /**
     * Exclui todos os jogos associados a um ID de usuário específico.
     * Este método é tipicamente chamado quando um usuário está sendo excluído do sistema
     * (pelo UserService) para limpar dados relacionados.
     * @param userId O ID do usuário cujos jogos serão excluídos.
     */
    @Transactional // Garante que a operação de exclusão em lote seja atômica.
    public void deleteAllGamesByUserId(String userId) {
        // Busca todos os jogos associados ao ID do usuário.
        List<Game> userGames = gameRepository.findByUserId(userId);
        // Se existirem jogos, exclui todos eles.
        if (!userGames.isEmpty()) {
            gameRepository.deleteAll(userGames);
        }
    }
}