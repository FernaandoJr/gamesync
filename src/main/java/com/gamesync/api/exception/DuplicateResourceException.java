package com.gamesync.api.exception;

/**
 * Exceção customizada para representar situações onde uma tentativa de criar um recurso
 * falha porque um recurso com identificadores únicos (como email, username, ou uma combinação
 * de nome e ID de usuário para um jogo) já existe no sistema.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Construtor que aceita uma mensagem detalhando a causa da exceção,
     * especificamente qual recurso ou campo causou a duplicidade.
     *
     * @param message A mensagem de detalhe (que é salva para recuperação posterior
     * pelo método {@link Throwable#getMessage()}).
     * Exemplo: "Username 'usuario123' já existe."
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}