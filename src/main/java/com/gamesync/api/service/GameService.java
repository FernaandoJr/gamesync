package com.gamesync.api.service;

import com.gamesync.api.dto.ErrorResponse;
import com.gamesync.api.model.Game;
import com.gamesync.api.model.GameSource;
import com.gamesync.api.model.GameStatus;
import com.gamesync.api.repository.GameRepository;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication; // Importe esta
import org.springframework.security.core.context.SecurityContextHolder; // Importe esta
import com.gamesync.api.model.User; // Importe sua classe User

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> findAllGames() {
        User currentUser = getAuthenticatedUser();
        return gameRepository.findAll().stream().filter(
                game -> game.getUserId().equals(currentUser.getId())).collect(Collectors.toList()
        );
    }


    public Optional<Game> findGameById(String id) {
        User currentUser = getAuthenticatedUser();

        return gameRepository.findById(id)
                .filter(game -> game.getUserId().equals(currentUser.getId()));
    }

    // --- MÉTODO AUXILIAR PARA OBTER O USUÁRIO AUTENTICADO ---
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
        game.setAddedAt(new Date());

        if(game.getSource() == null || game.getSource() != GameSource.STEAM) {
            game.setSource(GameSource.MANUAL);
        }

        if(game.getHoursPlayed() == null) {
            game.setHoursPlayed(0);
        }

        User currentUser = getAuthenticatedUser();
        game.setUserId(currentUser.getId());

        if(!Arrays.asList(GameStatus.values()).contains(game.getStatus())){
            game.setStatus(null);
        }


        if (game.getSource() != GameSource.STEAM) {
            game.setSteam(null);
        }
        return gameRepository.save(game);
    }

    public Optional<Game> updateGame(String id, Game updatedGame) {
        return gameRepository.findById(id)
                .map(existingGame -> {
                    if(updatedGame.getName() != null && !updatedGame.getName().isEmpty()) {
                        existingGame.setName(updatedGame.getName());
                    }
                    if(updatedGame.getDescription() != null && !updatedGame.getDescription().isEmpty()) {
                        existingGame.setDescription(updatedGame.getDescription());
                    }

                    if(updatedGame.getDeveloper() != null && !updatedGame.getDeveloper().isEmpty()) {
                        existingGame.setDeveloper(updatedGame.getDeveloper());
                    }

                    if(updatedGame.getHoursPlayed() != null) {
                        existingGame.setHoursPlayed(updatedGame.getHoursPlayed());
                    }

                    if(!updatedGame.getFavorite() && existingGame.getFavorite()) {
                        existingGame.setFavorite(updatedGame.getFavorite());
                    }

                    if(updatedGame.getGenres() != null && !updatedGame.getGenres().isEmpty()) {
                        existingGame.setGenres(updatedGame.getGenres());
                    }

                    if(updatedGame.getTags() != null && !updatedGame.getTags().isEmpty()) {
                        existingGame.setTags(updatedGame.getTags());
                    }

                    if(updatedGame.getPlatforms() != null && !updatedGame.getPlatforms().isEmpty()) {
                        existingGame.setPlatforms(updatedGame.getPlatforms());
                    }

                    if(updatedGame.getStatus() != null) {
                        existingGame.setStatus(updatedGame.getStatus());
                    }

                    return gameRepository.save(existingGame);
                });
    }

    public boolean deleteGame(String id) {
        User currentUser = getAuthenticatedUser();
        Optional<Game> game = gameRepository.findById(id);

        if (game.isPresent() && game.get().getUserId().equals(currentUser.getId())) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllGamesByUserId(String userId) {
        List<Game> userGames = gameRepository.findByUserId(userId);
        gameRepository.deleteAll(userGames);
    }
}