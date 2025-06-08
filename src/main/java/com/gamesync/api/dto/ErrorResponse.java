package com.gamesync.api.dto;

/**
 * Data Transfer Object (DTO) para padronizar as respostas de erro da API.
 * Quando uma exceção ocorre e é tratada pelo {@link com.gamesync.api.exception.GlobalExceptionHandler},
 * uma instância desta classe é criada e retornada ao cliente no corpo da resposta HTTP,
 * geralmente em formato JSON.
 */
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
    public ErrorResponse(int status, String error, String message) { //
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
    public ErrorResponse(int status, org.springframework.http.HttpStatus httpStatus, String message) { //
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }

    public long getTimestamp() { //
        return timestamp;
    }

    public void setTimestamp(long timestamp) { //
        this.timestamp = timestamp;
    }

    public int getStatus() { //
        return status;
    }

    public void setStatus(int status) { //
        this.status = status;
    }

    public String getError() { //
        return error;
    }

    public void setError(String error) { //
        this.error = error;
    }

    public String getMessage() { //
        return message;
    }

    public void setMessage(String message) { //
        this.message = message;
    }
}