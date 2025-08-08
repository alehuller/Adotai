package br.com.alevhvm.adotai.animal.repository;

import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class AnimalRepositoryTest {

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    OngRepository ongRepository;

    final Animal animal = new Animal();
    final Ong ong = new Ong();
    final RedeVO redeVO = new RedeVO();

    @BeforeEach
    void setUp(){
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
    }

    @Test
    void deveEncontrarAnimalComOsMesmosCamposSalvos(){
        Optional<Animal> resultado = animalRepository.findByNome("Garfield");

        assertTrue(resultado.isPresent());
        Animal animalSalvo = resultado.get();

        assertEquals(animalSalvo.getNome(), animal.getNome());
        assertEquals(animalSalvo.getEspecie(), animal.getEspecie());
        assertEquals(animalSalvo.getRaca(), animal.getRaca());
        assertEquals(animalSalvo.getDataNascimento(), animal.getDataNascimento());
        assertEquals(animalSalvo.getFoto(), animal.getFoto());
        assertEquals(animalSalvo.getDescricao(), animal.getDescricao());
        assertEquals(animalSalvo.getPorte(), animal.getPorte());
        assertEquals(animalSalvo.getSexo(), animal.getSexo());
        assertEquals(animalSalvo.getStatus(), animal.getStatus());
        assertSame(animalSalvo.getOng(), ong);
    }

    @Test
    void deveEncontrarAnimalPorNome(){
        Optional<Animal> resultado = animalRepository.findByNome("Garfield");

        assertTrue(resultado.isPresent());
        assertEquals(resultado.get().getNome(), animal.getNome());
    }

    @Test
    void deveDeletarAnimalPorNome(){
        animalRepository.deleteByNome("Garfield");

        Optional<Animal> resultado = animalRepository.findByNome(animal.getNome());
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveEncontrarOngPorNomeUsuario(){

        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> resultado = animalRepository.findByOngNomeUsuario("amigosanimais", pageable);

        List<String> nomesAnimais = resultado.getContent()
                .stream()
                .map(Animal::getNome)
                .toList();

        boolean contemAnimal = nomesAnimais.contains("Garfield");
        assertTrue(contemAnimal );
    }
}
