package com.deveficiente.pagamentos.listapagamentos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;

@Entity
public class Fraude {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Email
	private String email;
	
	public Fraude(@Email String email) {
		super();
		this.email = email;
	}
	
	
}
