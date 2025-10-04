package br.com.alevhvm.adotai.avaliacao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alevhvm.adotai.avaliacao.controller.AvaliacaoController;
import br.com.alevhvm.adotai.avaliacao.dto.AvaliacaoDTO;
import br.com.alevhvm.adotai.avaliacao.model.Avaliacao;
import br.com.alevhvm.adotai.avaliacao.repository.AvaliacaoRepository;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.ong.exception.OngNotFoundException;
import br.com.alevhvm.adotai.avaliacao.exception.AvaliacaoNotFoundException;
import br.com.alevhvm.adotai.usuario.exception.UsuarioNotFoundException;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import br.com.alevhvm.adotai.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    private final OngRepository ongRepository;

    private final AvaliacaoRepository avaliacaoRepository;
    
    @Transactional(readOnly = true)
    public AvaliacaoDTO findById(Long id) {
        logger.debug("Iniciando busca de avaliacao com id = {}", id);

        Avaliacao entity = avaliacaoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Avaliacao nao encontrada para id = {}", id);
                    return new AvaliacaoNotFoundException("Avaliacao nao encontrada");
                });

        AvaliacaoDTO dto = DozerMapper.parseObject(entity, AvaliacaoDTO.class);
        dto.add(linkTo(methodOn(AvaliacaoController.class).acharAvaliacaoPorId(id)).withSelfRel());

        logger.info("Avaliacao encontrada com sucesso: id = {}", entity.getId());
        return dto;
    }

    @Transactional
    public AvaliacaoDTO create(AvaliacaoDTO avaliacaoDTO) {
        logger.debug("Iniciando o registro de uma Avaliacao");

        Usuario usuario = usuarioRepository.findById(avaliacaoDTO.getIdUsuario())
            .orElseThrow(() -> {
                logger.warn("Usuario nao encontrado");
                return new UsuarioNotFoundException("Usuario nao encontrado");
            });

        Ong ong = ongRepository.findById(avaliacaoDTO.getIdOng())
            .orElseThrow(() -> {
                logger.warn("Ong nao encontrada");
                return new OngNotFoundException("Ong nao encontrada");
            });

        Avaliacao entity = DozerMapper.parseObject(avaliacaoDTO, Avaliacao.class);
        entity.setUsuario(usuario);
        entity.setOng(ong);
        entity.setNota(avaliacaoDTO.getNota());
        entity.setComentario(avaliacaoDTO.getComentario());
        entity.setDataCriacao(LocalDateTime.now());
        AvaliacaoDTO dto = DozerMapper.parseObject(avaliacaoRepository.save(entity), AvaliacaoDTO.class);
        dto.add(
                linkTo(
                        methodOn(AvaliacaoController.class).acharAvaliacaoPorId(dto.getKey())).withSelfRel());

        logger.info("Avaliacao criada com sucesso.");
        return dto;
    }
}
