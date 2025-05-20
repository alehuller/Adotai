package br.com.alevh.sistema_adocao_pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.model.Ong;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {

}
