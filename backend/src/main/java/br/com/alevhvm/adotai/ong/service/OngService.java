package br.com.alevhvm.adotai.ong.service;

import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.ong.validations.OngValidacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.alevhvm.adotai.adocao.controller.AdocaoController;
import br.com.alevhvm.adotai.ong.controller.OngController;
import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.dto.OngFiltroDTO;
import br.com.alevhvm.adotai.ong.dto.OngUpdateDTO;
import br.com.alevhvm.adotai.ong.exception.OngNotFoundException;
import br.com.alevhvm.adotai.ong.exception.OngNulaException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.adocao.repository.AdocaoRepository;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OngService {

    private static final Logger logger = LoggerFactory.getLogger(OngService.class);

    private final OngRepository ongRepository;

    private final AdocaoRepository adocaoRepository;

    private final PasswordEncoder passwordEncoder;

    private final PagedResourcesAssembler<OngDTO> assembler;

    private final PagedResourcesAssembler<AdocaoDTO> adocaoDtoAssembler;

    private final Validator validator;

    private final AuthenticationManager authenticationManager;

    private final br.com.alevhvm.adotai.auth.service.TokenService tokenService;

    private final OngValidacao ongValidacao;

    private final CepService cepService;

    public PagedModel<EntityModel<OngDTO>> findAll(Pageable pageable) {
        logger.debug("Iniciando busca de todos as ongs");

        Page<Ong> ongPage = ongRepository.findAll(pageable);

        logger.info("Encontrada(s) {} página(s), com {} Ong(s)", ongPage.getTotalPages(), ongPage.getTotalElements());

        Page<OngDTO> ongDtosPage = ongPage.map(o -> DozerMapper.parseObject(o, OngDTO.class));
        ongDtosPage.map(o -> o.add(linkTo(methodOn(OngController.class).acharOngPorId(o.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(OngController.class).listarOngs(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(ongDtosPage, link);
    }

    public OngDTO findById(Long id) {
        logger.debug("Iniciando busca de ong com id = {}", id);

        Ong entity = ongRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Ong não encontrada para id = {}", id);
                    return new OngNotFoundException("Ong não encontrada.");
                });

        OngDTO dto = DozerMapper.parseObject(entity, OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(id)).withSelfRel());

        logger.info("Ong encontrada com sucesso: id={}, nomeUsuario={}", entity.getIdOng(), entity.getNomeUsuario());
        return dto;

    }

    public OngDTO findByNomeUsuario(String nomeUsuario) {
        logger.debug("Iniciando busca de ong com nomeUsuario = {}", nomeUsuario);

        Ong entity = getOngEntityByNomeUsuario(nomeUsuario);

        OngDTO dto = DozerMapper.parseObject(entity, OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorNomeUsuario(nomeUsuario)).withSelfRel());

        logger.info("Ong encontrada com sucesso: id={}, nomeUsuario={}", entity.getIdOng(), entity.getNomeUsuario());
        return dto;

    }

    public PagedModel<EntityModel<AdocaoDTO>> findAllAdocoesByOngId(Long idOng, Pageable pageable) {
        logger.debug("Iniciando busca de todos as Adocoes de uma Ong");

        Page<Adocao> adocaoPage = adocaoRepository.findAdocoesByOngId(idOng, pageable);

        logger.info("Encontrada(s) {} página(s), com {} Adocao(oes)", adocaoPage.getTotalPages(), adocaoPage.getTotalElements());

        Page<AdocaoDTO> adocaoDtoPage = adocaoPage.map(a -> DozerMapper.parseObject(a, AdocaoDTO.class));

        adocaoDtoPage = adocaoDtoPage.map(
                dto -> dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel()));

        Link selfLink = linkTo(methodOn(OngController.class)
                .listarAdocoesPorOngId(idOng, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return adocaoDtoAssembler.toModel(adocaoDtoPage, selfLink);
    }

    public Page<OngDTO> filtrarOngs(OngFiltroDTO filtro, Pageable pageable) {
        logger.debug("Iniciando filtro de Ongs");
        Page<Ong> ongs = ongRepository.filtrarOngsNativo(filtro, pageable);
        logger.debug("Filtro finalizado de animais");
        return ongs.map(ong -> DozerMapper.parseObject(ong, OngDTO.class));
    }

    public OngDTO create(OngDTO ong) {
        logger.debug("Iniciando a criacao de uma ong");

        // Passo 3: Preenche o endereço com base no CEP usando a API ViaCEP
        cepService.preencherEndereco(ong.getEndereco());
        ongValidacao.validarEnderecoPreenchido(ong.getEndereco());

        ongValidacao.validate(ong);

        ong.setSenha(passwordEncoder.encode(ong.getSenha()));
        Ong entity = DozerMapper.parseObject(ong, Ong.class);
        entity.setCnpj(ong.getCnpj().getCnpj());
        entity.setEmail(ong.getEmail().toLowerCase());
        entity.setRole(Roles.ONG);

        OngDTO dto = DozerMapper.parseObject(ongRepository.save(entity), OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(dto.getKey())).withSelfRel());

        logger.info("Ong {} criada com sucesso.", entity.getNome());
        return dto;
    }

    public TokenDTO logar(LoginDTO data) {
        logger.debug("Inciando login da Ong {}", data.identifier());

        String identifier = data.identifier();

        // Se for um e-mail (tem '@'), transforma para lowercase
        if (identifier.contains("@")) {
            identifier = identifier.toLowerCase();
        }

        // credenciais do spring security
        var usernamePassword = new UsernamePasswordAuthenticationToken(identifier, data.password());

        // autentica de forma milagrosa as credenciais
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((LoginIdentityView) auth.getPrincipal());

        logger.info("Ong {} logada com sucesso.", data.identifier());
        return new TokenDTO(token);
    }

    public OngDTO update(OngUpdateDTO ongUpdate, String nomeUsuario) {
        logger.debug("Iniciando atualização de ong com nomeUsuario = {}", nomeUsuario);

        if (ongUpdate == null) {
            logger.warn("Nao ha dados");
            throw new OngNulaException("Não há dados");
        }

        Ong entity = getOngEntityByNomeUsuario(nomeUsuario);

        cepService.preencherEndereco(ongUpdate.getEndereco());
        
        entity.setNome(ongUpdate.getNome());
        entity.setFotoPerfil(ongUpdate.getFotoPerfil());
        entity.setEmail(ongUpdate.getEmail().toLowerCase());
        entity.setSenha(passwordEncoder.encode(ongUpdate.getSenha()));
        entity.setEndereco(ongUpdate.getEndereco());
        entity.setCell(ongUpdate.getCell());
        entity.setResponsavel(ongUpdate.getResponsavel());
        entity.setDescricao(ongUpdate.getDescricao());
        entity.setRede(ongUpdate.getRede());

        ongValidacao.validarEnderecoPreenchido(ongUpdate.getEndereco());
        ongValidacao.validateUpdate(entity);

        OngDTO dto = DozerMapper.parseObject(ongRepository.save(entity), OngDTO.class);

        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(dto.getKey())).withSelfRel());

        logger.info("Ong {} atualizada com sucesso.", entity.getNomeUsuario());
        return dto;
    }

    public OngDTO partialUpdate(String nomeUsuario, Map<String, Object> updates) {
        logger.debug("Iniciando atualização parcial de animal com nomeUsuario = {}", nomeUsuario);

        Ong ong = getOngEntityByNomeUsuario(nomeUsuario);

        ongValidacao.validatePartialUpdate(nomeUsuario, updates);

        ObjectMapper mapper = new ObjectMapper();
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Ong.class, campo);
            if (field != null) {
                field.setAccessible(true);

                if (campo.equalsIgnoreCase("email") && valor instanceof String) {
                    valor = ((String) valor).toLowerCase();
                }
                if (campo.equals("endereco") && valor instanceof Map<?, ?> valorMapEndereco) {
                    EnderecoVO enderecoOriginal = ong.getEndereco();
                    EnderecoVO enderecoAtualizado = mapper.convertValue(valor, EnderecoVO.class);

                    if (enderecoOriginal == null) {
                        ong.setEndereco(enderecoAtualizado);
                    } else {
                        // merge campo a campo
                        if (enderecoAtualizado.getLogradouro() != null)
                            enderecoOriginal.setLogradouro(enderecoAtualizado.getLogradouro());
                        if (enderecoAtualizado.getNumero() != null)
                            enderecoOriginal.setNumero(enderecoAtualizado.getNumero());
                        if (enderecoAtualizado.getComplemento() != null)
                            enderecoOriginal.setComplemento(enderecoAtualizado.getComplemento());
                        if (enderecoAtualizado.getBairro() != null)
                            enderecoOriginal.setBairro(enderecoAtualizado.getBairro());
                        if (enderecoAtualizado.getCidade() != null)
                            enderecoOriginal.setCidade(enderecoAtualizado.getCidade());
                        if (enderecoAtualizado.getEstado() != null)
                            enderecoOriginal.setEstado(enderecoAtualizado.getEstado());
                        if (enderecoAtualizado.getCep() != null)
                            enderecoOriginal.setCep(enderecoAtualizado.getCep());
                    }

                } else if (campo.equals("rede") && valor instanceof Map<?, ?> valorMapRede) {
                    RedeVO redeOriginal = ong.getRede();
                    RedeVO redeAtualizado = mapper.convertValue(valor, RedeVO.class);

                    if (redeOriginal == null) {
                        ong.setRede(redeAtualizado);
                    } else {
                        if (redeAtualizado.getSite() != null)
                            redeOriginal.setSite(redeAtualizado.getSite());
                        if (redeAtualizado.getInstagram() != null)
                            redeOriginal.setInstagram(redeAtualizado.getInstagram());
                        if (redeAtualizado.getFacebook() != null)
                            redeOriginal.setFacebook(redeAtualizado.getFacebook());
                        if (redeAtualizado.getTiktok() != null)
                            redeOriginal.setTiktok(redeAtualizado.getTiktok());
                        if (redeAtualizado.getYoutube() != null)
                            redeOriginal.setYoutube(redeAtualizado.getYoutube());
                        if (redeAtualizado.getWhatsapp() != null)
                            redeOriginal.setWhatsapp(redeAtualizado.getWhatsapp());
                        if (redeAtualizado.getX() != null)
                            redeOriginal.setX(redeAtualizado.getX());
                        if (redeAtualizado.getLinkedin() != null)
                            redeOriginal.setLinkedin(redeAtualizado.getLinkedin());
                    }
                } else {
                    ReflectionUtils.setField(field, ong, mapper.convertValue(valor, field.getType()));
                }

            }
        });

        OngDTO ongDTO = DozerMapper.parseObject(ong, OngDTO.class);

        Set<ConstraintViolation<OngDTO>> violations = validator.validate(ongDTO);

        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<OngDTO> violation : violations) {
                errors.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Erro de validação: " + errors.toString(), violations);
        }

        ongRepository.save(ong);

        OngDTO dto = DozerMapper.parseObject(ong, OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorNomeUsuario(nomeUsuario)).withSelfRel());

        logger.info("Ong {} atualziada parcialmente com sucesso.", ong.getNome());
        return dto;
    }

    @Transactional
    public void delete(String nomeUsuario) {
        logger.debug("Iniciando delecao de ong de nome = {}", nomeUsuario);
        var ong = getOngEntityByNomeUsuario(nomeUsuario);
        ongRepository.deleteByNomeUsuario(nomeUsuario);
        logger.info("Ong {} deletada com sucesso.", nomeUsuario);
    }

    public Ong getOngEntityByNomeUsuario(String nomeUsuario) {
        return ongRepository.findByNomeUsuario(nomeUsuario)
            .orElseThrow(() -> {
                logger.warn("Ong nao encontrda para nomeUsuario = {}", nomeUsuario);
                return new OngNotFoundException("Ong não encontrada.");
            });
    }
}