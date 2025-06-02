package com.gamesync.api.controller;

import com.gamesync.api.dto.ErrorResponse;
import com.gamesync.api.model.Game;
import com.gamesync.api.model.GameSource;
import com.gamesync.api.model.GameStatus;
import com.gamesync.api.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.findAllGames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        return gameService.findGameById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->{
                            ErrorResponse error = new ErrorResponse("Jogo não encontrado!", HttpStatus.NOT_FOUND.value());
                            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
                        });
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        if (game.getName() == null || game.getName().isBlank()) {
            ErrorResponse error = new ErrorResponse("O nome do jogo é obrigatório.", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }

        if (game.getDeveloper() == null || game.getDeveloper().isBlank()) {
            ErrorResponse error = new ErrorResponse("O desenvolvedor do jogo é obrigatório.", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }

        if (game.getStatus() == null || !java.util.Arrays.asList(GameStatus.values()).contains(game.getStatus())) {
            ErrorResponse error = new ErrorResponse("O status do jogo é obrigatório ou inválido.", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }

        List<Game> userGames = gameService.findAllGames();
        boolean gameExists = userGames.stream()
                .anyMatch(existingGame ->
                                existingGame.getName() != null && existingGame.getName().equalsIgnoreCase(game.getName())
                );
        if (gameExists) {
            ErrorResponse error = new ErrorResponse("Jogo com este nome já existe para este usuário.", HttpStatus.CONFLICT.value());
            return new ResponseEntity(error, HttpStatus.CONFLICT);
        }

        try {
            Game createdGame = gameService.createGame(game);
            return ResponseEntity.ok(createdGame);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable String id, @RequestBody Game updated) {
        return gameService.updateGame(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        if (gameService.deleteGame(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}