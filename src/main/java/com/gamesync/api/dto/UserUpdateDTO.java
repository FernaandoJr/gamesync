package com.gamesync.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters if provided.")
    private String username;

    @Email(message = "Email should be valid if provided.")
    @Size(max = 100, message = "Email must be up to 100 characters if provided.")
    private String email;

    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters if provided.")
    private String newPassword;

    public UserUpdateDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}