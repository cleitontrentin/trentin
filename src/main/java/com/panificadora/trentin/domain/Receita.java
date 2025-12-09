package com.panificadora.trentin.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "RECEITAS")
public class Receita extends AbstractEntity<Long> {
	
	private String nome;
	
	private BigDecimal quantidade;
	
	private Integer rendimento;
	
    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemReceita> itens = new ArrayList<>();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

    public List<ItemReceita> getItens() {
        return itens;
    }

    public void setItens(List<ItemReceita> itens) {
        this.itens = itens;
    }

	public Integer getRendimento() {
		return rendimento;
	}

	public void setRendimento(Integer rendimento) {
		this.rendimento = rendimento;
	}
    
	@Transient
	public BigDecimal getCustoTotal() {
	    return itens.stream()
	        .map(ItemReceita::getCustoItem)
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Transient
	public BigDecimal getCustoUnitario() {
	    if (getRendimento() == null || getRendimento() == 0) {
	        return BigDecimal.ZERO;
	    }

	    return getCustoTotal()
	            .divide(BigDecimal.valueOf(getRendimento()), 4, RoundingMode.HALF_UP);
	}
	
}
