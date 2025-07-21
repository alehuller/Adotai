package br.com.alevhvm.adotai.usuario.controller;

import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevhvm.adotai.common.docs.UsuarioControllerDocs;
import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioUpdateDTO;
import br.com.alevhvm.adotai.usuario.service.UsuarioService;
import br.com.alevhvm.adotai.common.util.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

        @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<UsuarioDTO> acharUsuarioPorId(@PathVariable(value = "id") Long id) {
                UsuarioDTO dto = usuarioService.findById(id);
                return ResponseEntity.ok(dto);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @GetMapping(value = "/nomeUsuario/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<UsuarioDTO> acharUsuarioPorNomeUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario) {
                UsuarioDTO dto = usuarioService.findByNomeUsuario(nomeUsuario);
                return ResponseEntity.ok(dto);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @GetMapping(value = "/{nomeUsuario}/adocoes", produces = { MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_YML,
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

        @PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody @Valid RegistroDTO registroDTO) {
                UsuarioDTO criado = usuarioService.create(registroDTO);
                URI location = linkTo(methodOn(UsuarioController.class).acharUsuarioPorId(criado.getKey())).toUri();
                return ResponseEntity.created(location).body(criado);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @PutMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario,
                        @RequestBody @Valid UsuarioUpdateDTO usuario) {
                UsuarioDTO dto = usuarioService.update(usuario, nomeUsuario);
                return ResponseEntity.ok(dto);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @PatchMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<UsuarioDTO> atualizarParcialUsuario(
                        @PathVariable(value = "nomeUsuario") String nomeUsuario,
                        @RequestBody Map<String, Object> updates) {
                UsuarioDTO usuarioAtualizado = usuarioService.partialUpdate(nomeUsuario, updates);
                return ResponseEntity.ok(usuarioAtualizado);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @DeleteMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<?> deletarPorNomeUsuario(@PathVariable(name = "nomeUsuario") String nomeUsuario) {
                usuarioService.delete(nomeUsuario);
                return ResponseEntity.noContent().build();
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @PostMapping("/favoritar/{nomeUsuario}/{animalId}")
        public ResponseEntity<Map<String, Object>> toggleFavorito(
                        @PathVariable String nomeUsuario,
                        @PathVariable Long animalId) {

                boolean novoEstado = usuarioService.toggleAnimalFavorito(nomeUsuario, animalId);

                Map<String, Object> response = new HashMap<>();
                response.put("animalId", animalId);
                response.put("favorito", novoEstado);
                response.put("mensagem",
                                novoEstado ? "Animal favoritado com sucesso" : "Animal removido dos favoritos");

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @GetMapping(value = "/{nomeUsuario}/favoritos", produces = {
                        MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML
        })
        public ResponseEntity<PagedModel<EntityModel<AnimalDTO>>> listarAnimaisFavoritos(
                        @PathVariable("nomeUsuario") String nomeUsuario,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction,
                        @RequestParam(value = "sort", defaultValue = "nome") String sort) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, sortDirection, sort);

                PagedModel<EntityModel<AnimalDTO>> pagedModel = usuarioService
                                .findAnimaisFavoritosByNomeUsuario(nomeUsuario, pageable);

                return ResponseEntity.ok(pagedModel);
        }
}