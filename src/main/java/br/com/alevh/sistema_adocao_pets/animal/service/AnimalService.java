package br.com.alevh.sistema_adocao_pets.animal.service;

import br.com.alevh.sistema_adocao_pets.common.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.common.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import br.com.alevh.sistema_adocao_pets.animal.controller.AnimalController;
import br.com.alevh.sistema_adocao_pets.ong.controller.OngController;
import br.com.alevh.sistema_adocao_pets.common.vo.DescricaoVO;
import br.com.alevh.sistema_adocao_pets.animal.dto.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.animal.dto.AnimalFiltroDTO;
import br.com.alevh.sistema_adocao_pets.common.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.animal.model.Animal;
import br.com.alevh.sistema_adocao_pets.ong.model.Ong;
import br.com.alevh.sistema_adocao_pets.animal.repository.AnimalRepository;
import br.com.alevh.sistema_adocao_pets.ong.repository.OngRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    private final OngRepository ongRepository;

    private final PagedResourcesAssembler<AnimalDTO> assembler;

    private final Validator validator;

    public PagedModel<EntityModel<AnimalDTO>> findAll(Pageable pageable) {

        Page<Animal> animalPage = animalRepository.findAll(pageable);

        Page<AnimalDTO> animalDtosPage = animalPage.map(a -> DozerMapper.parseObject(a, AnimalDTO.class));
        animalDtosPage
                .map(a -> a.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(a.getKey())).withSelfRel()));

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

    public AnimalDTO findByNome(String nome) {
        Animal entity = animalRepository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado."));

        AnimalDTO dto = DozerMapper.parseObject(entity, AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorNome(nome)).withSelfRel());
        return dto;
    }

    public PagedModel<EntityModel<AnimalDTO>> findAllByOngNome(String nomeUsuario, Pageable pageable) {

        Page<Animal> animalPage = animalRepository.findByOngNomeUsuario(nomeUsuario, pageable);

        Page<AnimalDTO> animalDtoPage = animalPage.map(a -> DozerMapper.parseObject(a, AnimalDTO.class));

        animalDtoPage = animalDtoPage.map(
                dto -> dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel()));

        Link selfLink = linkTo(methodOn(OngController.class)
                .listarAnimaisDeUmaOng(nomeUsuario, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return assembler.toModel(animalDtoPage, selfLink);
    }

    public Page<AnimalDTO> filtrarAnimais(AnimalFiltroDTO filtro, Pageable pageable) {
        Page<Animal> animais = animalRepository.filtrarAnimaisNativo(filtro, pageable);
        return animais.map(animal -> DozerMapper.parseObject(animal, AnimalDTO.class));
    }

    public AnimalDTO create(AnimalDTO animal) {
        if (animal == null)
            throw new RequiredObjectIsNullException();

        Ong ong = ongRepository.findById(animal.getIdOng())
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrada"));

        Animal entity = DozerMapper.parseObject(animal, Animal.class);
        entity.setOng(ong);
        AnimalDTO dto = DozerMapper.parseObject(animalRepository.save(entity), AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public AnimalDTO update(AnimalDTO animal, String nome) {

        if (animal == null)
            throw new RequiredObjectIsNullException();

        Animal entity = animalRepository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado."));

        Ong ong = ongRepository.findById(animal.getIdOng())
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrada."));

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

    public AnimalDTO partialUpdate(String nome, Map<String, Object> updates) {
        Animal animal = animalRepository.findByNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado."));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Animal.class, campo);
            if (field != null) {
                field.setAccessible(true);
                
                if (campo.equals("descricao") && valor instanceof Map<?, ?> valorMap) {
                    DescricaoVO descricaoOriginal = animal.getDescricao();
                    DescricaoVO descricaoAtualizado = mapper.convertValue(valor, DescricaoVO.class);

                    if (descricaoOriginal == null) {
                        animal.setDescricao(descricaoAtualizado);
                    } else {
                        if (descricaoAtualizado.getGeral() != null) descricaoOriginal.setGeral(descricaoAtualizado.getGeral());
                        if (descricaoAtualizado.getHistoricoSaude() != null) descricaoOriginal.setHistoricoSaude(descricaoAtualizado.getHistoricoSaude());
                        if (descricaoAtualizado.getVacinacao() != null) descricaoOriginal.setVacinacao(descricaoAtualizado.getVacinacao());
                    }
                } else {
                    ReflectionUtils.setField(field, animal, mapper.convertValue(valor, field.getType()));
                }
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

    @Transactional
    public void delete(String nome) {
        animalRepository.deleteByNome(nome);
    }
}