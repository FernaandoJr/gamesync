// File: src/main/java/com/gamesync/api/exception/BadRequestException.java
package com.gamesync.api.exception;

/**
 * Exceção customizada para representar situações de "Bad Request" (Requisição Inválida - HTTP 400).
 * Esta exceção é lançada quando uma requisição do cliente não pode ser processada
 * devido a dados de entrada inválidos, formato incorreto, ou violação de regras de negócio
 * que tornam a requisição semanticamente incorreta.
 *
 * <p>Estender {@link RuntimeException} significa que esta é uma exceção não verificada (unchecked),
 * o que é comum para exceções de tempo de execução que indicam erros de programação ou
 * problemas com os dados fornecidos pelo cliente que não podem ser razoavelmente recuperados
 * em tempo de compilação.</p>
 */
public class BadRequestException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem detalhando a causa da exceção.
     * @param message A mensagem de detalhe (que é salva para recuperação posterior pelo método {@link Throwable#getMessage()}).
     */
    public BadRequestException(String message) {
        super(message); // Chama o construtor da classe pai (RuntimeException) com a mensagem.
    }

    /**
     * Construtor que aceita uma mensagem detalhando a causa da exceção e a causa raiz.
     * Útil para encadear exceções e preservar o stack trace da exceção original.
     * @param message A mensagem de detalhe.
     * @param cause A causa raiz (que é salva para recuperação posterior pelo método {@link Throwable#getCause()});
     * um valor null é permitido e indica que a causa é inexistente ou desconhecida.
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause); // Chama o construtor da classe pai (RuntimeException) com a mensagem e a causa.
    }
}