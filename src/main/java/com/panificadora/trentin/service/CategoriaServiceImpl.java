package com.panificadora.trentin.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panificadora.trentin.dao.CategoriaDao;
import com.panificadora.trentin.entities.Categoria;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
	
	@Autowired
	private CategoriaDao dao;


	@Override
	public void salvar(Categoria categoria) {
		dao.save(categoria);
		
	}

	@Override
	public void editar(Categoria categoria) {
		dao.update(categoria);
		
	}

	@Override
	public void excluir(Long id) {
		dao.delete(id);
		
	}

	@Override
	public Categoria buscarPorId(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<Categoria> buscarTodos() {
		return dao.findAll();
	}
	

	@Override
	public boolean categoriaTemProduto(Long id) {
		if (buscarPorId(id).getProdutos().isEmpty()) {
			return false;
		}
		return true;
	}

}
