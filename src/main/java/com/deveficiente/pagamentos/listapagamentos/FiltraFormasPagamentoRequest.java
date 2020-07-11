package com.deveficiente.pagamentos.listapagamentos;

import javax.validation.constraints.NotNull;

public class FiltraFormasPagamentoRequest {

	@NotNull
	private Long idRestaurante;
	@NotNull
	private Long idUsuario;

	public FiltraFormasPagamentoRequest(@NotNull Long idRestaurante,
			@NotNull Long idUsuario) {
		super();
		this.idRestaurante = idRestaurante;
		this.idUsuario = idUsuario;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}
	
	public Long getIdRestaurante() {
		return idRestaurante;
	}

}
