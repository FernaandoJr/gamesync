package com.gamesync.api.exception;

/**
 * Exceção customizada para representar situações de "Bad Request" (Requisição Inválida - HTTP 400).
 * Esta exceção é lançada quando uma requisição do cliente não pode ser processada
 * devido a dados de entrada inválidos, formato incorreto, ou violação de regras de negócio
 * que tornam a requisição semanticamente incorreta.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem detalhando a causa da exceção.
     * @param message A mensagem de detalhe (que é salva para recuperação posterior pelo método {@link Throwable#getMessage()}).
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Construtor que aceita uma mensagem detalhando a causa da exceção e a causa raiz.
     * Útil para encadear exceções e preservar o stack trace da exceção original.
     * @param message A mensagem de detalhe.
     * @param cause A causa raiz (que é salva para recuperação posterior pelo método {@link Throwable#getCause()});
     * um valor null é permitido e indica que a causa é inexistente ou desconhecida.
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}