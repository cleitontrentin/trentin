package com.panificadora.trentin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.dao.ProductDao;
import com.panificadora.trentin.entities.Produto;

@Service
@Transactional(readOnly = false)
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProductDao dao;

	@Override
	public void salvar(Produto product) {
		dao.save(product);

	}

	@Override
	public void editar(Produto product) {
		dao.update(product);

	}

	@Override
	public void excluir(Long id) {
		dao.delete(id);

	}

	@Override
	public Produto buscarPorId(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<Produto> buscarTodos() {
		return dao.findAll();
	}
	

	@Override
	public Produto findByCode(String code) {
		
		return dao.findByCode(code);
	}



	@Override
	@Transactional
	public void addStock(Long id, int quantity) {
		Produto product = dao.findById(id);
		product.addStock(quantity);
		dao.save(product);
	}

	@Override
	@Transactional
	public void removeStock(Long id, int quantity) {
		Produto product = dao.findById(id);
		product.removeStock(quantity);
		dao.save(product);
	}



}
