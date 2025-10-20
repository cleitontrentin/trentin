package com.panificadora.trentin.dao;

import java.util.List;

import com.panificadora.trentin.domain.Funcionario;

public interface FuncionarioDao {
    
	void save(Funcionario funcionario);

    void update(Funcionario funcionario);

    void delete(Long id);

    Funcionario findById(Long id);

    List<Funcionario> findAll();

	List<Funcionario> findbyNome(String nome);
}

