package com.panificadora.trentin.service;

import java.util.List;

import com.panificadora.trentin.domain.Cargo;
import com.panificadora.trentin.domain.Receita;

public interface ReceitaService {

	void salvar(Receita receita);
	
	void editar(Receita receita);
	
	void excluir(Long id);
	
	Receita buscarPorId(Long id);
	
	List<Receita> buscarTodos();
	

}
