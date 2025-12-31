package com.panificadora.trentin.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.panificadora.trentin.entities.Produto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class ProductDaoImpl extends AbstractDao<Produto, Long> implements ProductDao {
	
	
	@Override
    public Produto findByCode(String code) {
        List<Produto> result = createQuery(
            "SELECT p FROM Produto p WHERE p.code = ?1", code
        );
        // retorna o primeiro produto se existir, senão retorna null
        return result.isEmpty() ? null : result.get(0);
    }
}
