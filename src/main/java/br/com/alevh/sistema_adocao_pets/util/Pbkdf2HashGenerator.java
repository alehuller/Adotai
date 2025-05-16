package br.com.alevh.sistema_adocao_pets.util;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Pbkdf2HashGenerator {

    public static void main(String[] args) {
        String secret = "";
        int iterations = 8;
        int keyLength = 256;
        Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm algorithm = Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(secret, iterations, keyLength, algorithm);

        String rawPassword = "senha123";

        String encodedPassword = pbkdf2Encoder.encode(rawPassword);

        System.out.println("Senha codificada: " + encodedPassword);
    }
}
