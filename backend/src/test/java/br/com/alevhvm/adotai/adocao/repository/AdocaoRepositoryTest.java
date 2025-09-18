package br.com.alevhvm.adotai.adocao.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;

@DataJpaTest
@ActiveProfiles("test")
public class AdocaoRepositoryTest {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private OngRepository ongRepository;

    private Adocao adocao;
    private Usuario usuario;
    private Animal animal;
    private Ong ong;

    private RedeVO redeVO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setNome("UserTeste");
        usuario.setNomeUsuario("UserTesteNome");
        usuario.setSenha("senha123");
        usuario.setCell("11999999999");
        usuario.setCpf("824.387.720-77");
        usuario.setRole(Roles.USER);
        usuarioRepository.save(usuario);

        redeVO = new RedeVO();
        redeVO.setInstagram("https://teste.adotai.com/");
        redeVO.setFacebook("https://teste.facebook.com/adotai");

        ong = new Ong();
        ong.setNome("Amigos dos Animais");
        ong.setNomeUsuario("amigosanimais");
        ong.setEmail("contato@amigosanimais.org");
        ong.setSenha("123456");
        ong.setFotoPerfil("foto_ong.png");
        ong.setCell("11988887777");
        ong.setRole(Roles.ONG);
        ong.setEndereco(new EnderecoVO(
                "Rua das Flores",
                "123",
                "Casa 2",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000"
        ));
        ong.setCnpj("12.345.678/0001-90");
        ong.setResponsavel("Maria Silva");
        ong.setDescricao("ONG dedicada ao resgate e adoção de animais abandonados.");
        ong.setRede(redeVO);
        ongRepository.save(ong);

        animal = new Animal();
        animal.setNome("Garfield");
        animal.setEspecie("Gato");
        animal.setRaca("Persa");
        animal.setDataNascimento(LocalDate.of(2021, 8, 3));
        animal.setPorte("Pequeno");
        animal.setSexo("Macho");
        animal.setStatus(StatusAnimal.INDISPONIVEL);
        animal.setOng(ong);
        animal.setFoto("foto.png");
        animalRepository.save(animal);

        adocao = new Adocao();
        adocao.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocao.setStatus(StatusAdocao.APROVADA);
        adocao.setUsuario(usuario);
        adocao.setAnimal(animal);
        adocaoRepository.save(adocao);

        pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "idAdocao");
    }

    @Test
    void deveEncontrarAdocoesPeloIdDoUsuario() {
        Long usuarioId = usuario.getIdUsuario();

        Page<Adocao> resultado = adocaoRepository.findAdocoesByUsuarioId(usuarioId, pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(LocalDate.parse("2024-07-11"), resultado.getContent().get(0).getDataAdocao());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarAdocaoPeloUsuarioId() {
        Page<Adocao> resultado = adocaoRepository.findAdocoesByUsuarioId(2L, pageable);

        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
    }

    @Test
    void deveEncontrarAdocoesPeloIdDaOng() {
        Long ongId = ong.getIdOng();

        Page<Adocao> resultado = adocaoRepository.findAdocoesByOngId(ongId, pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(StatusAdocao.APROVADA, resultado.getContent().get(0).getStatus());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarAdocaoPelaOngId() {
        Page<Adocao> resultado = adocaoRepository.findAdocoesByOngId(2L, pageable);

        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
    }

    @Test
    void deveEncontrarAdocoesPeloNomeUsuario() {
        Page<Adocao> resultado = adocaoRepository.findAdocoesByNomeUsuario("UserTesteNome", pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(LocalDate.parse("2024-07-11"), resultado.getContent().get(0).getDataAdocao());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarAdocaoPeloNomeUsuario() {
        Page<Adocao> resultado = adocaoRepository.findAdocoesByNomeUsuario("UserTesteNomeErrado", pageable);

        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
    }
}
