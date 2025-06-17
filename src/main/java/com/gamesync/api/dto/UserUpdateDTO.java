package com.gamesync.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para encapsular os dados que podem ser atualizados
 * em um perfil de usuário existente.
 *
 * <p>
 * Esta classe é usada como corpo da requisição (request body) nos endpoints da
 * API
 * para atualização de informações do usuário. Os campos são opcionais,
 * permitindo
 * que o cliente envie apenas os dados que deseja modificar.
 * </p>
 *
 * <p>
 * As anotações de validação garantem que, se um valor for fornecido para um
 * campo,
 * ele deve atender aos critérios especificados (ex: formato de email, tamanho
 * da string).
 * </p>
 * 
 * <p>
 * Utilizando Lombok:
 * - @Data: Gera getters, setters, equals, hashCode e toString
 * - @NoArgsConstructor: Gera construtor sem argumentos
 * - @AllArgsConstructor: Gera construtor com todos os argumentos
 * - @Builder: Implementa o padrão Builder para criação de objetos
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters if provided.")
		private String username;

    @Email(message = "Email should be valid if provided.")
    @Size(max = 100, message = "Email must be up to 100 characters if provided.")
		private String email;
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters if provided.")
		private String newPassword;
}