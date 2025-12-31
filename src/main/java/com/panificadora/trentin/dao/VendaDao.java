package com.panificadora.trentin.dao;

import java.util.List;

import com.panificadora.trentin.entities.Venda;

public interface VendaDao {

    void save(Venda venda );

    void update(Venda venda);

    void delete(Long id);

    Venda findById(Long id);

    List<Venda> findAll();

    Venda buscarPorIdComItens(Long id);
}
