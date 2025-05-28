package com.gamesync.api.controller;

import com.gamesync.api.model.User;
import com.gamesync.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users") // Mapeia este controller para o caminho /users
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Para criptografar a senha

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint para registrar um novo usuário.
     * Mapeado para requisições POST em /users/register.
     * Este endpoint é permitido a todos (permitAll) na SecurityConfig.
     *
     * @param user O objeto User contendo username, password e email.
     * @return ResponseEntity com o usuário criado e status 201 Created, ou 400 Bad Request se o usuário já existir.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Verifica se o username ou email já existe
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("Nome de usuário já existe!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email já registrado!", HttpStatus.BAD_REQUEST);
        }

        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Define o papel padrão para o novo usuário
        user.setRoles(Collections.singletonList("ROLE_USER")); // Adiciona o papel ROLE_USER

        User savedUser = userRepository.save(user);
        // Não retorne a senha no objeto retornado
        savedUser.setPassword(null);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Você pode adicionar outros endpoints relacionados a usuários aqui, como:
    // @GetMapping("/{id}") para buscar um usuário por ID (com autenticação e autorização)
    // @PutMapping("/{id}") para atualizar dados do usuário
    // @DeleteMapping("/{id}") para deletar usuário
}