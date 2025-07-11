package br.com.alevh.sistema_adocao_pets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.model.LoginIdentityView;

@Repository
public interface LoginIdentityViewRepository extends JpaRepository<LoginIdentityView, Long> {
    Optional<LoginIdentityView> findByEmail(String email);

    Optional<LoginIdentityView> findByNomeUsuario(String nomeUsuario);
}
