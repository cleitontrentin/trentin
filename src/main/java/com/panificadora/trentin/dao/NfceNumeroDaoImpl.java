package com.panificadora.trentin.dao;

import org.springframework.stereotype.Repository;

import com.panificadora.trentin.entities.NfceNumero;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class NfceNumeroDaoImpl implements NfceNumeroDao {
	
	@PersistenceContext
    private EntityManager em;

	@Override
	public NfceNumero buscarControle() {
		return em.find(NfceNumero.class, 1L);
	}

	@Override
	public void update(NfceNumero controle) {
		em.merge(controle);
		
	}

}
