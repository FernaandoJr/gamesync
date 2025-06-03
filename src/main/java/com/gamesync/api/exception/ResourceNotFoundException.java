// File: src/main/java/com/gamesync/api/exception/ResourceNotFoundException.java
package com.gamesync.api.exception;

/**
 * Exceção customizada para representar situações onde um recurso solicitado
 * não pôde ser encontrado no sistema.
 *
 * <p>Esta exceção é tipicamente lançada quando uma operação tenta acessar ou manipular
 * um recurso (como um usuário, um jogo, etc.) que não existe na base de dados
 * ou não está acessível nas condições especificadas.</p>
 *
 * <p>Quando capturada por um handler global de exceções, esta exceção geralmente
 * resultaria em uma resposta HTTP 404 Not Found.</p>
 *
 * <p>Estender {@link RuntimeException} significa que esta é uma exceção não verificada (unchecked).
 * Isso é comum para cenários onde a não existência de um recurso é uma condição
 * de tempo de execução que pode ocorrer devido a um ID inválido fornecido pelo cliente
 * ou uma inconsistência de dados que não pode ser prevista em tempo de compilação.</p>
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

    // Nota: Um construtor adicional com (String message, Throwable cause) poderia ser adicionado
    // se houvesse cenários onde esta exceção pudesse encadear outra, mas para
    // recursos não encontrados, uma mensagem simples geralmente é suficiente.
}