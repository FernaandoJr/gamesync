// File: src/main/java/com/gamesync/api/service/UserService.java
package com.gamesync.api.service;

import com.gamesync.api.dto.UserRegistrationDTO;
import com.gamesync.api.dto.UserUpdateDTO;
import com.gamesync.api.exception.DuplicateResourceException;
import com.gamesync.api.exception.ResourceNotFoundException;
import com.gamesync.api.model.User;
import com.gamesync.api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para o deleteUser

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GameService gameService; // Será injetado depois que criarmos GameService

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       GameService gameService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.gameService = gameService;
    }

    private User getAuthenticatedUserInternal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Nenhum usuário autenticado encontrado ou tipo de principal inválido.");
        }
        return (User) authentication.getPrincipal();
    }

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + registrationDTO.getUsername() + "' já existe.");
        }
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + registrationDTO.getEmail() + "' já registrado.");
        }
        if (registrationDTO.getSteamId() != null && !registrationDTO.getSteamId().isBlank() &&
                userRepository.findBySteamId(registrationDTO.getSteamId()).isPresent()) {
            throw new DuplicateResourceException("Steam ID '" + registrationDTO.getSteamId() + "' já registrado.");
        }

        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        if (registrationDTO.getSteamId() != null && !registrationDTO.getSteamId().isBlank()) {
            newUser.setSteamId(registrationDTO.getSteamId());
        }
        newUser.setRoles(Collections.singletonList("ROLE_USER"));

        return userRepository.save(newUser);
    }

    public Optional<User> updateUser(String userId, UserUpdateDTO userUpdateDTO) {
        User authenticatedUser = getAuthenticatedUserInternal();

        if (!authenticatedUser.getId().equals(userId)) {
            throw new ResourceNotFoundException("Acesso negado para atualizar este usuário ou usuário não encontrado.");
        }

        return userRepository.findById(userId)
                .map(existingUser -> {
                    if (userUpdateDTO.getUsername() != null && !userUpdateDTO.getUsername().isBlank()) {
                        if (!existingUser.getUsername().equalsIgnoreCase(userUpdateDTO.getUsername()) &&
                                userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent()) {
                            throw new DuplicateResourceException("Novo nome de usuário '" + userUpdateDTO.getUsername() + "' já existe.");
                        }
                        existingUser.setUsername(userUpdateDTO.getUsername());
                    }

                    if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isBlank()) {
                        if (!existingUser.getEmail().equalsIgnoreCase(userUpdateDTO.getEmail()) &&
                                userRepository.findByEmail(userUpdateDTO.getEmail()).isPresent()) {
                            throw new DuplicateResourceException("Novo email '" + userUpdateDTO.getEmail() + "' já registrado.");
                        }
                        existingUser.setEmail(userUpdateDTO.getEmail());
                    }

                    if (userUpdateDTO.getNewPassword() != null && !userUpdateDTO.getNewPassword().isBlank()) {
                        existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));
                    }
                    return userRepository.save(existingUser);
                });
    }

    @Transactional
    public boolean deleteUser(String userId) {
        User authenticatedUser = getAuthenticatedUserInternal();

        if (!authenticatedUser.getId().equals(userId)) {
            throw new ResourceNotFoundException("Acesso negado para excluir este usuário ou usuário não encontrado.");
        }

        if (userRepository.existsById(userId)) {
            gameService.deleteAllGamesByUserId(userId);
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(String id) {
        User authenticatedUser = getAuthenticatedUserInternal();
        if (authenticatedUser.getId().equals(id) || authenticatedUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return userRepository.findById(id);
        }
        throw new ResourceNotFoundException("Usuário não encontrado ou acesso negado.");
    }

    public User getAuthenticatedUser() {
        return getAuthenticatedUserInternal();
    }
}