package com.panificadora.trentin.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "RECEITAS")
public class Receita extends AbstractEntity<Long> {
	
	private String nomeReceita;
	
	private BigDecimal quantidade;
	
	@ElementCollection
	@CollectionTable(name = "RECEITA_INSUMOS", joinColumns = @JoinColumn(name = "receita_id"))
	@MapKeyJoinColumn(name = "insumo_id")
	@Column(name ="quantidade")
	private Map<Insumo, Double> insumos = new HashMap<>();

	public String getNomeReceita() {
		return nomeReceita;
	}

	public void setNomeReceita(String nomeReceita) {
		this.nomeReceita = nomeReceita;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public Map<Insumo, Double> getInsumos() {
		return insumos;
	}

	public void setInsumos(Map<Insumo, Double> insumos) {
		this.insumos = insumos;
	}
    
}
