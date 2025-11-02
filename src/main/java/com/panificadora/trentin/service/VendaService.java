package com.panificadora.trentin.service;

import java.util.List;

import com.panificadora.trentin.domain.MetodoPagamento;
import com.panificadora.trentin.domain.Venda;

public interface VendaService {

	void salvar(Venda venda);
	
	void editar(Venda venda);
	
	void excluir(Long id);
	
	Venda buscarPorId(Long id);
	
	List<Venda> buscarTodos();
	
	Venda criarVenda();

	Venda adicionarItemPorCodigo(Long vendaId, String codigo, int quantidade);

	Venda finalizarVenda(Long vendaId, MetodoPagamento metodoPagamento, String cliente);

	Venda buscarPorIdComItens(Long id);
}
