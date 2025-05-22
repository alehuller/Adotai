package br.com.alevh.sistema_adocao_pets.service;

import java.util.logging.Logger;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import br.com.alevh.sistema_adocao_pets.repository.OngRepository;

@Service
public class UserServices implements UserDetailsService{
    
    private Logger logger = Logger.getLogger(UserServices.class.getName());

    UsuarioRepository usuarioRepository;
    OngRepository ongRepository;

    public UserServices(UsuarioRepository usuarioRepository, OngRepository ongRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ongRepository = ongRepository;
    }

    // @Override
    // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //     logger.info("Carregando usuário pelo email: " + email);
    //     var usuario = usuarioRepository.findUsuarioByEmail(email);
    //     if (usuario != null) {
    //         return usuario;
    //     } else {
    //         throw new UsernameNotFoundException(String.format("%s não foi encontrado.", email));
    //     }
    // }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Carregando usuário pelo email: " + email);
        var usuario = usuarioRepository.findUsuarioByEmail(email);
        var ong = ongRepository.findOngByEmail(email);
        if (usuario != null) {
            return usuario;
        } else if (ong != null) {
            return ong;
        } else {
            throw new UsernameNotFoundException(String.format("%s não foi encontrado.", email));
        }
    }
}
