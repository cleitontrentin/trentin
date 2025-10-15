package com.panificadora.trentin.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.domain.Produto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class ProductDaoImpl extends AbstractDao<Produto, Long> implements ProductDao {
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	    @Override
	    public Optional<Produto> findByCode(String code) {
	        List<Produto> result = entityManager.createQuery(
	                "SELECT p FROM Produto p WHERE p.code = :code", Produto.class)
	                .setParameter("code", code)
	                .getResultList();
	        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	    }


}
