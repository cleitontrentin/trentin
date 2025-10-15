package com.panificadora.trentin.dao;

import java.util.List;
import java.util.Optional;

import com.panificadora.trentin.domain.Produto;

public interface ProductDao {
	
	void save(Produto Product);
	
	void update(Produto Product);
	
	void delete(Long id);

    List<Produto> findAll();
    
    Produto findById(Long id);

    Optional<Produto> findByCode(String code);

}
