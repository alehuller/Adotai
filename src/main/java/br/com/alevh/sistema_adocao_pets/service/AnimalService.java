package br.com.alevh.sistema_adocao_pets.service;

import br.com.alevh.sistema_adocao_pets.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import br.com.alevh.sistema_adocao_pets.controller.AnimalController;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngDTO;
import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.model.Animal;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import br.com.alevh.sistema_adocao_pets.repository.AnimalRepository;
import br.com.alevh.sistema_adocao_pets.repository.OngRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    private final OngRepository ongRepository;

    private final OngService ongService;

    private final PagedResourcesAssembler<AnimalDTO> assembler;

    private final Validator validator;

    public PagedModel<EntityModel<AnimalDTO>> findAll(Pageable pageable) {

        Page<Animal> animalPage = animalRepository.findAll(pageable);

        Page<AnimalDTO> animalDtosPage = animalPage.map(a -> DozerMapper.parseObject(a, AnimalDTO.class));
        animalDtosPage.map(a -> a.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(a.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(AnimalController.class).listarAnimais(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(animalDtosPage, link);
        // return usuarioRepository.findAll();
    }

    public AnimalDTO findById(Long id) {

        Animal entity = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado."));

        AnimalDTO dto = DozerMapper.parseObject(entity, AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(id)).withSelfRel());
        return dto;
    }

    public AnimalDTO create(AnimalDTO animal) {
        if(animal == null) throw new RequiredObjectIsNullException();

        Ong ong = ongRepository.findById(animal.getIdOng())
            .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrada"));

        Animal entity = DozerMapper.parseObject(animal, Animal.class);
        entity.setOng(ong);
        AnimalDTO dto = DozerMapper.parseObject(animalRepository.save(entity), AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        animalRepository.deleteById(id);
    }

    public AnimalDTO update(AnimalDTO animal, Long id) { 
        
        if(animal == null) throw new RequiredObjectIsNullException();
        
        Animal entity = animalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado."));
        
        OngDTO ongDTO = ongService.findById(animal.getIdOng());
        Ong ong = DozerMapper.parseObject(ongDTO, Ong.class);

        entity.setNome(animal.getNome());
        entity.setEspecie(animal.getEspecie());
        entity.setRaca(animal.getRaca());
        entity.setDataNascimento(animal.getDataNascimento());
        entity.setFoto(animal.getFoto());
        entity.setDescricao(animal.getDescricao());
        entity.setPorte(animal.getPorte());
        entity.setSexo(animal.getSexo());
        entity.setStatus(animal.getStatus());
        entity.setOng(ong);

        AnimalDTO dto = DozerMapper.parseObject(animalRepository.save(entity), AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public AnimalDTO partialUpdate(Long id, Map<String, Object> updates) {
        Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado."));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Animal.class, campo);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, animal, mapper.convertValue(valor, field.getType()));
            }
        });
        
        // Mapeia a entidade para o DTO
        AnimalDTO animalDTO = DozerMapper.parseObject(animal, AnimalDTO.class);

        // Faz a validação do DTO
        Set<ConstraintViolation<AnimalDTO>> violations = validator.validate(animalDTO);

        // Se houver erros de validação, lança uma exceção
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<AnimalDTO> violation : violations) {
                errors.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Erro de validação: " + errors.toString(), violations);
        }

        animalRepository.save(animal);
        return DozerMapper.parseObject(animal, AnimalDTO.class);
    }
}