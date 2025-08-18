package br.com.alevhvm.adotai.animal.service;

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

import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @InjectMocks
    private AnimalService animalService;

    @Mock
    private OngRepository ongRepository;

    @Mock
    private AnimalRepository animalRepository;

    private AnimalDTO animalDTO;
    private Animal animalEntity;
    private Ong ong;

    private DescricaoVO descricaoVO;
    private RedeVO redeVO;

    @BeforeEach
    void setUp() {
        redeVO = new RedeVO();
        redeVO.setInstagram("https://teste.adotai.com/");
        redeVO.setFacebook("https://teste.facebook.com/adotai");

        ong = new Ong();
        ong.setIdOng(1L);
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

        descricaoVO = new DescricaoVO();
        descricaoVO.setGeral("Descrição Teste");
        descricaoVO.setHistoricoSaude("Historico Teste");
        descricaoVO.setVacinacao("Vacinacao Teste");

        animalDTO = new AnimalDTO();
        animalDTO.setKey(1L);
        animalDTO.setNome("Animal Nome");
        animalDTO.setEspecie("Cachorro");
        animalDTO.setRaca("Spitz Alemão");
        animalDTO.setDataNascimento(LocalDate.parse("2024-07-11"));
        animalDTO.setFoto("Foto Teste");
        animalDTO.setDescricao(descricaoVO);
        animalDTO.setPorte("Porte Teste");
        animalDTO.setSexo("Macho");
        animalDTO.setStatus(StatusAnimal.ADOTADO);
        animalDTO.setIdOng(1L);

        animalEntity = new Animal();
        animalEntity.setIdAnimal(1L);
        animalEntity.setNome("Animal Nome");
        animalEntity.setEspecie("Cachorro");
        animalEntity.setRaca("Spitz Alemão");
        animalEntity.setDataNascimento(LocalDate.parse("2024-07-11"));
        animalEntity.setFoto("Foto Teste");
        animalEntity.setDescricao(descricaoVO);
        animalEntity.setPorte("Porte Teste");
        animalEntity.setSexo("Macho");
        animalEntity.setStatus(StatusAnimal.ADOTADO);
        animalEntity.setOng(ong);
    }

    @Test
    void deveCriarAnimalComSucesso() {
        when(ongRepository.findById(animalDTO.getIdOng())).thenReturn(Optional.of(ong));
        when(animalRepository.save(any(Animal.class))).thenReturn(animalEntity);

        AnimalDTO resultado = animalService.create(animalDTO);

        assertNotNull(resultado);
        assertEquals("Animal Nome", resultado.getNome());
        assertEquals("Cachorro", resultado.getEspecie());
        assertEquals("Spitz Alemão", resultado.getRaca());
        verify(ongRepository).findById(animalDTO.getIdOng());
        verify(animalRepository).save(any(Animal.class));
    }
}
