package com.panificadora.trentin.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class Venda extends AbstractEntity<Long>  {
	
	@Column(updatable = false)	
	private LocalDateTime criadoEm = LocalDateTime.now();

    private String cliente; // opcional

    @Enumerated(EnumType.STRING)
    private VendaStatus status = VendaStatus.ABERTA;

    private BigDecimal total = BigDecimal.ZERO;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VendaItem> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento; 
    
    @Column(name = "chave_nfce")
    private String chaveNfce;
    
    @Column(name = "numero_nfce")
    private String numeroNfce;
    
    @Column(name = "status_nfce")
    private String statusNfce;

    @Column(name = "protocolo_nfce")
    private String protocoloNfce;
    
    @Column(name = "xml_nfce", columnDefinition = "LONGTEXT")
    private String xmlNfce;
    
    @Column(name = "data_emissao_nfce")
    private LocalDateTime dataEmissaoNfce;

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public VendaStatus getStatus() {
		return status;
	}

	public void setStatus(VendaStatus status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<VendaItem> getItens() {
		return itens;
	}

	public void setItens(List<VendaItem> itens) {
		this.itens = itens;
	}

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
 
    public String getChaveNfce() {
		return chaveNfce;
	}

	public void setChaveNfce(String chaveNfce) {
		this.chaveNfce = chaveNfce;
	}

	public String getNumeroNfce() {
		return numeroNfce;
	}

	public void setNumeroNfce(String numeroNfce) {
		this.numeroNfce = numeroNfce;
	}

	public String getStatusNfce() {
		return statusNfce;
	}

	public void setStatusNfce(String statusNfce) {
		this.statusNfce = statusNfce;
	}

	public String getProtocoloNfce() {
		return protocoloNfce;
	}

	public void setProtocoloNfce(String protocoloNfce) {
		this.protocoloNfce = protocoloNfce;
	}
	
	public String getXmlNfce() {
	    return xmlNfce;
	}

	public void setXmlNfce(String xmlNfce) {
	    this.xmlNfce = xmlNfce;
	}
	
	public LocalDateTime getDataEmissaoNfce() {
	    return dataEmissaoNfce;
	}

	public void setDataEmissaoNfce(LocalDateTime dataEmissaoNfce) {
	    this.dataEmissaoNfce = dataEmissaoNfce;
	}

	public void adicionarItem(VendaItem item) {
        item.setVenda(this);
        this.itens.add(item);
        recalcularTotal();
    }

    public void removerItem(Long itemId) {
        this.itens.removeIf(i -> i.getId() != null && i.getId().equals(itemId));
        recalcularTotal();
    }
    

    public void recalcularTotal() {
        this.total = this.itens.stream()
                .map(VendaItem::getSubtotal)
                .filter(s -> s != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public boolean possuiNfce() {
        return this.chaveNfce != null && !this.chaveNfce.isEmpty();
    }
    

}
