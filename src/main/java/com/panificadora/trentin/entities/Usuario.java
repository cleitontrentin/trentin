package com.panificadora.trentin.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@SuppressWarnings("serial")
@Entity
public class Usuario extends AbstractEntity<Long> {
	
	@Column(unique = true)
    private String username;

    private String senha;

    private String role;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    

}
