package br.com.alevhvm.adotai.auth.service;

import br.com.alevhvm.adotai.auth.repository.LoginIdentityViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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