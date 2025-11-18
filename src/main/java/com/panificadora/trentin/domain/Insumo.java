package com.panificadora.trentin.domain;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "insumos")
public class Insumo extends AbstractEntity<Long>{


    private String nome;
    
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(7,2) DEFAULT 0.00")
    private BigDecimal preco;

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

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
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
