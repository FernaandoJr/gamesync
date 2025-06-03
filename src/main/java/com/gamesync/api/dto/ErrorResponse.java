package com.gamesync.api.dto;

/**
 * Data Transfer Object (DTO) para padronizar as respostas de erro da API.
 * Quando uma exceção ocorre e é tratada pelo {@link com.gamesync.api.exception.GlobalExceptionHandler},
 * uma instância desta classe é criada e retornada ao cliente no corpo da resposta HTTP,
 * geralmente em formato JSON.
 */
public class ErrorResponse { //

    /**
     * O timestamp (em milissegundos desde a época Unix) de quando o erro ocorreu.
     * Ajuda a rastrear quando um erro específico aconteceu.
     */
    private long timestamp; //

    /**
     * O código de status HTTP associado ao erro (ex: 400, 404, 500).
     */
    private int status; //

    /**
     * Uma breve descrição textual do status HTTP (ex: "Not Found", "Bad Request", "Internal Server Error").
     * Geralmente corresponde à "reason phrase" do código de status HTTP.
     */
    private String error; //

    /**
     * Uma mensagem mais detalhada descrevendo o erro específico que ocorreu.
     * Pode ser a mensagem da exceção original ou uma mensagem customizada.
     */
    private String message; //

    // Opcionalmente, poderia ser adicionado um campo 'path' para indicar o endpoint da API que causou o erro.
    // private String path;

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
        this.timestamp = System.currentTimeMillis(); // Define o timestamp para o momento atual.
        this.status = status;
        this.error = httpStatus.getReasonPhrase(); // Obtém a frase padrão do status (ex: "Not Found", "Bad Request").
        this.message = message;
    }


    // --- Getters e Setters ---
    // Métodos padrão para acessar e (se necessário) modificar os campos da classe.
    // Para DTOs de resposta, os setters podem não ser estritamente necessários se os campos
    // forem definidos apenas via construtor, mas são incluídos por completude ou para uso
    // por frameworks de serialização/desserialização se necessário.

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