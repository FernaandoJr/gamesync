package com.gamesync.api.exception;
import com.gamesync.api.dto.ErrorResponse; // DTO que encapsula os detalhes do erro a serem enviados ao cliente.
import org.slf4j.Logger;        // Interface para logging (SLF4J).
import org.slf4j.LoggerFactory; // Fábrica para obter instâncias de Logger.
import org.springframework.http.HttpStatus;     // Enumeração para códigos de status HTTP.
import org.springframework.http.ResponseEntity; // Representa uma resposta HTTP completa (status, headers, corpo).
import org.springframework.validation.FieldError; // Encapsula informações sobre um erro de validação de campo.
import org.springframework.web.bind.MethodArgumentNotValidException; // Exceção lançada quando a validação de um argumento de método anotado com @Valid falha.
import org.springframework.web.bind.annotation.ControllerAdvice;     // Anotação que permite que esta classe forneça tratamento de exceções compartilhado entre múltiplos controllers.
import org.springframework.web.bind.annotation.ExceptionHandler;     // Anotação para definir métodos que tratarão exceções específicas.
import java.util.HashMap; // Implementação de Map para armazenar erros de validação de campo.
import java.util.Map;     // Interface para coleções de pares chave-valor.

/**
 * Handler de exceções global para a aplicação.
 * Utiliza a anotação {@link ControllerAdvice} do Spring para interceptar exceções lançadas
 * por qualquer controller na aplicação e fornecer uma resposta HTTP padronizada.
 * Isso centraliza o tratamento de erros e garante que os clientes da API recebam
 * respostas de erro consistentes.
 */
@ControllerAdvice // Torna esta classe um componente global para tratamento de exceções em controllers.
public class GlobalExceptionHandler {

    // Logger para registrar informações sobre as exceções ocorridas.
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handler para a exceção customizada {@link ResourceNotFoundException}.
     * Chamado quando um recurso solicitado não é encontrado no sistema.
     * @param ex A instância da exceção ResourceNotFoundException lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 404 (Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class) // Especifica que este método trata ResourceNotFoundException.
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Loga a mensagem da exceção com nível WARN.
        logger.warn(ex.getMessage());
        // Cria o objeto de resposta de erro padronizado.
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // Código de status HTTP (ex: 404)
                HttpStatus.NOT_FOUND,         // Objeto HttpStatus para obter a "reason phrase" (ex: "Not Found")
                ex.getMessage()               // Mensagem detalhada da exceção.
        );
        // Retorna a resposta HTTP com o ErrorResponse e o status apropriado.
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para a exceção customizada {@link BadRequestException}.
     * Chamado quando uma requisição do cliente é malformada ou contém dados inválidos.
     * @param ex A instância da exceção BadRequestException lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 400 (Bad Request).
     */
    @ExceptionHandler(BadRequestException.class) // Especifica que este método trata BadRequestException.
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        logger.warn(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para a exceção customizada {@link DuplicateResourceException}.
     * Chamado quando uma tentativa de criar um recurso falha devido a um conflito (recurso já existe).
     * @param ex A instância da exceção DuplicateResourceException lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 409 (Conflict).
     */
    @ExceptionHandler(DuplicateResourceException.class) // Especifica que este método trata DuplicateResourceException.
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        logger.warn(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handler para a exceção genérica {@link IllegalArgumentException}.
     * Pode ser lançada por várias partes da aplicação (incluindo serviços) para indicar um argumento inválido.
     * @param ex A instância da exceção IllegalArgumentException lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 400 (Bad Request).
     */
    @ExceptionHandler(IllegalArgumentException.class) // Especifica que este método trata IllegalArgumentException.
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handler para {@link MethodArgumentNotValidException}.
     * Esta exceção é lançada pelo Spring quando a validação de um argumento de método
     * anotado com {@link jakarta.validation.Valid} falha (ex: validação de DTOs).
     * @param ex A instância da exceção MethodArgumentNotValidException lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 400 (Bad Request)
     * e uma mensagem detalhando os erros de validação.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // Especifica que este método trata MethodArgumentNotValidException.
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Cria um mapa para armazenar os erros de validação por campo.
        Map<String, String> errors = new HashMap<>();
        // Itera sobre todos os erros de validação obtidos do BindingResult da exceção.
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // Obtém o nome do campo que falhou na validação.
            String fieldName = ((FieldError) error).getField();
            // Obtém a mensagem de erro padrão definida na anotação de validação.
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Validation error: {}", errors); // Loga o mapa de erros.
        // Formata uma mensagem de erro consolidada.
        String message = "Validation failed: " + errors.toString();
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                message // A mensagem inclui os detalhes dos campos que falharam.
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler global para qualquer outra exceção não tratada especificamente pelos handlers acima.
     * Atua como um "catch-all" para evitar que exceções inesperadas exponham stack traces ao cliente.
     * @param ex A instância da exceção genérica lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 500 (Internal Server Error)
     * e uma mensagem genérica de erro.
     */
    @ExceptionHandler(Exception.class) // Especifica que este método trata qualquer Exception não capturada anteriormente.
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex ) {
        // Loga o erro completo (com stack trace) para depuração interna.
        logger.error("An unexpected error occurred: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal server error occurred. Please try again later." // Mensagem genérica para o cliente.
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}