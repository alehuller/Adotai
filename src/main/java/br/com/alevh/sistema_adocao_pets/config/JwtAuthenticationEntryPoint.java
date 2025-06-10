package br.com.alevh.sistema_adocao_pets.config;

import br.com.alevh.sistema_adocao_pets.exceptions.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

// chat gpt q fez, ver melhor o que faz
// precisa disso pq trata as exceções que n chegam na controller, ou seja q o ControllerAdvice n pega

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        String json = new ObjectMapper().writeValueAsString(
                new ExceptionResponse(
                        new Date(),
                        "Acesso negado: " + authException.getMessage(),
                        request.getRequestURI()
                )
        );

        response.getWriter().write(json);
    }
}