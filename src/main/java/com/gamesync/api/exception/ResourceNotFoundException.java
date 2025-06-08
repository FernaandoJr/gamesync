// File: src/main/java/com/gamesync/api/exception/ResourceNotFoundException.java
package com.gamesync.api.exception;

/**
 * Exceção customizada para representar situações onde um recurso solicitado
 * não pôde ser encontrado no sistema.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem detalhando qual recurso não foi encontrado
     * ou o contexto da falha.
     *
     * @param message A mensagem de detalhe (que é salva para recuperação posterior
     * pelo método {@link Throwable#getMessage()}).
     * Exemplo: "Usuário com ID '123' não encontrado."
     */
    public ResourceNotFoundException(String message) {
        super(message); // Chama o construtor da classe pai (RuntimeException) com a mensagem.
    }
}