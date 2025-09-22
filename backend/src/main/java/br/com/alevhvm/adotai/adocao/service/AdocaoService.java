package br.com.alevhvm.adotai.adocao.service;

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

import br.com.alevhvm.adotai.adocao.controller.AdocaoController;
import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.exception.AdocaoNotFoundException;
import br.com.alevhvm.adotai.adocao.exception.AdocaoNulaException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.ong.model.Ong;
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

    private final AdocaoRepository adocaoRepository;

    private final AnimalRepository animalRepository;

    private final UsuarioRepository usuarioRepository;

    private final OngRepository ongRepository;

    private final PagedResourcesAssembler<AdocaoDTO> assembler;

    private final Validator validator;

    public PagedModel<EntityModel<AdocaoDTO>> findAll(Pageable pageable) {

        Page<Adocao> adocaoPage = adocaoRepository.findAll(pageable);

        Page<AdocaoDTO> adocaoDtosPage = adocaoPage.map(a -> DozerMapper.parseObject(a, AdocaoDTO.class));
        adocaoDtosPage
                .map(a -> a.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(a.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(AdocaoController.class).listarAdocoes(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(adocaoDtosPage, link);
    }

    public AdocaoDTO findById(Long id) {

        Adocao entity = adocaoRepository.findById(id)
                .orElseThrow(() -> new AdocaoNotFoundException("Adoção não encontrada."));

        AdocaoDTO dto = DozerMapper.parseObject(entity, AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(id)).withSelfRel());
        return dto;
    }

    public AdocaoDTO create(AdocaoDTO adocao) {
        if (adocao == null)
            throw new AdocaoNulaException("Não há dados");

        Animal animal = animalRepository.findById(adocao.getIdAnimal())
                .orElseThrow(() -> new AdocaoNotFoundException("Animal não encontrado"));

        Usuario usuario = usuarioRepository.findById(adocao.getIdUsuario())
                .orElseThrow(() -> new AdocaoNotFoundException("Usuário não encontrado"));

        Adocao entity = DozerMapper.parseObject(adocao, Adocao.class);
        entity.setAnimal(animal);
        entity.setUsuario(usuario);
        AdocaoDTO dto = DozerMapper.parseObject(adocaoRepository.save(entity), AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public AdocaoDTO update(AdocaoDTO adocao, Long id) {

        if (adocao == null)
            throw new NullPointerException();

        Adocao entity = adocaoRepository.findById(id)
                .orElseThrow(() -> new AdocaoNotFoundException("Adoção não encontrada."));

        Usuario usuario = usuarioRepository.findById(adocao.getIdUsuario())
                .orElseThrow(() -> new AdocaoNotFoundException("Usuário não encontrada."));

        Animal animal = animalRepository.findById(adocao.getIdAnimal())
                .orElseThrow(() -> new AdocaoNotFoundException("Animal não encontrado"));

        Ong ong = ongRepository.findById(animal.getOng().getIdOng())
                .orElseThrow(() -> new AdocaoNotFoundException("Ong não encontrada."));

        entity.setDataAdocao(adocao.getDataAdocao());
        entity.setStatus(adocao.getStatus());
        entity.setUsuario(usuario);
        animal.setOng(ong);
        entity.setAnimal(animal);

        AdocaoDTO dto = DozerMapper.parseObject(adocaoRepository.save(entity), AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public AdocaoDTO partialUpdate(Long id, Map<String, Object> updates) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new AdocaoNotFoundException("Adoção não encontrada."));

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

        return dto;
    }

    public void delete(Long id) {
        if (!adocaoRepository.existsById(id)) {
            throw new AdocaoNotFoundException("Adoção não encontrada.");
        }
        adocaoRepository.deleteById(id);
    }
}