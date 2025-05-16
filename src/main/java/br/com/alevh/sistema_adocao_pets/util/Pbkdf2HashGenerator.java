package br.com.alevh.sistema_adocao_pets.util;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Pbkdf2HashGenerator {

    public static void main(String[] args) {
        // Parâmetros do seu PasswordEncoder
        String secret = ""; // vazio
        int iterations = 8;
        int keyLength = 256;
        Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm algorithm = Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

        // Crie o encoder
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(secret, iterations, keyLength, algorithm);

        // Senha a ser codificada
        String rawPassword = "senha123";

        // Codifique a senha
        String encodedPassword = pbkdf2Encoder.encode(rawPassword);

        // Exiba o hash gerado
        System.out.println("Senha codificada: " + encodedPassword);
    }
}
