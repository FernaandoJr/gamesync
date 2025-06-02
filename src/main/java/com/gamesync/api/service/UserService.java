// src/main/java/com.gamesync.api.service/UserService.java
package com.gamesync.api.service;

import com.gamesync.api.dto.UserUpdateDTO;
import com.gamesync.api.model.User;
import com.gamesync.api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GameService gameService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GameService gameService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.gameService = gameService;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Nenhum usuário autenticado encontrado no contexto de segurança.");
        }
        return (User) authentication.getPrincipal();
    }

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nome de usuário já existe!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já registrado!");
        }
        if (user.getSteamId() != null && userRepository.findBySteamId(user.getSteamId()).isPresent()) {
            throw new IllegalArgumentException("Steam ID já registrado!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList("ROLE_USER"));
        return userRepository.save(user);
    }

    public Optional<User> updateUser(String id, UserUpdateDTO userUpdateDTO) {
        User authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.getId().equals(id)) {
            return Optional.empty();
        }

        return userRepository.findById(id)
                .map(existingUser -> {
                    if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isBlank()) {
                        if (userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent() &&
                                !userUpdateDTO.getUsername().equalsIgnoreCase(existingUser.getUsername())) {
                            throw new IllegalArgumentException("Novo nome de usuário já existe!");
                        }
                        existingUser.setUsername(userUpdateDTO.getUsername());
                    }

                    if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isBlank()) {
                        if (userRepository.findByEmail(userUpdateDTO.getEmail()).isPresent() &&
                                !userUpdateDTO.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
                            throw new IllegalArgumentException("Novo email já registrado!");
                        }
                        existingUser.setEmail(userUpdateDTO.getEmail());
                    }

                    if (userUpdateDTO.getNewPassword() != null && !userUpdateDTO.getNewPassword().isBlank()) {
                        existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));
                    }

                    return userRepository.save(existingUser);
                });
    }

    public boolean deleteUser(String id) {
        User authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.getId().equals(id)) {
            return false;
        }

        if (userRepository.existsById(id)) {
            gameService.deleteAllGamesByUserId(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}