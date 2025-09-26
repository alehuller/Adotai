package br.com.alevhvm.adotai.animal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.alevhvm.adotai.animal.controller.AnimalController;
import br.com.alevhvm.adotai.ong.controller.OngController;
import br.com.alevhvm.adotai.ong.exception.OngNotFoundException;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.dto.AnimalFiltroDTO;
import br.com.alevhvm.adotai.animal.exception.AnimalNotFoundException;
import br.com.alevhvm.adotai.animal.exception.AnimalNuloException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private static final Logger logger = LoggerFactory.getLogger(AnimalService.class);

    private final AnimalRepository animalRepository;

    private final OngRepository ongRepository;

    private final PagedResourcesAssembler<AnimalDTO> assembler;

    private final Validator validator;

    @Transactional(readOnly = true)
    public PagedModel<EntityModel<AnimalDTO>> findAll(Pageable pageable) {
        logger.debug("Iniciando busca de todos os animais");

        Page<Animal> animalPage = animalRepository.findAll(pageable);

        logger.info("Encontrada(s) {} página(s), com {} Animal(is)", animalPage.getTotalPages(), animalPage.getTotalElements());

        Page<AnimalDTO> animalDtosPage = animalPage.map(a -> DozerMapper.parseObject(a, AnimalDTO.class));
        animalDtosPage
                .map(a -> a.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(a.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(AnimalController.class).listarAnimais(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(animalDtosPage, link);
        // return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AnimalDTO findById(Long id) {
        logger.debug("Iniciando busca do Animal de id {}", id);

        Animal entity = animalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Animal nao encontrado para id = {}", id);
                    return new AnimalNotFoundException("Animal não encontrado.");
                });

        AnimalDTO dto = DozerMapper.parseObject(entity, AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(id)).withSelfRel());

        logger.info("Animal encontrado com sucesso: id={}, nome={}", entity.getIdAnimal(), entity.getNome());
        return dto;
    }

    @Transactional(readOnly = true)
    public AnimalDTO findByNome(String nome) {
        logger.debug("Iniciando busca do animal com nome = {}", nome);

        Animal entity = getAnimalEntityByNome(nome);

        AnimalDTO dto = DozerMapper.parseObject(entity, AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorNome(nome)).withSelfRel());

        logger.info("Animal encontrado com sucesso: id={}, nome={}", entity.getIdAnimal(), entity.getNome());
        return dto;
    }

    @Transactional(readOnly = true)
    public PagedModel<EntityModel<AnimalDTO>> findAllByOngNome(String nomeUsuario, Pageable pageable) {
        logger.debug("Iniciando busca de todos os animais de uma ong");

        Page<Animal> animalPage = animalRepository.findByOngNomeUsuario(nomeUsuario, pageable);

        logger.info("Encontrada(s) {} página(s), com {} Animal(is) da ong {}", animalPage.getTotalPages(), animalPage.getTotalElements(), nomeUsuario);

        Page<AnimalDTO> animalDtoPage = animalPage.map(a -> DozerMapper.parseObject(a, AnimalDTO.class));

        animalDtoPage = animalDtoPage.map(
                dto -> dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel()));

        Link selfLink = linkTo(methodOn(OngController.class)
                .listarAnimaisDeUmaOng(nomeUsuario, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return assembler.toModel(animalDtoPage, selfLink);
    }

    @Transactional(readOnly = true)
    public Page<AnimalDTO> filtrarAnimais(AnimalFiltroDTO filtro, Pageable pageable) {
        logger.debug("Iniciando filtro de animais");
        Page<Animal> animais = animalRepository.filtrarAnimaisNativo(filtro, pageable);
        logger.info("Filtro finalizado de animais");
        return animais.map(animal -> DozerMapper.parseObject(animal, AnimalDTO.class));
    }

    @Transactional
    public AnimalDTO create(AnimalDTO animal) {
        logger.debug("Iniciando a criação de um Animal");

        if (animal == null) {
            logger.warn("Nao ha dados");
            throw new AnimalNuloException("Não há dados");
        }   

        Ong ong = ongRepository.findById(animal.getIdOng())
                .orElseThrow(() -> {
                    logger.warn("Ong nao encontrada por id = {}", animal.getIdOng());
                    return new OngNotFoundException("Ong não encontrada");
                });

        Animal entity = DozerMapper.parseObject(animal, Animal.class);
        entity.setOng(ong);
        AnimalDTO dto = DozerMapper.parseObject(animalRepository.save(entity), AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel());

        logger.info("Animal {} criado com sucesso.", entity.getNome());
        return dto;
    }

    @Transactional
    public AnimalDTO update(AnimalDTO animal, String nome) {
        logger.debug("Iniciando atualização de animal com nome = {}", nome);

        if (animal == null) {
            logger.warn("Nao ha dados");
            throw new AnimalNuloException("Não há dados");
        }

        Animal entity = getAnimalEntityByNome(nome);

        Ong ong = ongRepository.findById(animal.getIdOng())
                .orElseThrow(() -> {
                    logger.warn("Ong nao encontrada para o id = {}", animal.getIdOng());
                    return new OngNotFoundException("Ong não encontrada.");
                });

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

        logger.info("Animal {} atualizado com sucesso.", entity.getNome());
        return dto;
    }

    @Transactional
    public AnimalDTO partialUpdate(String nome, Map<String, Object> updates) {
        logger.debug("Iniciando atualização parcial de animal com nome = {}", nome);

        Animal animal = getAnimalEntityByNome(nome);

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
                        if (descricaoAtualizado.getGeral() != null)
                            descricaoOriginal.setGeral(descricaoAtualizado.getGeral());
                        if (descricaoAtualizado.getHistoricoSaude() != null)
                            descricaoOriginal.setHistoricoSaude(descricaoAtualizado.getHistoricoSaude());
                        if (descricaoAtualizado.getVacinacao() != null)
                            descricaoOriginal.setVacinacao(descricaoAtualizado.getVacinacao());
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

        AnimalDTO dto = DozerMapper.parseObject(animal, AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel());

        logger.info("Animal {} atualizado parcialmente com sucesso.", animal.getNome());
        return dto;
    }

    @Transactional
    public void delete(String nome) {
        logger.debug("Iniciando delecao do animal {}", nome);
        var animal = getAnimalEntityByNome(nome);
        animalRepository.deleteByNome(nome);
        logger.info("Animal {} deletado com sucesso.", nome);
    }

    @Transactional(readOnly = true)
    public Animal getAnimalEntityByNome(String nome) {
        return animalRepository.findByNome(nome)
                .orElseThrow(() -> {
                    logger.warn("Animal nao encontrado para nome = {}", nome);
                    return new AnimalNotFoundException("Animal não encontrado");
                });
    }
}