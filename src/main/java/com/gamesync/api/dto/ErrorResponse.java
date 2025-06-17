package com.gamesync.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) para padronizar as respostas de erro da API.
 * Quando uma exceção ocorre e é tratada pelo
 * {@link com.gamesync.api.exception.GlobalExceptionHandler},
 * uma instância desta classe é criada e retornada ao cliente no corpo da
 * resposta HTTP,
 * geralmente em formato JSON.
 * 
 * Utilizando Lombok:
 * - @Getter/@Setter: Gera getters e setters para todos os campos
 */
@Getter
@Setter
public class ErrorResponse {

    private long timestamp;
    private int status;
    private String error;
    private String message;

    /**
     * Construtor para criar uma instância de ErrorResponse.
     * @param status O código de status HTTP.
     * @param error A breve descrição textual do status HTTP.
     * @param message A mensagem detalhada do erro.
     */
		public ErrorResponse(int status, String error, String message) {
        this.timestamp = System.currentTimeMillis(); // Define o timestamp para o momento atual.
        this.status = status;
        this.error = error;
        this.message = message;
    }

    /**
     * Construtor alternativo que utiliza o enum {@link org.springframework.http.HttpStatus}
     * para popular automaticamente o campo 'error' com a "reason phrase" padrão do status.
     * @param status O código de status HTTP.
     * @param httpStatus O objeto HttpStatus do Spring, do qual a "reason phrase" será extraída.
     * @param message A mensagem detalhada do erro.
     */
		public ErrorResponse(int status, org.springframework.http.HttpStatus httpStatus, String message) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
			}
}