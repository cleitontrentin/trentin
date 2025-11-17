package com.panificadora.trentin.dao;

import java.util.List;

import com.panificadora.trentin.domain.Insumo;

public interface InsumoDao {

    void save(Insumo insumo );

    void update(Insumo insumo);

    void delete(Long id);

    Insumo findById(Long id);

    List<Insumo> findAll();
}
