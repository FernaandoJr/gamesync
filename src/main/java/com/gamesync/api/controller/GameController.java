package com.gamesync.api.controller;

import com.gamesync.api.dto.ErrorResponse;
import com.gamesync.api.dto.GameCreateDTO;
import com.gamesync.api.dto.GameUpdateDTO;
import com.gamesync.api.exception.ResourceNotFoundException;
import com.gamesync.api.model.Game;
import com.gamesync.api.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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

    @PostMapping
    @Operation(summary = "Cria um novo jogo",
            description = "Adiciona um novo jogo à coleção do usuário.",
            // Adicionando a documentação do header aqui
            responses = {
                    @ApiResponse(responseCode = "201", description = "Jogo criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Não autenticado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameCreateDTO createDTO) {
        Game createdGame = gameService.createGame(createDTO);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGamesForCurrentUser() {
        List<Game> games = gameService.findAllGamesByCurrentUser();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Game game = gameService.findGameByIdAndCurrentUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo com ID '" + id + "' não encontrado ou acesso negado."));
        return ResponseEntity.ok(game);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable String id, @Valid @RequestBody GameUpdateDTO updateDTO) {
        Game updatedGame = gameService.updateGame(id, updateDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao atualizar. Jogo com ID '" + id + "' não encontrado ou acesso negado."));
        return ResponseEntity.ok(updatedGame);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        if (gameService.deleteGame(id)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Falha ao excluir. Jogo com ID '" + id + "' não encontrado ou acesso negado.");
        }
    }
}