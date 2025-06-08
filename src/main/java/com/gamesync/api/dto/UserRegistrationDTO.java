package com.gamesync.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) para encapsular os dados necessários para o registro de um novo usuário.
 * Esta classe é usada como corpo da requisição (request body) no endpoint de registro de usuários
 * e inclui anotações de validação para garantir que os dados fornecidos sejam válidos
 * antes de serem processados pela lógica de serviço.
 */
public class UserRegistrationDTO {

    /**
     * Nome de usuário para o novo usuário.
     * É obrigatório, deve ter entre 3 e 50 caracteres.
     */
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    /**
     * Senha para o novo usuário.
     * É obrigatória, deve ter entre 6 e 100 caracteres.
     * Esta senha será codificada pelo serviço antes de ser armazenada.
     */
    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
    private String password;

    /**
     * Endereço de email para o novo usuário.
     * É obrigatório, deve ser um email válido e ter no máximo 100 caracteres.
     */
    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    @Size(max = 100, message = "Email must be up to 100 characters.")
    private String email;

    /**
     * Steam ID do usuário (opcional).
     * Se fornecido, deve ter no máximo 50 caracteres.
     */
    @Size(max = 50, message = "Steam ID must be up to 50 characters if provided.")
    private String steamId;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }
}