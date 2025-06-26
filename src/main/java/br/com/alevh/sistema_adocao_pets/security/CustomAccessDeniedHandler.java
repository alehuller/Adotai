package br.com.alevh.sistema_adocao_pets.security;

import br.com.alevh.sistema_adocao_pets.exceptions.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException acessException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = new ObjectMapper().writeValueAsString(
                new ExceptionResponse(
                        new Date(),
                        List.of(acessException.getMessage()+": Você não tem permissão para acessar este recurso"),
                        request.getRequestURI()));
        response.getWriter().write(json);
    }
}