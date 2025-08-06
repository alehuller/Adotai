package br.com.alevhvm.adotai.ong.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;

@DataJpaTest
@ActiveProfiles("test")
class OngRepositoryTest {

    @Autowired
    private OngRepository ongRepository;

    private Ong ong;

    private EnderecoVO enderecoVO;
    private RedeVO redeVO;

    @BeforeEach
    void setUp() {
        enderecoVO = new EnderecoVO();
        enderecoVO.setLogradouro("Rua Teste");
        enderecoVO.setNumero("100");
        enderecoVO.setComplemento("Complemente Teste");
        enderecoVO.setBairro("Bairro Teste");
        enderecoVO.setCidade("Cidade Teste");

        redeVO = new RedeVO();
        redeVO.setSite("https://siteteste.com");
        redeVO.setInstagram("https://instagramteste.com");
        redeVO.setFacebook("https://facebookteste.com");
        redeVO.setTiktok("https://tiktokteste.com");
        redeVO.setYoutube("https://youtubeteste.com");
        redeVO.setWhatsapp("https://whatsappteste.com");
        redeVO.setX("https://xteste.com");
        redeVO.setLinkedin("https://linkedinteste.com");

        ong = new Ong();
        ong.setEmail("ongteste@email.com");
        ong.setNome("OngTeste");
        ong.setNomeUsuario("OngTesteNome");
        ong.setSenha("senha123");
        ong.setCell("11222222222");
        ong.setRole(Roles.ONG);
        ong.setEndereco(enderecoVO);
        ong.setCnpj("33.975.872/0001-24");
        ong.setResponsavel("Responsável Teste");
        ong.setDescricao("Descrição Teste");
        ong.setRede(redeVO);

        ongRepository.save(ong);
    }

    @Test
    void deveEncontrarOngComOsMesmosCamposSalvos() {
        Optional<Ong> resultado = ongRepository.findByEmail("ongteste@email.com");

        assertTrue(resultado.isPresent());
        Ong ongSalva = resultado.get();

        assertEquals("ongteste@email.com", ongSalva.getEmail());
        assertEquals("OngTeste", ongSalva.getNome());
        assertEquals("OngTesteNome", ongSalva.getNomeUsuario());
        assertEquals("senha123", ongSalva.getSenha());
        assertEquals("11222222222", ongSalva.getCell());
        assertEquals(Roles.ONG, ongSalva.getRole());
        assertEquals(enderecoVO, ongSalva.getEndereco());
        assertEquals("33.975.872/0001-24", ongSalva.getCnpj());
        assertEquals("Responsável Teste", ongSalva.getResponsavel());
        assertEquals("Descrição Teste", ongSalva.getDescricao());
        assertEquals(redeVO, ongSalva.getRede());
    }

    @Test
    void deveEncontrarOngPorEmailQuandoHaEmailCadastrado() {
        Optional<Ong> resultado = ongRepository.findByEmail("ongteste@email.com");

        assertTrue(resultado.isPresent());
        assertEquals("OngTesteNome", resultado.get().getNomeUsuario());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPorEmail() {
        assertThrows(NoSuchElementException.class, () -> {
            ongRepository.findByEmail("ongtesteerrado@email.com")
                .orElseThrow();
        });
    }

    @Test
    void deveEncontrarOngPorNomeUsuarioQuandoHaCadastrado() {
        Optional<Ong> resultado = ongRepository.findByNomeUsuario("OngTesteNome");

        assertTrue(resultado.isPresent());
        assertEquals("ongteste@email.com", resultado.get().getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPorNomeUsuario() {
        assertThrows(NoSuchElementException.class, () -> {
            ongRepository.findByNomeUsuario("OngTesteNomeErrado")
                .orElseThrow();
        });
    }

    @Test
    void deveEncontrarOngPorCellQuandoHaCadastrado() {
        Optional<Ong> resultado = ongRepository.findByCell("11222222222");

        assertTrue(resultado.isPresent());
        assertEquals("OngTeste", resultado.get().getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPorCell() {
        assertThrows(NoSuchElementException.class, () -> {
            ongRepository.findByCell("11222222221")
                .orElseThrow();
        });
    }

    @Test
    void deveEncontrarOngPorCnojQuandoHaCadastrado() {
        Optional<Ong> resultado = ongRepository.findByCnpj("33.975.872/0001-24");

        assertTrue(resultado.isPresent());
        assertEquals("ongteste@email.com", resultado.get().getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPorCnpj() {
        assertThrows(NoSuchElementException.class, () -> {
            ongRepository.findByCnpj("33.975.872/0001-22")
                .orElseThrow();
        });
    }

    @Test
    void deveExcluirOPngPorNomeUsuario() {
        ongRepository.deleteByNomeUsuario("OngTesteNome");

        Optional<Ong> resultado = ongRepository.findByNomeUsuario("OngTesteNome");

        assertTrue(resultado.isEmpty());
    }
}
