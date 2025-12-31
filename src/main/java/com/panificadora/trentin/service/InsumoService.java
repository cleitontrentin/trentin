package com.panificadora.trentin.service;

import java.util.List;

import com.panificadora.trentin.entities.Insumo;
import com.panificadora.trentin.entities.UnidadeDeMedida;


public interface InsumoService {
	
    void salvar(Insumo insumo);

    void editar(Insumo insumo);

    void excluir(Long id);

    Insumo buscarPorId(Long id);
    
    List<Insumo> buscarTodos();

}
