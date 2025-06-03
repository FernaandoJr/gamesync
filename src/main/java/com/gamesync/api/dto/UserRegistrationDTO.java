package com.gamesync.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // ESSENCIAL
import jakarta.validation.constraints.Size;   // ESSENCIAL

public class UserRegistrationDTO {

    @NotBlank(message = "Username is required.") //
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Password is required.") //
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
    private String password;

    @NotBlank(message = "Email is required.") //
    @Email(message = "Email should be valid.") //
    @Size(max = 100, message = "Email must be up to 100 characters.")
    private String email;

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