package br.com.alevh.sistema_adocao_pets.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alevh.sistema_adocao_pets.repository.LoginIdentityViewRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final LoginIdentityViewRepository loginIdentityViewRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return loginIdentityViewRepository.findByEmail(identifier)
                .or(() -> loginIdentityViewRepository.findByNomeUsuario(identifier))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário ou ONG não encontrado com o identificador: " + identifier));
    }
}