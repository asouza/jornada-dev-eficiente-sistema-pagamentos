package com.deveficiente.pagamentos.modeladominio;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Email
	private String email;
	@Size(min = 1)
	@ElementCollection
	private Set<FormaPagamento> formasPagamento = new HashSet<>();

	public Usuario(@NotBlank @Email String email,
			@Size(min = 1) FormaPagamento... formasPagamento) {
		Assert.hasText(email,"O email é obrigatório");
		Assert.notNull(formasPagamento,"Formas de pagamento não pode ser nulo");
		Assert.isTrue(formasPagamento.length > 0,"Precisa de pelo menos uma forma de pagamento");
		
		this.email = email;
		Stream.of(formasPagamento).forEach(this.formasPagamento::add);
	}

}
