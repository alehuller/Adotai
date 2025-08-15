package br.com.alevhvm.adotai.adocao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.adocao.repository.AdocaoRepository;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class AdocaoServiceTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AdocaoService adocaoService;

    private AdocaoDTO adocaoDTO;
    private Adocao adocaoEntity;
    private Usuario usuario;
    private Ong ong;
    private Animal animal;


    private RedeVO redeVO;


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

        adocaoDTO = new AdocaoDTO();
        adocaoDTO.setKey(1L);
        adocaoDTO.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoDTO.setStatus(StatusAdocao.APROVADA);
        adocaoDTO.setIdAnimal(1L);
        adocaoDTO.setIdUsuario(1L);

        adocaoEntity = new Adocao();
        adocaoEntity.setIdAdocao(1L);
        adocaoEntity.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoEntity.setStatus(StatusAdocao.APROVADA);
        adocaoEntity.setAnimal(animal);
        adocaoEntity.setUsuario(usuario);
    }

    @Test
    void deveCriarAdocaoComSucesso() {
        when(animalRepository.findById(adocaoDTO.getIdAnimal())).thenReturn(Optional.of(animal));
        when(usuarioRepository.findById(adocaoDTO.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(adocaoRepository.save(any(Adocao.class))).thenReturn(adocaoEntity);

        AdocaoDTO resultado = adocaoService.create(adocaoDTO);

        assertNotNull(resultado);
        assertEquals(LocalDate.parse("2024-07-11"), resultado.getDataAdocao());
        assertEquals("Garfield", resultado.getNomeAnimal());
        verify(adocaoRepository).save(any(Adocao.class));
    }
}
