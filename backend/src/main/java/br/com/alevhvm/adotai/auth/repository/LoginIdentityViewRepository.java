package br.com.alevhvm.adotai.auth.repository;

import br.com.alevhvm.adotai.auth.model.LoginIdentityView;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginIdentityViewRepository extends JpaRepository<LoginIdentityView, Long> {
    Optional<LoginIdentityView> findByEmail(String email);

    Optional<LoginIdentityView> findByNomeUsuario(String nomeUsuario);
}
