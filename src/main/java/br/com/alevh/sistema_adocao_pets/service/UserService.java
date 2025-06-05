package br.com.alevh.sistema_adocao_pets.service;

import java.util.logging.Logger;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.repository.OngRepository;

@Service
public class UserService implements UserDetailsService{
    
    private Logger logger = Logger.getLogger(UserService.class.getName());

    UsuarioRepository usuarioRepository;
    OngRepository ongRepository;

    public UserService(UsuarioRepository usuarioRepository, OngRepository ongRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ongRepository = ongRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Carregando usuário pelo email: " + email);
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email);
        Ong ong = ongRepository.findOngByEmail(email);
        if (usuario != null) {
            return usuario;
        } else if (ong != null) {
            return ong;
        } else {
            throw new UsernameNotFoundException(String.format("%s não foi encontrado.", email));
        }
    }
}