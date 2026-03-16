package com.panificadora.trentin.entities;

import java.math.BigDecimal;

public class CodigoBalancaParser {

	public static CodigoBalancaDTO parse(String codigo) {

	    if (codigo.length() == 13 && codigo.startsWith("2")) {

	        String codigoProduto = codigo.substring(1, 5);

	        // pega apenas os 3 últimos dígitos antes do verificador
	        String valorStr = codigo.substring(9, 12);

	        BigDecimal valor = new BigDecimal(valorStr)
	                .divide(BigDecimal.valueOf(100));

	        CodigoBalancaDTO dto = new CodigoBalancaDTO(
	                codigoProduto.replaceFirst("^0+", ""),
	                valor,
	                true
	        );

	        dto.setQuantidade(BigDecimal.ONE);

	        return dto;
	    }

	    return new CodigoBalancaDTO(codigo, BigDecimal.ZERO, false);
	
	}
}


