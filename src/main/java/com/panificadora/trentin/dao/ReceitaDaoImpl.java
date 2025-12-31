package com.panificadora.trentin.dao;

import org.springframework.stereotype.Repository;

import com.panificadora.trentin.entities.Receita;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ReceitaDaoImpl extends AbstractDao<Receita, Long> implements ReceitaDao {
	
    @PersistenceContext
    private EntityManager em;

	@Override
    public Receita findByIdComItens(Long id) {
        return em.createQuery("""
            select distinct r from Receita r
            left join fetch r.itens i
            left join fetch i.insumo
            where r.id = :id
        """, Receita.class)
        .setParameter("id", id)
        .getSingleResult();
    }
	
	
}
