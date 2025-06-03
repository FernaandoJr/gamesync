package com.gamesync.api.service;

import com.gamesync.api.dto.GameCreateDTO;
import com.gamesync.api.dto.GameUpdateDTO;
import com.gamesync.api.exception.BadRequestException;
import com.gamesync.api.exception.DuplicateResourceException;
import com.gamesync.api.exception.ResourceNotFoundException;
import com.gamesync.api.model.Game;
import com.gamesync.api.model.GameSource;
import com.gamesync.api.model.GameStatus;
import com.gamesync.api.model.Steam;
import com.gamesync.api.model.User;
import com.gamesync.api.repository.GameRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserService userService;

    public GameService(GameRepository gameRepository, @Lazy UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Nenhum usuário autenticado encontrado ou tipo de principal inválido.");
        }
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public Game createGame(GameCreateDTO createDTO) {
        User currentUser = getAuthenticatedUser();

        if (gameRepository.existsByNameAndUserId(createDTO.getName(), currentUser.getId())) {
            throw new DuplicateResourceException("Jogo com o nome '" + createDTO.getName() + "' já existe para este usuário.");
        }

        Game newGame = new Game();
        newGame.setName(createDTO.getName());
        newGame.setDescription(createDTO.getDescription());
        newGame.setDeveloper(createDTO.getDeveloper());
        newGame.setUserId(currentUser.getId());

        newGame.setHoursPlayed(createDTO.getHoursPlayed() != null ? createDTO.getHoursPlayed() : 0);
        newGame.setFavorite(createDTO.isFavorite());

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
        newGame.setSource(createDTO.getSource() != null ? createDTO.getSource() : GameSource.MANUAL);
        newGame.setAddedAt(new Date());

        if (createDTO.getSource() == GameSource.STEAM && createDTO.getSteam() != null) {
            Steam steamDetails = new Steam();
            steamDetails.setAppId(createDTO.getSteam().getAppId());
            steamDetails.setStoreUrl(createDTO.getSteam().getStoreUrl());
            steamDetails.setHeaderImageUrl(createDTO.getSteam().getHeaderImageUrl());
            steamDetails.setAchievementCompletion(createDTO.getSteam().getAchievementCompletion());
            newGame.setSteam(steamDetails);
        } else {
            newGame.setSource(GameSource.MANUAL);
            newGame.setSteam(null);
        }

        return gameRepository.save(newGame);
    }

    public List<Game> findAllGamesByCurrentUser() {
        User currentUser = getAuthenticatedUser();
        return gameRepository.findByUserId(currentUser.getId());
    }

    public Optional<Game> findGameByIdAndCurrentUser(String gameId) {
        User currentUser = getAuthenticatedUser();
        Optional<Game> gameOpt = gameRepository.findById(gameId);
        if (gameOpt.isPresent() && gameOpt.get().getUserId().equals(currentUser.getId())) {
            return gameOpt;
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Game> updateGame(String gameId, GameUpdateDTO updateDTO) {
        User currentUser = getAuthenticatedUser();
        return gameRepository.findById(gameId)
                .map(existingGame -> {
                    if (!existingGame.getUserId().equals(currentUser.getId())) {
                        throw new ResourceNotFoundException("Jogo não encontrado ou acesso negado.");
                    }

                    if (updateDTO.getName() != null && !updateDTO.getName().isBlank()) {
                        if (!existingGame.getName().equalsIgnoreCase(updateDTO.getName()) &&
                                gameRepository.existsByNameAndUserId(updateDTO.getName(), currentUser.getId())) {
                            throw new DuplicateResourceException("Outro jogo com o nome '" + updateDTO.getName() + "' já existe para este usuário.");
                        }
                        existingGame.setName(updateDTO.getName());
                    }
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

                    if (updateDTO.getSteam() != null) {
                        Steam steamDetails = existingGame.getSteam() != null ? existingGame.getSteam() : new Steam();
                        if (updateDTO.getSteam().getAppId() != null) steamDetails.setAppId(updateDTO.getSteam().getAppId());
                        if (updateDTO.getSteam().getStoreUrl() != null) steamDetails.setStoreUrl(updateDTO.getSteam().getStoreUrl());
                        if (updateDTO.getSteam().getHeaderImageUrl() != null) steamDetails.setHeaderImageUrl(updateDTO.getSteam().getHeaderImageUrl());
                        if (updateDTO.getSteam().getAchievementCompletion() != null) steamDetails.setAchievementCompletion(updateDTO.getSteam().getAchievementCompletion());
                        existingGame.setSteam(steamDetails);
                        if (existingGame.getSource() == null || existingGame.getSource() != GameSource.STEAM) {
                            existingGame.setSource(GameSource.MANUAL); // Ou STEAM se o appId for fornecido e válido
                        }
                    }

                    return gameRepository.save(existingGame);
                });
    }

    @Transactional
    public boolean deleteGame(String gameId) {
        User currentUser = getAuthenticatedUser();
        Optional<Game> gameOpt = gameRepository.findById(gameId);

        if (gameOpt.isPresent()) {
            if (gameOpt.get().getUserId().equals(currentUser.getId())) {
                gameRepository.deleteById(gameId);
                return true;
            } else {
                throw new ResourceNotFoundException("Jogo não encontrado ou acesso negado para exclusão.");
            }
        }
        return false;
    }

    @Transactional
    public void deleteAllGamesByUserId(String userId) {
        List<Game> userGames = gameRepository.findByUserId(userId);
        if (!userGames.isEmpty()) {
            gameRepository.deleteAll(userGames);
        }
    }
}