package com.panificadora.trentin.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "RECEITA_ITENS")
public class ItemReceita  extends AbstractEntity<Long>{
	
	@ManyToOne
    @JoinColumn(name = "insumo_pk")
    private Insumo insumo;

    private BigDecimal quantidade;

    @ManyToOne
    @JoinColumn(name = "receita_pk")
    private Receita receita;

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    @Transient
    public BigDecimal getCustoItem() {
        if (insumo == null 
            || insumo.getPreco() == null 
            || quantidade == null 
            || insumo.getPeso() == null) {
            return BigDecimal.ZERO;
        }

        // preço por kg
        BigDecimal precoPorKg = insumo.getPreco()
                .divide(insumo.getPeso(), 10, RoundingMode.HALF_UP);

        // custo proporcional
        return precoPorKg.multiply(quantidade);
    }



}