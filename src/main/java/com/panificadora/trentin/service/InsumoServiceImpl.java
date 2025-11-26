package com.panificadora.trentin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.dao.InsumoDao;
import com.panificadora.trentin.domain.Insumo;

@Service
@Transactional(readOnly = false)
public class InsumoServiceImpl implements InsumoService {
	
	@Autowired
	private InsumoDao dao;

	@Override
	public void salvar(Insumo insumo) {
		dao.save(insumo);
		
	}

	@Override
	public void editar(Insumo insumo) {
		dao.update(insumo);
		
	}

	@Override
	public void excluir(Long id) {
		dao.delete(id);
		
	}

	@Transactional(readOnly = true)
	@Override
	public Insumo buscarPorId(Long id) {
		return dao.findById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Insumo> buscarTodos() {
		return dao.findAll();
	}


}
