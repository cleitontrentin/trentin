package com.panificadora.trentin.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "tabela_nutricional")
public class TabelaNutricional extends AbstractEntity<Long> {
	
	private BigDecimal valorEnergetico; // kcal

    private BigDecimal carboidratos; // g
    
    private BigDecimal acucaresTotais; // g
    
    private BigDecimal acucaresAdicionados; //g

    private BigDecimal proteinas; // g

    private BigDecimal gordurasTotais; // g

    private BigDecimal gordurasSaturadas; // g

    private BigDecimal gordurasTrans; // g

    private BigDecimal fibraAlimentar; // g

    private BigDecimal sodio; // mg

	public BigDecimal getValorEnergetico() {
		return valorEnergetico;
	}

	public void setValorEnergetico(BigDecimal valorEnergetico) {
		this.valorEnergetico = valorEnergetico;
	}

	public BigDecimal getCarboidratos() {
		return carboidratos;
	}

	public void setCarboidratos(BigDecimal carboidratos) {
		this.carboidratos = carboidratos;
	}

	public BigDecimal getAcucaresTotais() {
		return acucaresTotais;
	}

	public void setAcucaresTotais(BigDecimal acucaresTotais) {
		this.acucaresTotais = acucaresTotais;
	}

	public BigDecimal getAcucaresAdicionados() {
		return acucaresAdicionados;
	}

	public void setAcucaresAdicionados(BigDecimal acucaresAdicionados) {
		this.acucaresAdicionados = acucaresAdicionados;
	}

	public BigDecimal getProteinas() {
		return proteinas;
	}

	public void setProteinas(BigDecimal proteinas) {
		this.proteinas = proteinas;
	}

	public BigDecimal getGordurasTotais() {
		return gordurasTotais;
	}

	public void setGordurasTotais(BigDecimal gordurasTotais) {
		this.gordurasTotais = gordurasTotais;
	}

	public BigDecimal getGordurasSaturadas() {
		return gordurasSaturadas;
	}

	public void setGordurasSaturadas(BigDecimal gordurasSaturadas) {
		this.gordurasSaturadas = gordurasSaturadas;
	}

	public BigDecimal getGordurasTrans() {
		return gordurasTrans;
	}

	public void setGordurasTrans(BigDecimal gordurasTrans) {
		this.gordurasTrans = gordurasTrans;
	}

	public BigDecimal getFibraAlimentar() {
		return fibraAlimentar;
	}

	public void setFibraAlimentar(BigDecimal fibraAlimentar) {
		this.fibraAlimentar = fibraAlimentar;
	}

	public BigDecimal getSodio() {
		return sodio;
	}

	public void setSodio(BigDecimal sodio) {
		this.sodio = sodio;
	}
    
}