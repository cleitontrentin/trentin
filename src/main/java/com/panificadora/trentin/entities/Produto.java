package com.panificadora.trentin.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "PRODUTOS")
public class Produto extends AbstractEntity<Long> {

	@Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(7,2) DEFAULT 0.00")
    private BigDecimal price;

	private BigDecimal stock;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING) // Garante que o valor seja salvo como string
    @Column(name = "unidade_medida")
    private UnidadeDeMedida unidadeDeMedida;
    
	@ManyToOne
	@JoinColumn(name = "id_categoria_fk")
	@JsonIgnore
	private Categoria categoria;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public void addStock(BigDecimal quantidade) {

	    if (this.stock == null) {
	        this.stock = BigDecimal.ZERO;
	    }

	    this.stock = this.stock.add(quantidade);
	}


    public void removeStock(BigDecimal quantidade) {

        if (this.stock == null) {
            this.stock = BigDecimal.ZERO;
        }

        if (this.stock.compareTo(quantidade) < 0) {
            throw new RuntimeException(
                "Estoque insuficiente para o produto: " + name
            );
        }

        this.stock = this.stock.subtract(quantidade);
    }


}