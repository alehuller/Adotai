package br.com.alevhvm.adotai.administrador.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.dto.AdministradorUpdateDTO;
import br.com.alevhvm.adotai.administrador.service.AdministradorService;
import br.com.alevhvm.adotai.common.docs.AdministradorControllerDocs;
import br.com.alevhvm.adotai.common.enums.StatusConta;
import br.com.alevhvm.adotai.common.util.MediaType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/administradores")
@Tag(name = "Administradores", description = "Endpoints para manipulação do registro de Administradores.")
public class AdministradorController implements AdministradorControllerDocs{

    private final AdministradorService administradorService;

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML })
    public ResponseEntity<PagedModel<EntityModel<AdministradorDTO>>> listarAdministradores(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(administradorService.findAll(pageable));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML })
    public ResponseEntity<AdministradorDTO> acharAdministradorPorId(@PathVariable(value = "id") Long id) {
        AdministradorDTO dto = administradorService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/nomeUsuario/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML })
    public ResponseEntity<AdministradorDTO> acharAdministradorPorNomeUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario) {
        AdministradorDTO dto = administradorService.findByNomeUsuario(nomeUsuario);
        return ResponseEntity.ok(dto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                    MediaType.APPLICATION_XML })
    public ResponseEntity<AdministradorDTO> atualizarAdministrador(@PathVariable(value = "nomeUsuario") String nomeUsuario,
            @RequestBody @Valid AdministradorUpdateDTO administrador) {
        AdministradorDTO dto = administradorService.update(administrador, nomeUsuario);    
        return ResponseEntity.ok(dto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                    MediaType.APPLICATION_XML })
    public ResponseEntity<AdministradorDTO> atualizarParcialAdministrador(
            @PathVariable(value = "nomeUsuario") String nomeUsuario,
            @RequestBody Map<String, Object> updates) {
        AdministradorDTO administradorAtualizado = administradorService.partialUpdate(nomeUsuario, updates);
        return ResponseEntity.ok(administradorAtualizado);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML })
    public ResponseEntity<?> deletarPorNomeUsuario(@PathVariable(name = "nomeUsuario") String nomeUsuario) {
        administradorService.delete(nomeUsuario);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(value = "/normalParaMaster/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML })
    public ResponseEntity<AdministradorDTO> atualizarAdmNormalParaMaster(@PathVariable(value = "nomeUsuario") String nomeUsuario) {
        AdministradorDTO administradorAtualizado = administradorService.atualizarAdmNormalParaMaster(nomeUsuario);
        return ResponseEntity.ok(administradorAtualizado);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(value = "/mudarStatus/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML })
    public ResponseEntity<String> toggleStatus(@PathVariable(name = "nomeUsuario") String nomeUsuario) {
        String resposta = administradorService.toggleStatus(nomeUsuario);
        return ResponseEntity.ok(resposta);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(value = "/bloquearAdm/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML })
    public ResponseEntity<StatusConta> bloquearAdministrador(@PathVariable(name = "nomeUsuario") String nomeUsuario) {
        StatusConta status = administradorService.bloquearConta(nomeUsuario);
        return ResponseEntity.ok(status);
    }
}
