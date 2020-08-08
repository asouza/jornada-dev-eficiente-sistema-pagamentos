package com.deveficiente.pagamentos.modeladominio;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Restaurante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private @NotBlank String nome;
	@ElementCollection
	private @Size(min = 1) Set<FormaPagamento> formasPagamento = new HashSet<>();
	
	@Deprecated
	public Restaurante() {

	}

	public Restaurante(@NotBlank String nome,
			@Size(min = 1) FormaPagamento... formasPagamento) {
		this.nome = nome;
		Stream.of(formasPagamento).forEach(this.formasPagamento :: add);
	}
	
	public boolean aceita(@NotNull FormaPagamento formaPagamento) {
		return this.formasPagamento.contains(formaPagamento);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurante other = (Restaurante) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public Optional<Long> getId() {
		return Optional.ofNullable(id);
	}
	
	

}
