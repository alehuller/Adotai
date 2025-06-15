package br.com.alevh.sistema_adocao_pets.data.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull()
        @NotBlank()
        String identifier,

        @NotNull()
        @NotBlank()
        String password){
}
