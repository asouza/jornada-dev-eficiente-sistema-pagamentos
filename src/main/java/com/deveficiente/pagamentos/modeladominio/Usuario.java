package com.deveficiente.pagamentos.modeladominio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import com.deveficiente.pagamentos.listapagamentos.RegraFraude;

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
	// 1
	private Set<FormaPagamento> formasPagamento = new HashSet<>();
	@ElementCollection
	private List<Restaurante> selecoes = new ArrayList<>();

	@Deprecated
	public Usuario() {

	}

	public Usuario(@NotBlank @Email String email,
			@Size(min = 1) FormaPagamento... formasPagamento) {
		Assert.hasText(email, "O email é obrigatório");
		Assert.notNull(formasPagamento,
				"Formas de pagamento não pode ser nulo");
		Assert.isTrue(formasPagamento.length > 0,
				"Precisa de pelo menos uma forma de pagamento");

		this.email = email;
		//
		Stream.of(formasPagamento).forEach(this.formasPagamento::add);
	}

	public Set<FormaPagamento> filtraFormasPagamento(
			@NotNull @Valid Restaurante restaurante,
			Collection<RegraFraude> regrasFraude) {
		return this.formasPagamento.stream()
				// 1
				.filter(restaurante::aceita)
				// 1
				.filter(formaPagamento -> {
					// 1
					return regrasFraude.stream().allMatch(
							regra -> regra.aceita(formaPagamento, this));
				}).collect(Collectors.toSet());
	}

	public String getEmail() {
		return email;
	}

	public boolean podePagar(Restaurante restaurante,
			FormaPagamento formaPagamento,
			Collection<RegraFraude> regrasFraude) {
		return filtraFormasPagamento(restaurante, regrasFraude)
				.contains(formaPagamento);
	}

	public void registraSelecao(Restaurante restaurante) {
		this.selecoes.add(restaurante);
	}

}
