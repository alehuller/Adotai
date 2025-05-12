package br.com.alevh.sistema_adocao_pets.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private Logger logger = Logger.getLogger(UsuarioService.class.getName());

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario create(Usuario user) {
        logger.info("Criando um usuário");
        return usuarioRepository.save(user);
    }

    public void delete(Long id) {
        logger.info(String.format("Apagando usuário de id %d", id));
        usuarioRepository.deleteById(id);
    }

    public Usuario update(Usuario usuario) {
        logger.info(String.format("Atualizando usuário de nome %s", usuario.getNome()));
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Long id) {
        logger.info(String.format("Consultando usuário de id $d", id));
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Usuário de id %d não encontrado", id)));
    }
}
