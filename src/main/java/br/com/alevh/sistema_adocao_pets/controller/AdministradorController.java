package br.com.alevh.sistema_adocao_pets.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.service.AdministradorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/administradores")
@Tag(name = "Administradores", description = "Endpoints para manipulação do registro de Administradores.")
public class AdministradorController {
    
    private final AdministradorService administradorService;
}
