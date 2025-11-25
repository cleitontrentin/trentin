package com.panificadora.trentin.domain;

public enum UnidadeDeMedida {
	UNIDADE("Unidade"),
    KG("Kg"),
    LITRO("Litro");
    
    private  String descricao;
    

	UnidadeDeMedida(String unidadeDeMedida) {
        this.descricao = unidadeDeMedida;
    }

    public String getUnidadeDeMedida() {
        return descricao;
    }

}
