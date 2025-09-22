package br.com.alevhvm.adotai.administrador.service;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import br.com.alevhvm.adotai.administrador.controller.AdministradorController;
import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.dto.AdministradorUpdateDTO;
import br.com.alevhvm.adotai.administrador.exception.AdmIsMasterException;
import br.com.alevhvm.adotai.administrador.exception.AdministradorNotFoundException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.administrador.repository.AdministradorRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.auth.service.TokenService;
import br.com.alevhvm.adotai.administrador.validations.AdministradorValidacao;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorValidacao administradorValidacao;

    private final AdministradorRepository administradorRepository;

    private final PasswordEncoder passwordEncoder;

    private final PagedResourcesAssembler<AdministradorDTO> assembler;

    private final Validator validator;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public PagedModel<EntityModel<AdministradorDTO>> findAll(Pageable pageable) {

        Page<Administrador> administradorPage = administradorRepository.findAll(pageable);

        Page<AdministradorDTO> administradorDtosPage = administradorPage
                .map(a -> DozerMapper.parseObject(a, AdministradorDTO.class));
        administradorDtosPage
                .map(u -> u.add(linkTo(methodOn(AdministradorController.class).acharAdministradorPorId(u.getKey()))
                        .withSelfRel()));

        Link link = linkTo(methodOn(AdministradorController.class).listarAdministradores(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(administradorDtosPage, link);
    }

    public AdministradorDTO create(AdministradorDTO administradorDTO) {

        administradorValidacao.validate(administradorDTO);

        Administrador entity = DozerMapper.parseObject(administradorDTO, Administrador.class);
        entity.setRole(Roles.ADMIN);
        entity.setSenha(passwordEncoder.encode(administradorDTO.getSenha()));
        entity.setEmail(administradorDTO.getEmail().toLowerCase());

        AdministradorDTO dto = DozerMapper.parseObject(administradorRepository.save(entity), AdministradorDTO.class);
        dto.add(
                linkTo(
                        methodOn(AdministradorController.class).acharAdministradorPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public AdministradorDTO findById(Long id) {
        Administrador entity = administradorRepository.findById(id)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador não encontrado."));

        AdministradorDTO dto = DozerMapper.parseObject(entity, AdministradorDTO.class);
        dto.add(linkTo(methodOn(AdministradorController.class).acharAdministradorPorId(id)).withSelfRel());
        return dto;
    }

    public AdministradorDTO update(AdministradorUpdateDTO administradorUpdateDTO, String nomeUsuario) {

        Administrador entity = administradorRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador não encontrado."));

        entity.setNome(administradorUpdateDTO.getNome());
        entity.setFotoPerfil(administradorUpdateDTO.getFotoPerfil());
        entity.setEmail(administradorUpdateDTO.getEmail().toLowerCase());
        entity.setSenha(passwordEncoder.encode(administradorUpdateDTO.getSenha()));
        entity.setCell(administradorUpdateDTO.getCell());

        administradorValidacao.validateUpdate(entity);

        AdministradorDTO dto = DozerMapper.parseObject(administradorRepository.save(entity), AdministradorDTO.class);
        dto.add(linkTo(methodOn(AdministradorController.class).acharAdministradorPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public AdministradorDTO findByNomeUsuario(String nomeUsuario) {

        Administrador entity = administradorRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new AdministradorNotFoundException("Usuário não encontrado."));

        AdministradorDTO dto = DozerMapper.parseObject(entity, AdministradorDTO.class);
        dto.add(linkTo(methodOn(AdministradorController.class).acharAdministradorPorNomeUsuario(nomeUsuario))
                .withSelfRel());
        return dto;
    }

    public AdministradorDTO partialUpdate(String nomeUsuario, Map<String, Object> updates) {
        Administrador administrador = administradorRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new AdministradorNotFoundException("Usuário não encontrado."));

        administradorValidacao.validatePartialUpdate(nomeUsuario, updates);

        ObjectMapper mapper = new ObjectMapper();
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Administrador.class, campo);
            if (field != null) {
                field.setAccessible(true);

                if (campo.equalsIgnoreCase("email") && valor instanceof String) {
                    valor = ((String) valor).toLowerCase();
                }

                ReflectionUtils.setField(field, administrador, mapper.convertValue(valor, field.getType()));
            }
        });

        AdministradorDTO administradorDTO = DozerMapper.parseObject(administrador, AdministradorDTO.class);

        Set<ConstraintViolation<AdministradorDTO>> violations = validator.validate(administradorDTO);

        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<AdministradorDTO> violation : violations) {
                errors.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Erro de validação: " + errors.toString(), violations);
        }

        administradorRepository.save(administrador);
        
        AdministradorDTO dto = DozerMapper.parseObject(administrador, AdministradorDTO.class);
        dto.add(linkTo(methodOn(AdministradorController.class).acharAdministradorPorNomeUsuario(nomeUsuario)).withSelfRel());

        return dto;
    }

    public TokenDTO logar(LoginDTO data) {

        String identifier = data.identifier();

        if (identifier.contains("@")) {
            identifier = identifier.toLowerCase();
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(identifier, data.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((LoginIdentityView) auth.getPrincipal());

        return new TokenDTO(token);
    }

    @Transactional
    public void delete(String nomeUsuario) {
        var admin = administradorRepository.findByNomeUsuario(nomeUsuario)
            .orElseThrow(() -> new AdministradorNotFoundException("Administrador não encontrado"));
        administradorRepository.delete(admin);
    }

    public AdministradorDTO atualizarAdmNormalParaMaster(String nomeUsuario) {
        Administrador entity = administradorRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador não encontrado."));

        if (entity.getRole() == Roles.ADMINMASTER) {
            throw new AdmIsMasterException("Esse administrador já é ADMINMASTER.");
        }

        entity.setRole(Roles.ADMINMASTER);

        AdministradorDTO dto = DozerMapper.parseObject(administradorRepository.save(entity), AdministradorDTO.class);
        dto.add(linkTo(methodOn(AdministradorController.class).acharAdministradorPorId(dto.getKey())).withSelfRel());
        return dto;
    }
}
