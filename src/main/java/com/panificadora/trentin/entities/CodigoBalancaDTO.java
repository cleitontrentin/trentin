package com.panificadora.trentin.entities;

import java.math.BigDecimal;

public class CodigoBalancaDTO {

    private String codigoProduto;
    private BigDecimal valor;   // valor final
    private boolean porValor;

    public CodigoBalancaDTO(String codigoProduto, BigDecimal valor, boolean porValor) {
        this.codigoProduto = codigoProduto;
        this.valor = valor;
        this.porValor = porValor;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public boolean isPorValor() {
        return porValor;
    }
}

