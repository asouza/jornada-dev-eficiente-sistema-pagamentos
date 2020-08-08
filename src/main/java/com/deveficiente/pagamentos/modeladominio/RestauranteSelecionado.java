package com.deveficiente.pagamentos.modeladominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class RestauranteSelecionado {

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
	public RestauranteSelecionado() {

	}

	public RestauranteSelecionado(@NotNull @Valid Usuario usuario,
			@NotNull @Valid Restaurante restaurante) {
		this.usuario = usuario;
		this.restaurante = restaurante;
	}

	public boolean mesmoRestaurante(Restaurante restaurante) {
		return this.restaurante.equals(restaurante);
	}

}
