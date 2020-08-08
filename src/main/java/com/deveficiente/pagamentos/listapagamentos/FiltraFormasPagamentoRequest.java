package com.deveficiente.pagamentos.listapagamentos;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.CombinacaoUsuarioRestaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

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

	public CombinacaoUsuarioRestaurante toModel(EntityManager manager) {
		// 1
		Usuario usuario = manager.find(Usuario.class, this.idUsuario);
		// 1
		Restaurante restaurante = manager.find(Restaurante.class,
				this.idRestaurante);
		
		return new CombinacaoUsuarioRestaurante(usuario, restaurante);
	}

}
