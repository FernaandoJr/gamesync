package com.gamesync.api.controller;

import com.gamesync.api.dto.UserRegistrationDTO;
import com.gamesync.api.dto.UserUpdateDTO;
import com.gamesync.api.exception.ResourceNotFoundException;
import com.gamesync.api.model.User;
import com.gamesync.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciar operações relacionadas a usuários.
 * Expõe endpoints HTTP para registro, busca, atualização e exclusão de usuários.
 * Todas as operações são mapeadas sob o caminho base "/users".
 * Interage com a camada de serviço (UserService) para executar a lógica de negócios.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Construtor da classe UserController.
     * Utilizado pelo Spring para injetar a dependência do UserService.
     * @param userService A instância do serviço de usuários.
     */
    public UserController(UserService userService) {
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
        User savedUser = userService.registerUser(registrationDTO);
        savedUser.setPassword(null);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obter os detalhes do perfil do usuário atualmente autenticado.
     * Mapeado para requisições HTTP GET em "/users/me".
     * @return ResponseEntity contendo o objeto User do usuário autenticado (sem a senha) e status HTTP 200 (OK).
     */
    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUserProfile() {
        User user = userService.getAuthenticatedUser();
        user.setPassword(null);
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
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + id + "' não encontrado."));
        user.setPassword(null);
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
        User updatedUser = userService.updateUser(id, userUpdateDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao atualizar. Usuário com ID '" + id + "' não encontrado ou acesso negado."));
        updatedUser.setPassword(null);
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
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Falha ao excluir. Usuário com ID '" + id + "' não encontrado ou acesso negado.");
        }
    }
}