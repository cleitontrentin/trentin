package com.panificadora.trentin.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@SuppressWarnings("serial")
@Entity
public class VendaItem extends AbstractEntity<Long> {
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Produto produto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id")
    private Venda venda;
    
    
    
    public Produto getProduto() {
		return produto;
	}



	public void setProduto(Produto produto) {
		this.produto = produto;
	}



	public Integer getQuantidade() {
		return quantidade;
	}



	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}



	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}



	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}



	public BigDecimal getSubtotal() {
		return subtotal;
	}



	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}



	public Venda getVenda() {
		return venda;
	}



	public void setVenda(Venda venda) {
		this.venda = venda;
	}



	@PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (precoUnitario != null && quantidade != null) {
            subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        } else {
            subtotal = BigDecimal.ZERO;
        }
    }

}
