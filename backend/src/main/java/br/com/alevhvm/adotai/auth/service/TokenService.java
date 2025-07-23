package br.com.alevhvm.adotai.auth.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class TokenService {
    // faz com que os hashs sejam unicos na aplicação (evitar padrões -> evitar
    // ataques)
    // guardar à 7 chaves
    @Value("${security.jwt.secret}") // application.properties
    private String secret;

    @Value("${security.jwt.token.expire-length}")
    private long expireLength;

    // gere tokens de Ong
    public String generateToken(UserDetails userDetails) {

        try {
            // algoritmo de geração de token, vem dentro da biblioteca do jwt
            // recebe uma secret
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // geração do token em si
            String token = JWT.create()
                    .withIssuer("auth-api") // emissor do token -> nome aplicação, colocar o nome que quiser
                    .withSubject(userDetails.getUsername()) // quem recebe o token -> usuário
                    .withExpiresAt(genExpirationDate()) // tempo pra expirar o token
                    .sign(algorithm); // fazer a assinatura/geração final com o algoritmo ;
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    // gere tokens de Usuario

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("auth-api") // Define o emissor esperado do token
                .build() // Constrói o verificador JWT
                .verify(token) // Verifica e decodifica o token JWT fornecido
                .getSubject(); // Retorna o "subject" do token -> identificador do usuário
    }

    // tempo de expiração pro token
    private Instant genExpirationDate() {
        return Instant.now().plusMillis(expireLength);
    }
}