package com.deveficiente.pagamentos.modeladominio;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import com.deveficiente.pagamentos.listapagamentos.RegraFraude;

@Entity
public class CombinacaoUsuarioRestaurante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Valid
	@ManyToOne
	private Usuario usuario;
	@NotNull
	@Valid
	@ManyToOne
	private Restaurante restaurante;
	
	@Deprecated
	public CombinacaoUsuarioRestaurante() {

	}

	public CombinacaoUsuarioRestaurante(@NotNull @Valid Usuario usuario,
			@NotNull @Valid Restaurante restaurante) {
		this.usuario = usuario;
		this.restaurante = restaurante;
	}

	public boolean mesmoRestaurante(Restaurante restaurante) {
		return this.restaurante.equals(restaurante);
	}

	public Set<FormaPagamento> selecionaFormasPagamento(
			Collection<RegraFraude> regrasFraude) {
		return usuario.filtraFormasPagamento(restaurante, regrasFraude);
	}

	public Long getUsuarioId() {
		Assert.isTrue(usuario.getId().isPresent(),"Neste ponto o usuario precisa de id");
		return usuario.getId().get();
	}

	public Long getRestauranteId() {
		Assert.isTrue(restaurante.getId().isPresent(),"Neste ponto o restaurante precisa de id");
		return restaurante.getId().get();
	}

}
