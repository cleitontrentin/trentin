package com.panificadora.trentin.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.dao.ReceitaDao;
import com.panificadora.trentin.domain.Receita;

@Service @Transactional(readOnly = false)
public class ReceitaServiceImpl implements ReceitaService {
	
	@Autowired
	private ReceitaDao dao;

	@Override
	public void salvar(Receita receita) {
		dao.save(receita);
		
	}

	@Override
	public void editar(Receita receita) {
		dao.update(receita);
		
	}

	@Override
	public void excluir(Long id) {
		dao.delete(id);
		
	}

	@Override
	public Receita buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}

	@Override
	public List<Receita> buscarTodos() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}
	

	@Override
	public BigDecimal calcularCustoUnitario(Receita receita) {
	    BigDecimal total = calcularCustoTotal(receita);
	    return receita.getRendimento() != null && receita.getRendimento() > 0 
	            ? total.divide(BigDecimal.valueOf(receita.getRendimento()), 4, RoundingMode.HALF_UP)
	            : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal calcularCustoTotal(Receita receita) {
	    return receita.getItens().stream()
	        .<BigDecimal>map(item -> item.getInsumo().getPreco().multiply(item.getQuantidade())) // Cast explícito aqui
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
}
