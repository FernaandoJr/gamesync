// File: src/main/java/com/gamesync/api/exception/DuplicateResourceException.java
package com.gamesync.api.exception;

/**
 * Exceção customizada para representar situações onde uma tentativa de criar um recurso
 * falha porque um recurso com identificadores únicos (como email, username, ou uma combinação
 * de nome e ID de usuário para um jogo) já existe no sistema.
 *
 * <p>Esta exceção geralmente resultaria em uma resposta HTTP 409 Conflict se capturada
 * por um handler global de exceções.</p>
 *
 * <p>Estender {@link RuntimeException} significa que esta é uma exceção não verificada (unchecked).
 * Isso é apropriado para situações onde a condição de erro (recurso duplicado)
 * é detectada em tempo de execução e geralmente é causada por dados fornecidos pelo cliente
 * que violam restrições de unicidade.</p>
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
        super(message); // Chama o construtor da classe pai (RuntimeException) com a mensagem.
    }

    // Nota: Um construtor adicional com (String message, Throwable cause) poderia ser adicionado
    // se houvesse cenários onde esta exceção pudesse encadear outra, mas para
    // violações de unicidade, uma mensagem simples geralmente é suficiente.
}