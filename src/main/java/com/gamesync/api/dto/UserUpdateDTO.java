package com.gamesync.api.dto;

import jakarta.validation.constraints.Email; // Valida se o campo é um endereço de email bem formado.
import jakarta.validation.constraints.Size;   // Define restrições de tamanho (mínimo/máximo) para Strings.

/**
 * Data Transfer Object (DTO) para encapsular os dados que podem ser atualizados
 * em um perfil de usuário existente.
 *
 * <p>Esta classe é usada como corpo da requisição (request body) nos endpoints da API
 * para atualização de informações do usuário. Os campos são opcionais, permitindo
 * que o cliente envie apenas os dados que deseja modificar.</p>
 *
 * <p>As anotações de validação garantem que, se um valor for fornecido para um campo,
 * ele deve atender aos critérios especificados (ex: formato de email, tamanho da string).</p>
 */
public class UserUpdateDTO { //

    /**
     * Novo nome de usuário.
     * Se fornecido, deve ter entre 3 e 50 caracteres.
     * A ausência deste campo (nulo) significa que o nome de usuário não deve ser alterado.
     */
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters if provided.")
    private String username; //

    /**
     * Novo endereço de email.
     * Se fornecido, deve ser um email válido e ter no máximo 100 caracteres.
     * A ausência deste campo (nulo) significa que o email não deve ser alterado.
     */
    @Email(message = "Email should be valid if provided.")
    @Size(max = 100, message = "Email must be up to 100 characters if provided.")
    private String email; //

    /**
     * Nova senha para o usuário.
     * Se fornecida, deve ter entre 6 e 100 caracteres.
     * A ausência deste campo (nulo) significa que a senha não deve ser alterada.
     * Esta senha será codificada pelo serviço antes de ser armazenada.
     */
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters if provided.")
    private String newPassword; //

    /**
     * Construtor padrão.
     * Necessário para frameworks de desserialização JSON como Jackson (usado pelo Spring MVC)
     * para instanciar o objeto antes de popular seus campos a partir do corpo da requisição.
     */
    public UserUpdateDTO() { //
    }

    // --- Getters e Setters ---
    // Métodos padrão para acessar e modificar os campos da classe.
    // Usados pelo framework Spring MVC (via Jackson) para popular o objeto a partir do JSON da requisição
    // e pelo código da aplicação (ex: UserService) para ler os valores a serem atualizados.

    public String getUsername() { //
        return username;
    }

    public void setUsername(String username) { //
        this.username = username;
    }

    public String getEmail() { //
        return email;
    }

    public void setEmail(String email) { //
        this.email = email;
    }

    public String getNewPassword() { //
        return newPassword;
    }

    public void setNewPassword(String newPassword) { //
        this.newPassword = newPassword;
    }
}