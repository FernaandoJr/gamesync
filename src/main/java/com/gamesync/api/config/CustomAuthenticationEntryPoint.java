package com.gamesync.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesync.api.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Ponto de entrada de autenticação customizado para lidar com requisições não autenticadas.
 * Quando um usuário tenta acessar um recurso protegido sem autenticação (ou com autenticação inválida),
 * este componente é invocado para enviar uma resposta HTTP 401 Unauthorized com um corpo JSON amigável,
 * em vez do comportamento padrão do Spring Security (que pode ser um redirecionamento ou página de erro).
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * Construtor para injeção de dependência do ObjectMapper.
     * @param objectMapper Objeto para serialização/desserialização JSON.
     */
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Este método é chamado quando um usuário não autenticado tenta acessar um recurso protegido.
     * Ele constrói uma resposta HTTP 401 Unauthorized com um corpo JSON contendo uma mensagem de erro.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse to which the 401 response will be written.
     * @param authException The AuthenticationException that caused this entry point to be triggered.
     * @throws IOException If an input or output exception occurs.
     * @throws ServletException If a servlet-specific error occurs.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // Cria o objeto de resposta de erro personalizado.
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(), // Código 401
                HttpStatus.UNAUTHORIZED,       // Enum HttpStatus para a descrição "Unauthorized"
                "Acesso nao autorizado. Por favor, realize login para continuar."
        );

        // Define o tipo de conteúdo da resposta para JSON.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getOutputStream().println(objectMapper.writeValueAsString(errorResponse));
    }
}