package br.com.alevh.sistema_adocao_pets.usuario.service;

import br.com.alevh.sistema_adocao_pets.adocao.controller.AdocaoController;
import br.com.alevh.sistema_adocao_pets.animal.controller.AnimalController;
import br.com.alevh.sistema_adocao_pets.usuario.controller.UsuarioController;
import br.com.alevh.sistema_adocao_pets.auth.dto.LoginDTO;
import br.com.alevh.sistema_adocao_pets.auth.dto.TokenDTO;
import br.com.alevh.sistema_adocao_pets.auth.dto.RegistroDTO;
import br.com.alevh.sistema_adocao_pets.adocao.dto.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.animal.dto.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.usuario.dto.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.usuario.dto.UsuarioUpdateDTO;
import br.com.alevh.sistema_adocao_pets.common.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.common.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.adocao.model.Adocao;
import br.com.alevh.sistema_adocao_pets.animal.model.Animal;
import br.com.alevh.sistema_adocao_pets.auth.model.LoginIdentityView;
import br.com.alevh.sistema_adocao_pets.usuario.model.Usuario;
import br.com.alevh.sistema_adocao_pets.adocao.repository.AdocaoRepository;
import br.com.alevh.sistema_adocao_pets.animal.repository.AnimalRepository;
import br.com.alevh.sistema_adocao_pets.usuario.repository.UsuarioRepository;
import br.com.alevh.sistema_adocao_pets.auth.enums.Roles;
import br.com.alevh.sistema_adocao_pets.service.auth.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import br.com.alevh.sistema_adocao_pets.usuario.validations.UsuarioValidacao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final AdocaoRepository adocaoRepository;

    private final AnimalRepository animalRepository;

    private final PasswordEncoder passwordEncoder;

    private final PagedResourcesAssembler<UsuarioDTO> assembler;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final PagedResourcesAssembler<AdocaoDTO> adocaoDtoAssembler;

    private final PagedResourcesAssembler<AnimalDTO> animalDtoAssembler;

    private final Validator validator;

    private final UsuarioValidacao usuarioValidacao;

    public PagedModel<EntityModel<UsuarioDTO>> findAll(Pageable pageable) {

        Page<Usuario> usuarioPage = usuarioRepository.findAll(pageable);

        Page<UsuarioDTO> usuarioDtosPage = usuarioPage.map(u -> DozerMapper.parseObject(u, UsuarioDTO.class));
        usuarioDtosPage
                .map(u -> u.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorId(u.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(UsuarioController.class).listarUsuarios(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(usuarioDtosPage, link);
    }

    public UsuarioDTO findByNomeUsuario(String nomeUsuario) {

        Usuario entity = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        UsuarioDTO dto = DozerMapper.parseObject(entity, UsuarioDTO.class);
        dto.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorNomeUsuario(nomeUsuario)).withSelfRel());
        return dto;
    }

    public UsuarioDTO findById(Long id) {

        Usuario entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        UsuarioDTO dto = DozerMapper.parseObject(entity, UsuarioDTO.class);
        dto.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorId(id)).withSelfRel());
        return dto;
    }

    public PagedModel<EntityModel<AdocaoDTO>> findAllAdocoesByNomeUsuario(String nomeUsuario, Pageable pageable) {

        Page<Adocao> adocaoPage = adocaoRepository.findAdocoesByNomeUsuario(nomeUsuario, pageable);

        Page<AdocaoDTO> adocaoDtoPage = adocaoPage.map(a -> DozerMapper.parseObject(a, AdocaoDTO.class));

        adocaoDtoPage = adocaoDtoPage.map(
                dto -> dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel()));

        Link selfLink = linkTo(methodOn(UsuarioController.class)
                .listarAdocoesPorNomeUsuario(nomeUsuario, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return adocaoDtoAssembler.toModel(adocaoDtoPage, selfLink);
    }

    public UsuarioDTO create(RegistroDTO registroDTO) {

        usuarioValidacao.validate(registroDTO);

        Usuario entity = DozerMapper.parseObject(registroDTO, Usuario.class);
        entity.setCpf(registroDTO.getCpf().getCpf());
        entity.setSenha(passwordEncoder.encode(registroDTO.getPassword()));
        entity.setEmail(registroDTO.getEmail().toLowerCase());
        entity.setRole(Roles.USER);
        UsuarioDTO dto = DozerMapper.parseObject(usuarioRepository.save(entity), UsuarioDTO.class);
        dto.add(
                linkTo(
                        methodOn(UsuarioController.class).acharUsuarioPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public TokenDTO logar(LoginDTO data) {

        String identifier = data.identifier();

        // Se for um e-mail a string terá '@', isso será identificado e os dígitos da
        // string serão convertidos para lowercase
        if (identifier.contains("@")) {
            identifier = identifier.toLowerCase();
        }

        // Credenciais do spring security
        var usernamePassword = new UsernamePasswordAuthenticationToken(identifier, data.password());

        // Autenticação das credenciais
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((LoginIdentityView) auth.getPrincipal());

        return new TokenDTO(token);
    }

    public UsuarioDTO update(UsuarioUpdateDTO usuarioUpdate, String nomeUsuario) {

        Usuario entity = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        entity.setNome(usuarioUpdate.getNome());
        entity.setNomeUsuario(usuarioUpdate.getNomeUsuario());
        entity.setFotoPerfil(usuarioUpdate.getFotoPerfil());
        entity.setEmail(usuarioUpdate.getEmail().toLowerCase());
        entity.setSenha(passwordEncoder.encode(usuarioUpdate.getSenha()));
        entity.setCell(usuarioUpdate.getCell());

        usuarioValidacao.validateUpdate(entity);

        UsuarioDTO dto = DozerMapper.parseObject(usuarioRepository.save(entity), UsuarioDTO.class);
        dto.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public UsuarioDTO partialUpdate(String nomeUsuario, Map<String, Object> updates) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        usuarioValidacao.validatePartialUpdate(nomeUsuario, updates);

        ObjectMapper mapper = new ObjectMapper();
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Usuario.class, campo);
            if (field != null) {
                field.setAccessible(true);

                if (campo.equalsIgnoreCase("email") && valor instanceof String) {
                    valor = ((String) valor).toLowerCase();
                }

                ReflectionUtils.setField(field, usuario, mapper.convertValue(valor, field.getType()));
            }
        });

        UsuarioDTO usuarioDTO = DozerMapper.parseObject(usuario, UsuarioDTO.class);

        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(usuarioDTO);

        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<UsuarioDTO> violation : violations) {
                errors.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Erro de validação: " + errors.toString(), violations);
        }

        usuarioRepository.save(usuario);
        return DozerMapper.parseObject(usuario, UsuarioDTO.class);
    }

    @Transactional
    public void delete(String nomeUsuario) {
        usuarioRepository.deleteByNomeUsuario(nomeUsuario);
    }

    public PagedModel<EntityModel<AnimalDTO>> findAnimaisFavoritosByNomeUsuario(String nomeUsuario, Pageable pageable) {
        Page<Animal> animalPage = usuarioRepository.findAnimaisFavoritosByNomeUsuario(nomeUsuario, pageable);
        Page<AnimalDTO> animalDtoPage = animalPage.map(a -> DozerMapper.parseObject(a, AnimalDTO.class));

        animalDtoPage = animalDtoPage.map(dto -> {
            dto.add(linkTo(methodOn(AnimalController.class).acharAnimalPorId(dto.getKey())).withSelfRel());
            return dto;
        });

        Link selfLink = linkTo(methodOn(UsuarioController.class)
                .listarAnimaisFavoritos(
                        nomeUsuario,
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        "asc",
                        "nome"))
                .withSelfRel();

        return animalDtoAssembler.toModel(animalDtoPage, selfLink);
    }

    public boolean isAnimalFavorito(String nomeUsuario, Long animalId) {
        return usuarioRepository.existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal(nomeUsuario, animalId);
    }

    @Transactional
    @CacheEvict(value = "favoritos", key = "#nomeUsuario")
    public boolean toggleAnimalFavorito(String nomeUsuario, Long animalId) {
        boolean isFavorito = usuarioRepository.existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal(nomeUsuario, animalId);

        if (isFavorito) {
            usuarioRepository.removerFavoritoNativo(nomeUsuario, animalId);
        } else {
            usuarioRepository.adicionarFavoritoNativo(nomeUsuario, animalId);
        }

        return !isFavorito;
    }

    // Métodos auxiliares mantidos porém não chamados diretamente
    @Transactional
    public void adicionarAnimalFavorito(String nomeUsuario, Long animalId) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));

        usuario.getAnimaisFavoritos().add(animal);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void removerAnimalFavorito(String nomeUsuario, Long animalId) {
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        usuario.getAnimaisFavoritos().removeIf(animal -> animal.getIdAnimal().equals(animalId));
        usuarioRepository.save(usuario);
    }
}
