package br.com.alevh.sistema_adocao_pets.config;

import br.com.alevh.sistema_adocao_pets.repository.OngRepository;
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
    private final OngRepository ongRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(identifier)
                .map(user -> (UserDetails) user)
                .or(() -> usuarioRepository.findByNomeUsuario(identifier).map(user -> (UserDetails) user))
                .or(() -> ongRepository.findByEmail(identifier).map(user -> (UserDetails) user))
                .or(() -> ongRepository.findByNomeUsuario(identifier).map(user -> (UserDetails) user))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário ou ONG não encontrado com o identificador: " + identifier));
    }
}