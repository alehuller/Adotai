package br.com.alevhvm.adotai.usuario.repository;

import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    OngRepository ongRepository;

    private final Usuario usuario = new Usuario();
    private final Animal cachorro = new Animal();
    private final Animal gato = new Animal();
    private final Ong ong = new Ong();
    private final RedeVO redeVO = new RedeVO();
    private Long idCachorro;
    private Long idGato;


    @BeforeEach
    void setUp() {
        usuario.setEmail("teste@email.com");
        usuario.setNome("UserTeste");
        usuario.setNomeUsuario("UserTesteNome");
        usuario.setSenha("senha123");
        usuario.setCell("11999999999");
        usuario.setCpf("824.387.720-77");
        usuario.setRole(Roles.USER);
        usuarioRepository.save(usuario);

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
        redeVO.setInstagram("https://teste.adotai.com/");
        redeVO.setFacebook("https://teste.facebook.com/adotai");
        ong.setRede(redeVO);
        ongRepository.save(ong);

        cachorro.setNome("Mel");
        cachorro.setEspecie("Cachorro");
        cachorro.setRaca("Labrador");
        cachorro.setDataNascimento(LocalDate.of(2020, 5, 15));
        cachorro.setPorte("Grande");
        cachorro.setSexo("Fêmea");
        cachorro.setStatus(StatusAnimal.DISPONIVEL);
        cachorro.setOng(ong);
        idCachorro = animalRepository.save(cachorro).getIdAnimal();

        gato.setNome("Garfield");
        gato.setEspecie("Gato");
        gato.setRaca("Persa");
        gato.setDataNascimento(LocalDate.of(2021, 8, 3));
        gato.setPorte("Pequeno");
        gato.setSexo("Macho");
        gato.setStatus(StatusAnimal.INDISPONIVEL);
        gato.setOng(ong);
        idGato = animalRepository.save(gato).getIdAnimal();
    }

    @Test
    void deveEncontrarUsuarioComOsMesmosCamposSalvos() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("teste@email.com");

        assertTrue(resultado.isPresent());
        Usuario userSalvo = resultado.get();

        assertEquals(usuario.getEmail(), userSalvo.getEmail());
        assertEquals(usuario.getNome(), userSalvo.getNome());
        assertEquals(usuario.getNomeUsuario(), userSalvo.getNomeUsuario());
        assertEquals(usuario.getSenha(), userSalvo.getSenha());
        assertEquals(usuario.getCell(), userSalvo.getCell());
        assertEquals(usuario.getCpf(), userSalvo.getCpf());
        assertEquals(Roles.USER, userSalvo.getRole());
    }

    @Test
    void deveEncontrarUsuarioPorEmail() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("teste@email.com");

        assertTrue(resultado.isPresent());
        assertEquals("UserTesteNome", resultado.get().getNomeUsuario());
    }

    @Test
    void deveEncontrarUsuarioPorNomeUsuario() {
        Optional<Usuario> resultado = usuarioRepository.findByNomeUsuario("UserTesteNome");

        assertTrue(resultado.isPresent());
        assertEquals("teste@email.com", resultado.get().getEmail());
    }

    @Test
    void deveEncontrarUsuarioPorCell() {
        Optional<Usuario> resultado = usuarioRepository.findByCell("11999999999");

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getCell(), resultado.get().getCell());
    }

    @Test
    void deveEncontrarUsuarioPorCpf() {
        Optional<Usuario> resultado = usuarioRepository.findByCpf("824.387.720-77");

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getCpf(), resultado.get().getCpf());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorEmail() {
        assertThrows(NoSuchElementException.class, () -> usuarioRepository.findByEmail("emailerrado@email.com")
                .orElseThrow());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorNomeUsuario() {
        assertThrows(NoSuchElementException.class, () -> usuarioRepository.findByNomeUsuario("NomeUsuarioErrado")
                .orElseThrow());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorCell() {
        assertThrows(NoSuchElementException.class, () -> usuarioRepository.findByCell("11222222222")
                .orElseThrow());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorCpf() {
        assertThrows(NoSuchElementException.class, () -> usuarioRepository.findByCell("11222222222")
                .orElseThrow());
    }

    @Test
    void deveExcluirUsuarioPorNomeUsuario() {
        usuarioRepository.deleteByNomeUsuario(usuario.getNomeUsuario());

        Optional<Usuario> resultado = usuarioRepository.findByNomeUsuario("AdminTesteNome");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAdicionarAnimalAosFavoritos(){
        usuarioRepository.adicionarAnimalAosFavoritos("UserTesteNome", idCachorro);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> resultado = usuarioRepository.findAnimaisFavoritosByNomeUsuario("UserTesteNome", pageable);

        boolean contemMel = resultado
                .getContent()
                .stream()
                .anyMatch(a -> "Mel".equals(a.getNome()));

        assertTrue(contemMel);
    }

    @Test
    void deveRemoverAnimalDosFavoritos(){
        usuarioRepository.adicionarAnimalAosFavoritos("UserTesteNome", idGato);
        usuarioRepository.removerAnimalDosFavoritos("UserTesteNome", idGato);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> resultado = usuarioRepository.findAnimaisFavoritosByNomeUsuario("UserTesteNome", pageable);

        boolean contemGarfield = resultado
                .getContent()
                .stream()
                .anyMatch(a -> "Mel".equals(a.getNome()));

        assertFalse(contemGarfield);
    }

    @Test
    void deveEncontrarAnimaisFavoritosPorNomeUsuario() {
        usuarioRepository.adicionarAnimalAosFavoritos("UserTesteNome", idGato);
        usuarioRepository.adicionarAnimalAosFavoritos("UserTesteNome", idCachorro);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> resultado = usuarioRepository.findAnimaisFavoritosByNomeUsuario("UserTesteNome", pageable);

        List<String> nomesAnimaisFavoritos = resultado.getContent()
                .stream()
                .map(Animal::getNome)
                .toList();
        boolean contemAnimaisFavoritos = nomesAnimaisFavoritos.containsAll(List.of("Garfield", "Mel"));
        assertTrue(contemAnimaisFavoritos);
    }
}