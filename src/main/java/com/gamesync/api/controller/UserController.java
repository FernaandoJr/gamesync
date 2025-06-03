// File: src/main/java/com/gamesync/api/controller/UserController.java
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

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User savedUser = userService.registerUser(registrationDTO);
        savedUser.setPassword(null);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUserProfile() {
        User user = userService.getAuthenticatedUser();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID '" + id + "' não encontrado."));
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        User updatedUser = userService.updateUser(id, userUpdateDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao atualizar. Usuário com ID '" + id + "' não encontrado ou acesso negado."));
        updatedUser.setPassword(null);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Falha ao excluir. Usuário com ID '" + id + "' não encontrado ou acesso negado.");
        }
    }
}