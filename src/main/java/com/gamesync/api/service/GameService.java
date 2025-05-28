package com.gamesync.api.service;

import com.gamesync.api.model.Game;
import com.gamesync.api.model.GameSource;
import com.gamesync.api.repository.GameRepository;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication; // Importe esta
import org.springframework.security.core.context.SecurityContextHolder; // Importe esta
import com.gamesync.api.model.User; // Importe sua classe User

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }


    public Optional<Game> findGameById(String id) {
        return gameRepository.findById(id);
    }

    // --- NOVO MÉTODO AUXILIAR PARA OBTER O USUÁRIO AUTENTICADO ---
    private User getAuthenticatedUser() {
        // Obtém o objeto Authentication do contexto de segurança.
        // Este objeto contém os detalhes do usuário autenticado.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se há um usuário autenticado e se ele é do tipo User (nosso modelo).
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        // Se não houver usuário autenticado ou se não for do tipo esperado,
        // pode lançar uma exceção ou retornar null, dependendo da sua lógica.
        // Para este cenário, é melhor garantir que só se chame isso em endpoints protegidos.
        throw new IllegalStateException("Nenhum usuário autenticado encontrado no contexto de segurança.");
    }


    public Game createGame(Game game) {
        try {
            game.setAddedAt(new Date());
        } catch(Exception e) {
            throw new RuntimeException("Erro ao definir a data de adição do jogo: " + e.getMessage());
        }

        User currentUser = getAuthenticatedUser();
        game.setUserId(currentUser.getId()); // Define o userId do jogo

        if (game.getSource() != GameSource.STEAM) {
            game.setSteam(null);
        }
        return gameRepository.save(game);
    }

    public Optional<Game> updateGame(String id, Game updatedGame) { // <-- ID agora é String
        return gameRepository.findById(id)
                .map(existingGame -> {
                    existingGame.setName(updatedGame.getName());
                    existingGame.setDescription(updatedGame.getDescription());
                    existingGame.setDeveloper(updatedGame.getDeveloper());
                    existingGame.setHoursPlayed(updatedGame.getHoursPlayed());
                    existingGame.setFavorite(updatedGame.isFavorite());
                    existingGame.setGenres(updatedGame.getGenres());
                    existingGame.setTags(updatedGame.getTags());
                    existingGame.setPlatforms(updatedGame.getPlatforms());
                    existingGame.setAddedAt(updatedGame.getAddedAt()); // Considere se isso deve ser atualizado ou se é só na criação
                    existingGame.setStatus(updatedGame.getStatus());
                    existingGame.setSource(updatedGame.getSource());
                    existingGame.setSteam(updatedGame.getSteam());
                    return gameRepository.save(existingGame); // Salva o jogo atualizado
                });
    }


    public boolean deleteGame(String id) { // <-- ID agora é String
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }
}