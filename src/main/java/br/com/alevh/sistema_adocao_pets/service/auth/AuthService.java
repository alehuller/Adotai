package br.com.alevh.sistema_adocao_pets.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        System.out.print(identifier);
        return usuarioRepository.findByEmail(identifier)
                .or(() -> usuarioRepository.findByNomeUsuario(identifier))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado com o identificador: " + identifier));
    }
}
