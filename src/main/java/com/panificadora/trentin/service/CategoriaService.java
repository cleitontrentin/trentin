package com.panificadora.trentin.service;

import java.util.List;

import com.panificadora.trentin.entities.Categoria;

public interface CategoriaService {

    void salvar(Categoria categoria);

    void editar(Categoria categoria);

    void excluir(Long id);

    Categoria buscarPorId(Long id);
    
    List<Categoria> buscarTodos();

	boolean categoriaTemProduto(Long id);

	
}
