package com.panificadora.trentin.domain;

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

    private Integer stock;

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

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	// --- Métodos de negócio ---
    public void addStock(int quantity) {
        this.stock = (this.stock == null ? 0 : this.stock) + quantity;
    }

    public void removeStock(int quantity) {
        if (this.stock != null && this.stock >= quantity) {
            this.stock -= quantity;
        } else {
            throw new RuntimeException("Estoque insuficiente para o produto: " + name);
        }
    }

}