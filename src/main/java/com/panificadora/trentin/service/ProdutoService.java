package com.panificadora.trentin.service;

import java.util.List;
import java.util.Optional;

import com.panificadora.trentin.entities.Produto;

public interface ProdutoService {
	
	void salvar(Produto product);
	
	void editar(Produto product);
	
	void excluir(Long id);
	
	Produto buscarPorId(Long id);
	
	List<Produto> buscarTodos();

    Produto findByCode(String code);

	void addStock(Long id, int quantity);

	void removeStock(Long id, int quantity);

}
