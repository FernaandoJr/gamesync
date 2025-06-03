// File: src/main/java/com/gamesync/api/controller/GameController.java
package com.gamesync.api.controller;

import com.gamesync.api.dto.ErrorResponse; // DTO para respostas de erro padronizadas.
import com.gamesync.api.dto.GameCreateDTO; // DTO para dados de criação de um novo jogo.
import com.gamesync.api.dto.GameUpdateDTO; // DTO para dados de atualização de um jogo existente.
import com.gamesync.api.exception.ResourceNotFoundException; // Exceção para quando um recurso não é encontrado.
import com.gamesync.api.model.Game; // Entidade que representa um jogo no sistema.
import com.gamesync.api.service.GameService; // Serviço que lida com as operações de jogo.
import io.swagger.v3.oas.annotations.Operation; // Descreve uma operação (endpoint).
import io.swagger.v3.oas.annotations.media.Content;    // Descreve o conteúdo de uma requisição ou resposta.
import io.swagger.v3.oas.annotations.media.Schema;      // Define o esquema (estrutura) de um modelo de dados.
import io.swagger.v3.oas.annotations.responses.ApiResponse; // Descreve uma possível resposta de uma operação.
import jakarta.validation.Valid; // Ativa a validação para o objeto anotado.
import org.springframework.http.HttpStatus;     // Enumeração para códigos de status HTTP.
import org.springframework.http.ResponseEntity; // Representa uma resposta HTTP completa.
import org.springframework.web.bind.annotation.*; // Anotações para mapeamento de requisições (ex: @GetMapping, @PostMapping, @PathVariable, @RequestBody).
import java.util.List; // Interface para coleções de listas.

/**
 * Controller REST para gerenciar operações relacionadas a jogos.
 * Expõe endpoints HTTP para criar, ler, atualizar e excluir (CRUD) jogos,
 * interagindo com a camada de serviço (GameService) para executar a lógica de negócios.
 * Todos os endpoints nesta classe são prefixados com "/games" conforme definido em @RequestMapping.
 */
@RestController // Combinação de @Controller e @ResponseBody; indica que os retornos dos métodos serão o corpo da resposta.
@RequestMapping("/games") // Mapeia todas as requisições com o prefixo "/games" para este controller.
// @Tag(name = "Game Management", description = "Endpoints para gerenciamento de jogos") // Exemplo de como a tag poderia ser adicionada.
public class GameController {

    // Injeção de dependência do GameService, que contém a lógica de negócios para jogos.
    private final GameService gameService; //

    /**
     * Construtor para injeção de dependência do GameService.
     * @param gameService A instância do serviço de jogos a ser injetada pelo Spring.
     */
    public GameController(GameService gameService) { //
        this.gameService = gameService;
    }

    /**
     * Endpoint para criar um novo jogo.
     * Espera um objeto GameCreateDTO no corpo da requisição.
     * @param createDTO DTO contendo os dados do jogo a ser criado. Validado com @Valid.
     * @return ResponseEntity contendo o jogo criado e o status HTTP 201 (Created).
     */
    @PostMapping // Mapeia requisições HTTP POST para /games.
    @Operation(summary = "Cria um novo jogo", // Sumário da operação para documentação OpenAPI/Swagger.
            description = "Adiciona um novo jogo à coleção do usuário.", // Descrição mais detalhada.
            responses = { // Define as possíveis respostas HTTP para este endpoint.
                    @ApiResponse(responseCode = "201", description = "Jogo criado com sucesso", //
                            content = @Content(mediaType = "application/json", //
                                    schema = @Schema(implementation = Game.class))), // Esquema do corpo da resposta de sucesso.
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", //
                            content = @Content(mediaType = "application/json", //
                                    schema = @Schema(implementation = ErrorResponse.class))), // Esquema para resposta de erro 400.
                    @ApiResponse(responseCode = "401", description = "Não autenticado", //
                            content = @Content(mediaType = "application/json", //
                                    schema = @Schema(implementation = ErrorResponse.class))), // Esquema para resposta de erro 401.
            })
    // @Valid ativa a validação do GameCreateDTO com base nas anotações de validação (ex: @NotBlank, @Size) nele.
    // @RequestBody indica que o GameCreateDTO virá do corpo da requisição HTTP.
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameCreateDTO createDTO) { //
        Game createdGame = gameService.createGame(createDTO); // Chama o serviço para criar o jogo.
        // Retorna o jogo criado com o status HTTP 201 (Created).
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }

    /**
     * Endpoint para buscar todos os jogos do usuário atualmente autenticado.
     * @return ResponseEntity contendo uma lista de jogos e o status HTTP 200 (OK).
     */
    @GetMapping // Mapeia requisições HTTP GET para /games.
    // Poderiam ser adicionadas anotações @Operation e @ApiResponses aqui também para documentação.
    public ResponseEntity<List<Game>> getAllGamesForCurrentUser() {
        List<Game> games = gameService.findAllGamesByCurrentUser(); // Chama o serviço para buscar os jogos.
        return ResponseEntity.ok(games); // Retorna a lista de jogos com status HTTP 200 (OK).
    }

    /**
     * Endpoint para buscar um jogo específico pelo seu ID.
     * O acesso é restrito ao proprietário do jogo.
     * @param id O ID do jogo a ser buscado (passado como variável de caminho).
     * @return ResponseEntity contendo o jogo encontrado e o status HTTP 200 (OK).
     * @throws ResourceNotFoundException se o jogo não for encontrado ou o acesso for negado.
     */
    @GetMapping("/{id}") // Mapeia requisições HTTP GET para /games/{id}.
    // Poderiam ser adicionadas anotações @Operation e @ApiResponses aqui.
    public ResponseEntity<Game> getGameById(@PathVariable String id) { // @PathVariable extrai o 'id' da URL.
        // Chama o serviço para buscar o jogo. Se não encontrado ou acesso negado, o serviço
        // (ou este controller) lança ResourceNotFoundException, que será tratada pelo GlobalExceptionHandler.
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
    @PutMapping("/{id}") // Mapeia requisições HTTP PUT para /games/{id}.
    // Poderiam ser adicionadas anotações @Operation e @ApiResponses aqui.
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
    @DeleteMapping("/{id}") // Mapeia requisições HTTP DELETE para /games/{id}.
    // Poderiam ser adicionadas anotações @Operation e @ApiResponses aqui.
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        if (gameService.deleteGame(id)) { // Chama o serviço para excluir o jogo.
            return ResponseEntity.ok().build(); // Retorna 200 OK sem corpo na resposta.
        } else {
            // Se deleteGame retornar false (embora na implementação atual ele lance exceção em caso de falha de permissão),
            // lança ResourceNotFoundException. A implementação do GameService.deleteGame já lança exceção em caso de não propriedade.
            throw new ResourceNotFoundException("Falha ao excluir. Jogo com ID '" + id + "' não encontrado ou acesso negado.");
        }
    }
}