package br.com.alevh.sistema_adocao_pets.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Usuario acharPorId(@PathVariable(value = "id") Long id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario user) {
        return usuarioService.create(user);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletarPorId(@PathVariable(value = "id") Long id){
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    } 

    @PutMapping
    public Usuario atualizarUsuario(@RequestBody Usuario usuario) {        
        return usuarioService.update(usuario);
    }
}
