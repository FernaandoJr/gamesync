package com.gamesync.api.exception;
import com.gamesync.api.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler de exceções global para a aplicação.
 * Utiliza a anotação {@link ControllerAdvice} do Spring para interceptar exceções lançadas
 * por qualquer controller na aplicação e fornecer uma resposta HTTP padronizada.
 * Isso centraliza o tratamento de erros e garante que os clientes da API recebam
 * respostas de erro consistentes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handler para a exceção customizada {@link ResourceNotFoundException}.
     * Chamado quando um recurso solicitado não é encontrado no sistema.
     * @param ex A instância da exceção ResourceNotFoundException lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 404 (Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para a exceção customizada {@link BadRequestException}.
     * Chamado quando uma requisição do cliente é malformada ou contém dados inválidos.
     * @param ex A instância da exceção BadRequestException lançada.
     * @return Um ResponseEntity contendo um ErrorResponse com status HTTP 400 (Bad Request).
     */
    @ExceptionHandler(BadRequestException.class)
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
    @ExceptionHandler(DuplicateResourceException.class)
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
    @ExceptionHandler(IllegalArgumentException.class)
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Validation error: {}", errors);
        String message = "Validation failed: " + errors.toString();
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                message
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
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex ) {
        logger.error("An unexpected error occurred: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal server error occurred. Please try again later."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}