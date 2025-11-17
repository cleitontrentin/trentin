package com.panificadora.trentin.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "insumos")
public class Insumo extends AbstractEntity<Long>{


    private String nome;

    private String descricao;

    private String unidadeMedida;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tabela_nutricional_id")
    private TabelaNutricional tabelaNutricional;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public TabelaNutricional getTabelaNutricional() {
		return tabelaNutricional;
	}

	public void setTabelaNutricional(TabelaNutricional tabelaNutricional) {
		this.tabelaNutricional = tabelaNutricional;
	}
    
    

}
