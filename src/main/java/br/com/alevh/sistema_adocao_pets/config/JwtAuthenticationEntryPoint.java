package br.com.alevh.sistema_adocao_pets.config;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevh.sistema_adocao_pets.exceptions.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// chat gpt q fez, ver melhor o que faz
// precisa disso pq trata as exceções que n chegam na controller, ou seja q o ControllerAdvice n pega

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

                String json = new ObjectMapper().writeValueAsString(
                                new ExceptionResponse(
                                                new Date(),
                                        List.of("Acesso negado: " + authException.getMessage()),
                                                request.getRequestURI()));

                response.getWriter().write(json);
        }
}