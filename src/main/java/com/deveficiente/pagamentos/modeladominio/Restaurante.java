package com.deveficiente.pagamentos.modeladominio;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Restaurante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private @NotBlank String nome;
	@ElementCollection
	private @Size(min = 1) Set<FormaPagamento> formasPagamento = new HashSet<>();

	public Restaurante(@NotBlank String nome,
			@Size(min = 1) FormaPagamento... formasPagamento) {
		this.nome = nome;
		Stream.of(formasPagamento).forEach(this.formasPagamento :: add);
	}

}
