package com.gamesync.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para encapsular os dados necessários para o
 * registro de um novo usuário.
 * Esta classe é usada como corpo da requisição (request body) no endpoint de
 * registro de usuários e inclui anotações de validação para garantir que os
 * dados fornecidos sejam
 * válidos antes de serem processados pela lógica de serviço.
 * 
 * Utilizando Lombok:
 * - @Data: Gera getters, setters, equals, hashCode e toString
 * - @NoArgsConstructor: Gera construtor sem argumentos
 * - @AllArgsConstructor: Gera construtor com todos os argumentos
 * - @Builder: Implementa o padrão Builder para criação de objetos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDTO {

	@NotBlank(message = "Username is required.")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
	private String username;

	@NotBlank(message = "Password is required.")
	@Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
	private String password;

	@NotBlank(message = "Email is required.")
	@Email(message = "Email should be valid.")
	@Size(max = 100, message = "Email must be up to 100 characters.")
	private String email;
}