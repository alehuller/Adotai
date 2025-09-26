package br.com.alevhvm.adotai.adocao.service;

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

import br.com.alevhvm.adotai.adocao.controller.AdocaoController;
import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.exception.AdocaoNotFoundException;
import br.com.alevhvm.adotai.adocao.exception.AdocaoNulaException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.animal.exception.AnimalNotFoundException;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.ong.exception.OngNotFoundException;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.usuario.exception.UsuarioNotFoundException;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.adocao.repository.AdocaoRepository;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdocaoService {

    private static final Logger logger = LoggerFactory.getLogger(AdocaoService.class);

    private final AdocaoRepository adocaoRepository;

    private final AnimalRepository animalRepository;

    private final UsuarioRepository usuarioRepository;

    private final OngRepository ongRepository;

    private final PagedResourcesAssembler<AdocaoDTO> assembler;

    private final Validator validator;

    @Transactional(readOnly = true)
    public PagedModel<EntityModel<AdocaoDTO>> findAll(Pageable pageable) {
        logger.debug("Iniciando busca de todos as adocoes");

        Page<Adocao> adocaoPage = adocaoRepository.findAll(pageable);

        logger.info("Encontrada(s) {} pagina(s), com {} Adocao(oes)", adocaoPage.getTotalPages(), adocaoPage.getTotalElements());

        Page<AdocaoDTO> adocaoDtosPage = adocaoPage.map(a -> DozerMapper.parseObject(a, AdocaoDTO.class));
        adocaoDtosPage
                .map(a -> a.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(a.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(AdocaoController.class).listarAdocoes(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(adocaoDtosPage, link);
    }

    @Transactional(readOnly = true)
    public AdocaoDTO findById(Long id) {
        logger.debug("Iniciando busca da adocao com id = {}", id);

        Adocao entity = getAdocaoEntityById(id);

        AdocaoDTO dto = DozerMapper.parseObject(entity, AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(id)).withSelfRel());

        logger.info("Adocao encontrada com sucesso: id={}", entity.getIdAdocao());
        return dto;
    }

    @Transactional
    public AdocaoDTO create(AdocaoDTO adocao) {
        logger.debug("Iniciando a criacao de uma Adocao");

        if (adocao == null) {
            logger.warn("Nao ha dados");
            throw new AdocaoNulaException("Não há dados");
        }

        Animal animal = animalRepository.findById(adocao.getIdAnimal())
                .orElseThrow(() -> {
                    logger.warn("Animal não encontrado para id = {}", adocao.getIdAnimal());
                    return new AnimalNotFoundException("Animal não encontrado");
                });

        Usuario usuario = usuarioRepository.findById(adocao.getIdUsuario())
                .orElseThrow(() -> {
                    logger.warn("Usuario não encontrado para id = {}", adocao.getIdUsuario());
                    return new UsuarioNotFoundException("Usuário não encontrado");
                });

        Adocao entity = DozerMapper.parseObject(adocao, Adocao.class);
        entity.setAnimal(animal);
        entity.setUsuario(usuario);
        AdocaoDTO dto = DozerMapper.parseObject(adocaoRepository.save(entity), AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel());

        logger.info("Adocao criado com sucesso.");
        return dto;
    }

    @Transactional
    public AdocaoDTO update(AdocaoDTO adocao, Long id) {
        logger.debug("Iniciando atualização de adocao com id = {}", id);

        if (adocao == null) {
            logger.warn("Nao ha dados");
            throw new AdocaoNulaException("Não há dados");
        }
            

        Adocao entity = getAdocaoEntityById(id);

        Usuario usuario = usuarioRepository.findById(adocao.getIdUsuario())
                .orElseThrow(() -> {
                    logger.warn("Usuario não encontrado para id = {}", adocao.getIdUsuario());
                    return new UsuarioNotFoundException("Usuário não encontrado");
                });

        Animal animal = animalRepository.findById(adocao.getIdAnimal())
                .orElseThrow(() -> {
                    logger.warn("Animal não encontrado para id = {}", adocao.getIdAnimal());
                    return new AnimalNotFoundException("Animal não encontrado");
                });

        Ong ong = ongRepository.findById(animal.getOng().getIdOng())
                .orElseThrow(() -> {
                    logger.warn("Ong não encontrado para id = {}", animal.getOng().getIdOng());
                    return new OngNotFoundException("Ong não encontrada.");
                });

        entity.setDataAdocao(adocao.getDataAdocao());
        entity.setStatus(adocao.getStatus());
        entity.setUsuario(usuario);
        animal.setOng(ong);
        entity.setAnimal(animal);

        AdocaoDTO dto = DozerMapper.parseObject(adocaoRepository.save(entity), AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel());

        logger.info("Adocao {} atualizada com sucesso.", entity.getIdAdocao());
        return dto;
    }

    @Transactional
    public AdocaoDTO partialUpdate(Long id, Map<String, Object> updates) {
        logger.debug("Iniciando atualizacao parcial de adocao com id = {}", id);

        Adocao adocao = getAdocaoEntityById(id);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Adocao.class, campo);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, adocao, mapper.convertValue(valor, field.getType()));
            }
        });

        // Mapeia a entidade para o DTO
        AdocaoDTO adocaoDTO = DozerMapper.parseObject(adocao, AdocaoDTO.class);

        // Faz a validação do DTO
        Set<ConstraintViolation<AdocaoDTO>> violations = validator.validate(adocaoDTO);

        // Se houver erros de validação, lança uma exceção
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<AdocaoDTO> violation : violations) {
                errors.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Erro de validação: " + errors.toString(), violations);
        }

        adocaoRepository.save(adocao);

        AdocaoDTO dto = DozerMapper.parseObject(adocao, AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(id)).withSelfRel());

        logger.info("Adocao {} atualizada com sucesso.", adocao.getIdAdocao());
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        logger.debug("Iniciando delecao da adocao {}", id);

        if (!adocaoRepository.existsById(id)) {
            logger.warn("Adoção de id {} não encontrada.", id);
            throw new AdocaoNotFoundException("Adoção não encontrada.");
        }
        adocaoRepository.deleteById(id);

        logger.info("Adocao de id {} deletado com sucesso.", id);
    }

    @Transactional(readOnly = true)
    public Adocao getAdocaoEntityById(Long id) {
        return adocaoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Adocao nao encontrado para id = {}", id);
                    return new AdocaoNotFoundException("Adoção não encontrada.");
                });
    }
}