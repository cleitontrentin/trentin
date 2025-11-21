package com.panificadora.trentin.dao;

import java.util.List;

import com.panificadora.trentin.domain.Cargo;
import com.panificadora.trentin.domain.Receita;

public interface ReceitaDao {

    void save(Receita receita );

    void update(Receita receita);

    void delete(Long id);

    Receita findById(Long id);

    List<Receita> findAll();
}
