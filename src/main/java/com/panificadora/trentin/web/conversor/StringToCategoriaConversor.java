package com.panificadora.trentin.web.conversor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.panificadora.trentin.domain.Categoria;
import com.panificadora.trentin.service.CategoriaService;

@Component
public class StringToCategoriaConversor implements Converter<String, Categoria> {
	
	@Autowired
	private CategoriaService service;

	@Override
	public Categoria convert(String text) {
		if (text.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(text);
		return service.buscarPorId(id);
	}

}
