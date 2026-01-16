package com.panificadora.trentin.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.panificadora.trentin.entities.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.dao.FuncionarioDao;
import com.panificadora.trentin.entities.Funcionario;

@Service
@Transactional(readOnly = true)
public class FuncionarioServiceImpl implements FuncionarioService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private FuncionarioDao dao;

	@Transactional(readOnly = false)
	@Override
	public void salvar(Funcionario funcionario) {

	    Usuario usuario = funcionario.getUsuario();

	    if (usuario != null && usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
	        usuario.setSenha(
	            passwordEncoder.encode(usuario.getSenha())
	        );
	    }

	    dao.save(funcionario);
	}


	@Transactional(readOnly = false)
	@Override
	public void editar(Funcionario funcionario) {

	    Usuario usuario = funcionario.getUsuario();

	    if (usuario != null && usuario.getSenha() != null && !usuario.getSenha().isEmpty()
	        && !usuario.getSenha().startsWith("$2a$")) {

	        usuario.setSenha(
	            passwordEncoder.encode(usuario.getSenha())
	        );
	    }

	    dao.update(funcionario);
	}


	@Transactional(readOnly = false)
	@Override
	public void excluir(Long id) {
		dao.delete(id);
	}

	@Override
	public Funcionario buscarPorId(Long id) {
		
		return dao.findById(id);
	}

	@Override
	public List<Funcionario> buscarTodos() {
		
		return dao.findAll();
	}

	@Override
	public List<Funcionario> buscarPorNome(String nome) {
		
		return dao.findByNome(nome);
	}

	@Override
	public List<Funcionario> buscarPorCargo(Long id) {
		
		return dao.findByCargoId(id);
	}

	@Override
    public List<Funcionario> buscarPorDatas(LocalDate entrada, LocalDate saida) {
	    if (entrada != null && saida != null) {	    	
            return dao.findByDataEntradaDataSaida(entrada, saida);
        } else if (entrada != null) {        	
	        return dao.findByDataEntrada(entrada);
        } else if (saida != null) {        	
	        return dao.findByDataSaida(saida);
        } else {
        	return new ArrayList<>();
        }
    }
}
