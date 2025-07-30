package br.com.alevhvm.adotai.administrador.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.repository.AdministradorRepository;
import br.com.alevhvm.adotai.administrador.validations.AdministradorValidacao;
import br.com.alevhvm.adotai.auth.service.TokenService;

import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class AdministradorServiceTest {

    @Mock 
    private AdministradorValidacao administradorValidacao;

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PagedResourcesAssembler<AdministradorDTO> assembler;

    @Mock
    private Validator validator;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AdministradorService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
