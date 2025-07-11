package br.com.alevh.sistema_adocao_pets.animal.repository;

import br.com.alevh.sistema_adocao_pets.animal.dto.AnimalFiltroDTO;
import br.com.alevh.sistema_adocao_pets.animal.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AnimalRepositoryCustom {
    Page<Animal> filtrarAnimaisNativo(AnimalFiltroDTO filtro, Pageable pageable);
}
