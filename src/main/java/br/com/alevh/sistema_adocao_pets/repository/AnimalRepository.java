package br.com.alevh.sistema_adocao_pets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByNome(String nome);

    void deleteByNome(String nome);
    
}