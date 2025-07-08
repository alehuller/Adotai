package br.com.alevh.sistema_adocao_pets.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.controller.docs.AnimalControllerDocs;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalFiltroDTO;
import br.com.alevh.sistema_adocao_pets.service.AnimalService;
import br.com.alevh.sistema_adocao_pets.util.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/animais")
@Tag(name = "Animais", description = "Endpoints para manipulação do registro dos animais.")
public class AnimalController implements AnimalControllerDocs{

        private final AnimalService animalService;

        @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML })
        public ResponseEntity<PagedModel<EntityModel<AnimalDTO>>> listarAnimais(
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

                Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
                return ResponseEntity.ok(animalService.findAll(pageable));
        }

        @GetMapping(value = "/id/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public AnimalDTO acharAnimalPorId(@PathVariable(value = "id") Long id) {
                return animalService.findById(id);
        }

        @GetMapping(value = "/{nome}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public AnimalDTO acharAnimalPorNome(@PathVariable(value = "nome") String nome) {
                return animalService.findByNome(nome);
        }

        @PostMapping(value = "/filtro", produces = MediaType.APPLICATION_JSON)
        public ResponseEntity<Page<AnimalDTO>> filtrarAnimais(
                @RequestBody AnimalFiltroDTO filtro,
                @RequestParam(value = "page", defaultValue = "0") int page,
                @RequestParam(value = "size", defaultValue = "10") int size,
                @RequestParam(value = "direction", defaultValue = "asc") String direction) {
                
                Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, sortDirection, "nome");

                Page<AnimalDTO> resultados = animalService.filtrarAnimais(filtro, pageable);
                return ResponseEntity.ok(resultados);
        }

        @PostMapping(value = "/registro", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public AnimalDTO registrarAnimal(@RequestBody AnimalDTO animal) {
                return animalService.create(animal);
        }

        @PutMapping(value = "/{nome}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public AnimalDTO atualizarAnimal(@PathVariable(value = "nome") String nome, @RequestBody AnimalDTO animal) {
                return animalService.update(animal, nome);
        }

        @PatchMapping(value = "/{nome}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<AnimalDTO> atualizarParcialAnimal(@PathVariable(value = "nome") String nome, @RequestBody Map<String, Object> updates) {
                AnimalDTO animalAtualizado = animalService.partialUpdate(nome, updates);
                return ResponseEntity.ok(animalAtualizado);
        }

        @DeleteMapping(value = "/{nome}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<?> deletarPorNome(@PathVariable(name = "nome") String nome) {
                animalService.delete(nome);
                return ResponseEntity.noContent().build();
        }
}