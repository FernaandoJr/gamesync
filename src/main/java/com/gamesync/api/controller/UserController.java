// File: src/main/java/com/gamesync/api/controller/UserController.java
package com.gamesync.api.controller;

import com.gamesync.api.dto.UserRegistrationDTO; // DTO para dados de registro de novo usuário.
import com.gamesync.api.dto.UserUpdateDTO;       // DTO para dados de atualização de usuário existente.
import com.gamesync.api.exception.ResourceNotFoundException; // Exceção lançada quando um usuário específico não é encontrado.
import com.gamesync.api.model.User; // Entidade que representa um usuário no sistema.
import com.gamesync.api.service.UserService; // Serviço que lida com as operações de usuário.
import jakarta.validation.Valid; // Ativa a validação para o objeto anotado (DTO).
import org.springframework.http.HttpStatus;     // Enumeração para códigos de status HTTP (ex: CREATED, OK).
import org.springframework.http.ResponseEntity; // Representa uma resposta HTTP completa, incluindo status, headers e corpo.
import org.springframework.web.bind.annotation.*; // Coleção de anotações para mapeamento de requisições web (ex: @RestController, @RequestMapping, @PostMapping, @GetMapping, @PutMapping, @DeleteMapping, @PathVariable, @RequestBody).

/**
 * Controller REST para gerenciar operações relacionadas a usuários.
 * Expõe endpoints HTTP para registro, busca, atualização e exclusão de usuários.
 * Todas as operações são mapeadas sob o caminho base "/users".
 * Interage com a camada de serviço (UserService) para executar a lógica de negócios.
 */
@RestController // Anotação que combina @Controller e @ResponseBody, indicando que os valores retornados pelos métodos
// serão diretamente gravados no corpo da resposta HTTP (geralmente como JSON).
@RequestMapping("/users") // Define o prefixo "/users" para todos os endpoints mapeados nesta classe.
public class UserController {

    // Injeção de dependência do UserService. O Spring fornecerá uma instância de UserService aqui.
    private final UserService userService; //

    /**
     * Construtor da classe UserController.
     * Utilizado pelo Spring para injetar a dependência do UserService.
     * @param userService A instância do serviço de usuários.
     */
    public UserController(UserService userService) { //
        this.userService = userService;
    }

    /**
     * Endpoint para registrar um novo usuário no sistema.
     * Mapeado para requisições HTTP POST em "/users/register".
     * O corpo da requisição deve conter um JSON compatível com UserRegistrationDTO.
     * @param registrationDTO DTO contendo os dados para o registro (username, password, email, steamId).
     * A anotação @Valid dispara a validação das anotações presentes no DTO.
     * @return ResponseEntity contendo o objeto User do usuário criado (sem a senha) e status HTTP 201 (Created).
     */
    @PostMapping("/register") //
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        // Chama o serviço para registrar o usuário com os dados fornecidos.
        User savedUser = userService.registerUser(registrationDTO);
        // Remove a senha do objeto User antes de enviá-lo na resposta por segurança.
        savedUser.setPassword(null);
        // Retorna o usuário salvo e o status HTTP 201 Created.
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obter os detalhes do perfil do usuário atualmente autenticado.
     * Mapeado para requisições HTTP GET em "/users/me".
     * @return ResponseEntity contendo o objeto User do usuário autenticado (sem a senha) e status HTTP 200 (OK).
     */
    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUserProfile() {
        // Chama o serviço para obter o usuário autenticado.
        User user = userService.getAuthenticatedUser();
        // Remove a senha do objeto User por segurança.
        user.setPassword(null);
        // Retorna o usuário e o status HTTP 200 OK.
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint para obter os detalhes de um usuário específico pelo seu ID.
     * Mapeado para requisições HTTP GET em "/users/{id}".
     * @param id O ID do usuário a ser buscado, extraído da URL.
     * @return ResponseEntity contendo o objeto User encontrado (sem a senha) e status HTTP 200 (OK).
     * @throws ResourceNotFoundException se o usuário com o ID especificado não for encontrado.
     */
    @GetMapping("/{id}") //
    public ResponseEntity<User> getUserById(@PathVariable String id) { // @PathVariable extrai o valor de 'id' da URL.
        // Chama o serviço para buscar o usuário pelo ID.
        // Se o usuário não for encontrado (Optional vazio), lança ResourceNotFoundException.
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + id + "' não encontrado."));
        // Remove a senha do objeto User por segurança.
        user.setPassword(null);
        // Retorna o usuário e o status HTTP 200 OK.
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint para atualizar os dados de um usuário existente.
     * Mapeado para requisições HTTP PUT em "/users/{id}".
     * O corpo da requisição deve conter um JSON compatível com UserUpdateDTO.
     * Apenas o usuário autenticado pode atualizar seus próprios dados (lógica no UserService).
     * @param id O ID do usuário a ser atualizado, extraído da URL.
     * @param userUpdateDTO DTO contendo os dados a serem atualizados (username, email, newPassword).
     * A anotação @Valid dispara a validação das anotações presentes no DTO.
     * @return ResponseEntity contendo o objeto User atualizado (sem a senha) e status HTTP 200 (OK).
     * @throws ResourceNotFoundException se o usuário não for encontrado ou o acesso para atualização for negado.
     */
    @PutMapping("/{id}") //
    public ResponseEntity<User> updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        // Chama o serviço para atualizar o usuário.
        // Se a atualização falhar (ex: usuário não encontrado, acesso negado), lança ResourceNotFoundException.
        User updatedUser = userService.updateUser(id, userUpdateDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao atualizar. Usuário com ID '" + id + "' não encontrado ou acesso negado."));
        // Remove a senha do objeto User por segurança.
        updatedUser.setPassword(null);
        // Retorna o usuário atualizado e o status HTTP 200 OK.
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Endpoint para excluir um usuário existente.
     * Mapeado para requisições HTTP DELETE em "/users/{id}".
     * Apenas o usuário autenticado pode excluir sua própria conta (lógica no UserService).
     * @param id O ID do usuário a ser excluído, extraído da URL.
     * @return ResponseEntity com status HTTP 200 (OK) e sem corpo se a exclusão for bem-sucedida.
     * @throws ResourceNotFoundException se o usuário não for encontrado ou o acesso para exclusão for negado.
     */
    @DeleteMapping("/{id}") //
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        // Chama o serviço para excluir o usuário.
        if (userService.deleteUser(id)) {
            // Se a exclusão for bem-sucedida, retorna 200 OK sem corpo.
            return ResponseEntity.ok().build();
        } else {
            // Se o serviço retornar false (indicando que o usuário não foi encontrado para exclusão,
            // embora a implementação atual do serviço lance exceção em caso de não propriedade/não encontrado),
            // lança ResourceNotFoundException.
            throw new ResourceNotFoundException("Falha ao excluir. Usuário com ID '" + id + "' não encontrado ou acesso negado.");
        }
    }
}