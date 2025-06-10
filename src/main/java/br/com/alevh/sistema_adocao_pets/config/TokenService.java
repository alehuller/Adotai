package br.com.alevh.sistema_adocao_pets.config;

import br.com.alevh.sistema_adocao_pets.exceptions.InvalidJwtAuthenticationException;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    //faz com que os hashs sejam unicos na aplicação (evitar padrões -> evitar ataques)
    // guardar à 7 chaves
    @Value("${security.jwt.secret}") // application.properties
    private String secret;

    public String generateToken(Usuario usuario){

        try {
            // algoritmo de geração de token, vem dentro da biblioteca do jwt
            // recebe uma secret
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // geração do token em si
            String token = JWT.create()
                    .withIssuer("auth-api") // emissor do token -> nome aplicação, colocar o nome que quiser
                    .withSubject(usuario.getUsername()) // quem recebe o token -> usuário
                    .withExpiresAt(genExpirationDate()) //  tempo pra expirar o token
                    .sign(algorithm); // fazer a assinatura/geração final com o algoritmo ;
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api") // Define o emissor esperado do token
                    .build() // Constrói o verificador JWT
                    .verify(token) // Verifica e decodifica o token JWT fornecido
                    .getSubject(); // Retorna o "subject" do token -> identificador do usuário
        } catch (JWTVerificationException e) {
            throw new AuthenticationCredentialsNotFoundException("Token JWT inválido.");
        } catch (InvalidJwtAuthenticationException e){
            throw new InvalidJwtAuthenticationException("Token JWT ausente.");
        }
    }

    // tempo de expiração pro token
    private Instant genExpirationDate(){
        return LocalDateTime.now() // hora atual
                .plusHours(2) // mais duas horas
                .toInstant(ZoneOffset.of("-03:00")); // no fuso do Brasil
    }
}
