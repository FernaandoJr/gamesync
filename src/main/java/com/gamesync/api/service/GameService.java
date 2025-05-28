package com.gamesync.api.service;

import com.gamesync.api.model.Game;
import com.gamesync.api.repository.GameRepository;
import org.springframework.stereotype.Service;
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


    public Game createGame(Game game) {
        // Define a data de adição no momento da criação.
        // É uma lógica de negócio que reside no serviço, não no controller.
        game.setAddedAt(new Date());
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