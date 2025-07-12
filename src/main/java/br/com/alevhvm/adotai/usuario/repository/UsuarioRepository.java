package br.com.alevh.sistema_adocao_pets.usuario.repository;

import br.com.alevh.sistema_adocao_pets.animal.model.Animal;
import br.com.alevh.sistema_adocao_pets.usuario.model.Usuario;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByCell(String cell);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    void deleteByNomeUsuario(String nomeUsuario);

    @Query("SELECT a FROM Usuario u JOIN u.animaisFavoritos a WHERE u.nomeUsuario = :nomeUsuario")
    Page<Animal> findAnimaisFavoritosByNomeUsuario(@Param("nomeUsuario") String nomeUsuario, Pageable pageable);

    boolean existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal(String nomeUsuario, Long animalId);

    @Query("SELECT u.idUsuario FROM Usuario u WHERE u.nomeUsuario = :nomeUsuario")
    Long findIdByNomeUsuario(@Param("nomeUsuario") String nomeUsuario);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM usuario_animais_favoritos WHERE usuario_id = (SELECT id FROM usuario WHERE nome_usuario = ?1) AND animal_id = ?2", nativeQuery = true)
    void removerFavoritoNativo(String nomeUsuario, Long animalId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO usuario_animais_favoritos (usuario_id, animal_id) VALUES ((SELECT id FROM usuario WHERE nome_usuario = ?1), ?2)", nativeQuery = true)
    void adicionarFavoritoNativo(String nomeUsuario, Long animalId);
}
