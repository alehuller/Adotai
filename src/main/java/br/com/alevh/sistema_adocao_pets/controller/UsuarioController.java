package br.com.alevh.sistema_adocao_pets.controller;

import br.com.alevh.sistema_adocao_pets.data.dto.security.RegistroDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.controller.docs.UsuarioControllerDocs;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioUpdateDTO;
import br.com.alevh.sistema_adocao_pets.service.UsuarioService;
import br.com.alevh.sistema_adocao_pets.util.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
@Tag(name = "Usuários", description = "Endpoints para manipulação do registro dos usuários.")
public class UsuarioController implements UsuarioControllerDocs {

        private final UsuarioService usuarioService;

        @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML })
        public ResponseEntity<PagedModel<EntityModel<UsuarioDTO>>> listarUsuarios(
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

                Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
                return ResponseEntity.ok(usuarioService.findAll(pageable));
        }

        @GetMapping(value = "/id/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public UsuarioDTO acharUsuarioPorId(@PathVariable(value = "id") Long id) {
                return usuarioService.findById(id);
        }

        @GetMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public UsuarioDTO acharUsuarioPorNomeUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario) {
                return usuarioService.findByNomeUsuario(nomeUsuario);
        }

        @PostMapping(value = "/signup", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public UsuarioDTO registrarUsuario(@RequestBody @Valid RegistroDTO registroDTO) {
                return usuarioService.create(registroDTO);
        }

        @DeleteMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<?> deletarPorNomeUsuario(@PathVariable(name = "nomeUsuario") String nomeUsuario) {
                usuarioService.delete(nomeUsuario);
                return ResponseEntity.noContent().build();
        }

        @PutMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public UsuarioDTO atualizarUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario, @RequestBody @Valid UsuarioUpdateDTO usuario) {
                return usuarioService.update(usuario, nomeUsuario);
        }

        @PatchMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                MediaType.APPLICATION_XML })
        public ResponseEntity<UsuarioDTO> atualizarParcialUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario,
                                                                  @RequestBody Map<String, Object> updates) {
                UsuarioDTO usuarioAtualizado = usuarioService.partialUpdate(nomeUsuario, updates);
                return ResponseEntity.ok(usuarioAtualizado);
        }

        @GetMapping(value = "/{nomeUsuario}/adocoes", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                MediaType.APPLICATION_XML })
        public ResponseEntity<PagedModel<EntityModel<AdocaoDTO>>> listarAdocoesPorNomeUsuario(
                @PathVariable("nomeUsuario") String nomeUsuario,
                @RequestParam(value = "page", defaultValue = "0") int page,
                @RequestParam(value = "size", defaultValue = "10") int size,
                @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, sortDirection, "idAdocao");

                PagedModel<EntityModel<AdocaoDTO>> pagedModel = usuarioService.findAllAdocoesByNomeUsuario(nomeUsuario,
                        pageable);

                return ResponseEntity.ok(pagedModel);
        }
}