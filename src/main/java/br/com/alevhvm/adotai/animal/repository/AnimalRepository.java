package br.com.alevhvm.adotai.animal.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alevhvm.adotai.animal.model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>, AnimalRepositoryCustom {

    Optional<Animal> findByNome(String nome);

    void deleteByNome(String nome);

    //@Query("SELECT a FROM Animal a WHERE a.ong.nomeUsuario = :nomeUsuario")
    Page<Animal> findByOngNomeUsuario(String nomeUsuario, Pageable pageable);


}