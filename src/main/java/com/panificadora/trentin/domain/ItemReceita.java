package com.panificadora.trentin.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "RECEITA_ITENS")
public class ItemReceita  extends AbstractEntity<Long>{
	
	@ManyToOne
    @JoinColumn(name = "insumo_pk")
    private Insumo insumo;

    private Double quantidade;

    @ManyToOne
    @JoinColumn(name = "receita_pk")
    private Receita receita;


    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

}
