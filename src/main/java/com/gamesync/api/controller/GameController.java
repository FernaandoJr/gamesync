// File: src/main/java/com/gamesync/api/controller/GameController.java
package com.gamesync.api.controller;

import com.gamesync.api.dto.ErrorResponse;
import com.gamesync.api.dto.GameCreateDTO;
import com.gamesync.api.dto.GameUpdateDTO;
import com.gamesync.api.exception.ResourceNotFoundException;
import com.gamesync.api.model.Game;
import com.gamesync.api.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller REST para gerenciar operações relacionadas a jogos.
 * Expõe endpoints HTTP para criar, ler, atualizar e excluir (CRUD) jogos,
 * interagindo com a camada de serviço (GameService) para executar a lógica de negócios.
 * Todos os endpoints nesta classe são prefixados com "/games" conforme definido em @RequestMapping.
 */
@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    /**
     * Construtor para injeção de dependência do GameService.
     * @param gameService A instância do serviço de jogos a ser injetada pelo Spring.
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint para criar um novo jogo.
     * Espera um objeto GameCreateDTO no corpo da requisição.
     * @param createDTO DTO contendo os dados do jogo a ser criado. Validado com @Valid.
     * @return ResponseEntity contendo o jogo criado e o status HTTP 201 (Created).
     */
    @PostMapping
    @Operation(summary = "Cria um novo jogo",
            description = "Adiciona um novo jogo à coleção do usuário.",
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
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameCreateDTO createDTO) { //
        Game createdGame = gameService.createGame(createDTO);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }

    /**
     * Endpoint para buscar todos os jogos do usuário atualmente autenticado.
     * @return ResponseEntity contendo uma lista de jogos e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Game>> getAllGamesForCurrentUser() {
        List<Game> games = gameService.findAllGamesByCurrentUser();
        return ResponseEntity.ok(games);
    }

    /**
     * Endpoint para buscar um jogo específico pelo seu ID.
     * O acesso é restrito ao proprietário do jogo.
     * @param id O ID do jogo a ser buscado (passado como variável de caminho).
     * @return ResponseEntity contendo o jogo encontrado e o status HTTP 200 (OK).
     * @throws ResourceNotFoundException se o jogo não for encontrado ou o acesso for negado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Game game = gameService.findGameByIdAndCurrentUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo com ID '" + id + "' não encontrado ou acesso negado."));
        return ResponseEntity.ok(game);
    }

    /**
     * Endpoint para atualizar um jogo existente.
     * O acesso é restrito ao proprietário do jogo.
     * @param id O ID do jogo a ser atualizado (passado como variável de caminho).
     * @param updateDTO DTO contendo os dados a serem atualizados no jogo. Validado com @Valid.
     * @return ResponseEntity contendo o jogo atualizado e o status HTTP 200 (OK).
     * @throws ResourceNotFoundException se o jogo não for encontrado para atualização ou o acesso for negado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable String id, @Valid @RequestBody GameUpdateDTO updateDTO) {
        Game updatedGame = gameService.updateGame(id, updateDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao atualizar. Jogo com ID '" + id + "' não encontrado ou acesso negado."));
        return ResponseEntity.ok(updatedGame);
    }

    /**
     * Endpoint para excluir um jogo existente.
     * O acesso é restrito ao proprietário do jogo.
     * @param id O ID do jogo a ser excluído (passado como variável de caminho).
     * @return ResponseEntity com status HTTP 200 (OK) se a exclusão for bem-sucedida.
     * @throws ResourceNotFoundException se o jogo não for encontrado para exclusão ou o acesso for negado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        if (gameService.deleteGame(id)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Falha ao excluir. Jogo com ID '" + id + "' não encontrado ou acesso negado.");
        }
    }
}