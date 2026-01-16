package com.panificadora.trentin.entities;

import java.math.BigDecimal;

public class CodigoBalancaParser {

    public static CodigoBalancaDTO parse(String codigo) {

        if (codigo.length() == 13 && codigo.startsWith("2")) {

            String codigoProduto = codigo.substring(1, 5);
            String valorStr = codigo.substring(6, 12); // 00205

            BigDecimal valor = new BigDecimal(valorStr)
                    .divide(BigDecimal.valueOf(100)); // centavos → reais

            return new CodigoBalancaDTO(
                codigoProduto.replaceFirst("^0+", ""),
                valor,
                true
            );
        }

        // Produto comum
        return new CodigoBalancaDTO(codigo, BigDecimal.ZERO, false);
    }
}


