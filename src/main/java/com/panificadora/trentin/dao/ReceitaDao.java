package com.panificadora.trentin.dao;

import java.util.List;

import com.panificadora.trentin.entities.Cargo;
import com.panificadora.trentin.entities.Receita;

public interface ReceitaDao {

    void save(Receita receita );

    void update(Receita receita);

    void delete(Long id);

    Receita findById(Long id);

    List<Receita> findAll();
    
    Receita findByIdComItens(Long id);
}
