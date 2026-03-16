package com.panificadora.trentin.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "nfce_numero")
public class NfceNumero extends AbstractEntity<Long> {
	
	private Long ultimoNumero;


    public Long getUltimoNumero() {
        return ultimoNumero;
    }

    public void setUltimoNumero(Long ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }
}


