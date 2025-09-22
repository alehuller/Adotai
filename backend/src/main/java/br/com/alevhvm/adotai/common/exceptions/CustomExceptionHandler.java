package br.com.alevhvm.adotai.common.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import br.com.alevhvm.adotai.administrador.exception.AdmIsMasterException;
import br.com.alevhvm.adotai.administrador.exception.AdministradorNotFoundException;
import br.com.alevhvm.adotai.administrador.exception.AdministradorNuloException;
import br.com.alevhvm.adotai.adocao.exception.AdocaoNotFoundException;
import br.com.alevhvm.adotai.adocao.exception.AdocaoNulaException;
import br.com.alevhvm.adotai.animal.exception.AnimalNotFoundException;
import br.com.alevhvm.adotai.ong.exception.CepException;
import br.com.alevhvm.adotai.ong.exception.ConversaoEnderecoParaJsonException;
import br.com.alevhvm.adotai.ong.exception.ConversaoJsonParaEnderecoException;
import br.com.alevhvm.adotai.ong.exception.ConversaoJsonParaRedeException;
import br.com.alevhvm.adotai.ong.exception.ConversaoRedeParaJsonException;
import br.com.alevhvm.adotai.ong.exception.OngNotFoundException;

import java.util.Date;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                List.of(ex.getMessage()),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(AuthorizationDeniedException ex, WebRequest request) {
        System.out.println("Exceção capturada: " + ex.getClass().getSimpleName());
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                List.of(ex.getMessage() + ": Você não tem permissão para acessar este recurso"),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                List.of(ex.getMessage()),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ExceptionResponse> handleRequiredObjectIsNull(NullPointerException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                List.of(ex.getMessage()),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public final ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                List.of(ex.getMessage()),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                List.of(ex.getMessage()),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        // Extrai mensagens de erro dos campos
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                errors,
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidacaoException.class)
    public final ResponseEntity<ExceptionResponse> handleMultipleValidaptionException(ValidacaoException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getErros(), 
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AdministradorNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleAdministradorNotFound(
        AdministradorNotFoundException ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AdmIsMasterException.class)
    public final ResponseEntity<ExceptionResponse> handleAdmIsMaster(AdmIsMasterException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AdministradorNuloException.class)
    public final ResponseEntity<ExceptionResponse> handleAdministradorNulo(AdministradorNuloException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AdocaoNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleAdocaoNotFound(
        AdocaoNotFoundException ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AdocaoNulaException.class)
    public final ResponseEntity<ExceptionResponse> handleAdocaoNula(AdocaoNulaException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnimalNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleAnimalNotFound(
        AnimalNotFoundException ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConversaoEnderecoParaJsonException.class)
    public final ResponseEntity<ExceptionResponse> handleConversaoEnderecoParaJson(ConversaoEnderecoParaJsonException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConversaoJsonParaEnderecoException.class)
    public final ResponseEntity<ExceptionResponse> handleConversaoJsonParaEndereco(ConversaoJsonParaEnderecoException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConversaoRedeParaJsonException.class)
    public final ResponseEntity<ExceptionResponse> handleConversaoRedeParaJson(ConversaoRedeParaJsonException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConversaoJsonParaRedeException.class)
    public final ResponseEntity<ExceptionResponse> handleConversaoJsonParaRede(ConversaoJsonParaRedeException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CepException.class)
    public final ResponseEntity<ExceptionResponse> handleCep(CepException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(), 
            List.of(ex.getMessage()), 
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OngNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleOngNotFound(
        OngNotFoundException ex, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            List.of(ex.getMessage()),
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}