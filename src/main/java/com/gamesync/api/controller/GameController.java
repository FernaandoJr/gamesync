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
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Importação adicionada
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Games", description = "Operações relacionadas ao gerenciamento de jogos.") // Anotação adicionada
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
    @Operation(summary = "Cria um novo jogo", //
            description = "Adiciona um novo jogo à coleção do usuário autenticado.", //
            security = @SecurityRequirement(name = "basicAuth"), // Indica que requer autenticação basicAuth
            responses = {
                    @ApiResponse(responseCode = "201", description = "Jogo criado com sucesso.", //
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))), //
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou malformados.", //
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))), //
                    @ApiResponse(responseCode = "401", description = "Credenciais de autenticação ausentes ou inválidas.", //
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))), //
                    @ApiResponse(responseCode = "409", description = "Já existe um jogo com o mesmo nome para este usuário.", // Nova resposta para DuplicateResourceException
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
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
    @Operation(summary = "Lista todos os jogos do usuário",
            description = "Retorna uma lista de todos os jogos pertencentes ao usuário autenticado.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de jogos retornada com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class, subTypes = {Game.class}))), // Retorna uma lista de Game
                    @ApiResponse(responseCode = "401", description = "Credenciais de autenticação ausentes ou inválidas.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
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
    @Operation(summary = "Busca um jogo por ID",
            description = "Retorna os detalhes de um jogo específico pelo seu ID. Apenas o proprietário do jogo pode acessá-lo.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Jogo encontrado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais de autenticação ausentes ou inválidas.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Jogo não encontrado ou acesso negado (o jogo não pertence ao usuário autenticado).",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Game game = gameService.findGameByIdAndCurrentUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo com ID '" + id + "' não encontrado ou acesso negado.")); //
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
    @Operation(summary = "Atualiza um jogo existente",
            description = "Atualiza os dados de um jogo específico. Apenas o proprietário do jogo pode modificá-lo.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Jogo atualizado com sucesso.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou malformados.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciais de autenticação ausentes ou inválidas.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Jogo não encontrado para atualização ou acesso negado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "O novo nome do jogo já existe para este usuário.", // Nova resposta para DuplicateResourceException
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<Game> updateGame(@PathVariable String id, @Valid @RequestBody GameUpdateDTO updateDTO) {
        Game updatedGame = gameService.updateGame(id, updateDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao atualizar. Jogo com ID '" + id + "' não encontrado ou acesso negado.")); //
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
    @Operation(summary = "Exclui um jogo",
            description = "Remove um jogo da biblioteca do usuário. Apenas o proprietário do jogo pode excluí-lo.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Jogo excluído com sucesso (sem conteúdo)."),
                    @ApiResponse(responseCode = "401", description = "Credenciais de autenticação ausentes ou inválidas.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Jogo não encontrado para exclusão ou acesso negado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        if (gameService.deleteGame(id)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Falha ao excluir. Jogo com ID '" + id + "' não encontrado ou acesso negado."); //
        }
    }
}