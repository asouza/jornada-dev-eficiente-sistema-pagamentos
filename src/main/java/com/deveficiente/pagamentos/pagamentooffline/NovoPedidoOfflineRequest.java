package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import com.deveficiente.pagamentos.compartilhado.ExistsId;
import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

public class NovoPedidoOfflineRequest {

	@NotNull
	private FormaPagamento formaPagamento;
	@NotNull
	@ExistsId(domainClass = Restaurante.class, fieldName = "id")
	private Long idRestaurante;
	@NotNull
	@ExistsId(domainClass = Usuario.class, fieldName = "id")
	private Long idUsuario;

	public NovoPedidoOfflineRequest(@NotNull FormaPagamento formaPagamento,
			@NotNull Long idRestaurante, @NotNull Long idUsuario) {
		super();
		this.formaPagamento = formaPagamento;
		this.idRestaurante = idRestaurante;
		this.idUsuario = idUsuario;
	}
	
	public Long getIdRestaurante() {
		return idRestaurante;
	}
	
	public Long getIdUsuario() {
		return idUsuario;
	}
	
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public boolean isOffline() {
		return !formaPagamento.online;
	}

	public Transacao toTransacao(BigDecimal valor,EntityManager manager) {
		Usuario usuario = manager.find(Usuario.class, idUsuario);		
		Restaurante restaurante = manager.find(Restaurante.class, idRestaurante);
		
		return new Transacao(valor,usuario,restaurante,StatusTransacao.esperando_confirmacao_pagamento);
	}

}
