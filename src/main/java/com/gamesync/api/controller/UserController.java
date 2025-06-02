package com.gamesync.api.controller;

import com.gamesync.api.dto.UserUpdateDTO;
import com.gamesync.api.dto.ErrorResponse;
import com.gamesync.api.model.User;
import com.gamesync.api.service.UserService;
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
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            savedUser.setPassword(null);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            return userService.updateUser(id, userUpdateDTO)
                    .map(updatedUser -> {
                        updatedUser.setPassword(null);
                        return ResponseEntity.ok(updatedUser);
                    })
                    .orElseGet(() -> {
                        ErrorResponse error = new ErrorResponse("Usuário não encontrado ou acesso negado.", HttpStatus.NOT_FOUND.value());
                        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
                    });
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}