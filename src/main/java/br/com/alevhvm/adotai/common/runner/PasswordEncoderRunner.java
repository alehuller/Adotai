package br.com.alevh.sistema_adocao_pets.common.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.alevh.sistema_adocao_pets.administrador.repository.AdministradorRepository;
import br.com.alevh.sistema_adocao_pets.ong.repository.OngRepository;
import br.com.alevh.sistema_adocao_pets.usuario.repository.UsuarioRepository;
import br.com.alevh.sistema_adocao_pets.administrador.model.Administrador;
import br.com.alevh.sistema_adocao_pets.ong.model.Ong;
import br.com.alevh.sistema_adocao_pets.usuario.model.Usuario;

import java.util.List;

@Component
public class PasswordEncoderRunner implements ApplicationRunner {

    private final OngRepository ongRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderRunner(UsuarioRepository usuarioRepository, OngRepository ongRepository,
            AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.ongRepository = ongRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Ong> ongs = ongRepository.findAll();
        List<Administrador> administradores = administradorRepository.findAll();

        for (Usuario usuario : usuarios) {
            if (!usuario.getSenha().startsWith("{bcrypt}")) {
                String encodedPassword = passwordEncoder.encode(usuario.getSenha());
                usuario.setSenha(encodedPassword);
                usuarioRepository.save(usuario);
            }
        }

        for (Ong ong : ongs) {
            if (!ong.getSenha().startsWith("{bcrypt}")) {
                String encodedPassword = passwordEncoder.encode(ong.getSenha());
                ong.setSenha(encodedPassword);
                ongRepository.save(ong);
            }
        }

        for (Administrador administrador : administradores) {
            if (!administrador.getSenha().startsWith("{bcrypt}")) {
                String encodedPassword = passwordEncoder.encode(administrador.getSenha());
                administrador.setSenha(encodedPassword);
                administradorRepository.save(administrador);
            }

        }
    }
}