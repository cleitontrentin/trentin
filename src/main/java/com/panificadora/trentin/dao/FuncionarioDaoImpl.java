package com.panificadora.trentin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.panificadora.trentin.domain.Funcionario;

@Repository
public class FuncionarioDaoImpl extends AbstractDao<Funcionario, Long> implements FuncionarioDao {

	@Override
	public List<Funcionario> findbyNome(String nome) {
		
       return createQuery( "SELECT F FROM funcionario F WHERE F.NOME LIKE CONCAT('%',?1,'%')", nome);
	}

}
