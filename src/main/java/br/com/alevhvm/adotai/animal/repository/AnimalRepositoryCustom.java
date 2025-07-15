package br.com.alevhvm.adotai.animal.repository;

import br.com.alevhvm.adotai.animal.dto.AnimalFiltroDTO;
import br.com.alevhvm.adotai.animal.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AnimalRepositoryCustom {
    Page<Animal> filtrarAnimaisNativo(AnimalFiltroDTO filtro, Pageable pageable);
}
