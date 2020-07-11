package com.deveficiente.pagamentos.modeladominio;

public enum FormaPagamento {

	visa(true, "cartao visa"), master(true, "cartao master"),
	elo(true, "cartao elo"), hipercard(true, "cartao hiper"),
	maquineta(false, "maquina para passar cartao"),
	dinheiro(false, "dinheiro para pagar o pedidod");

	private boolean online;
	private String descricao;

	FormaPagamento(boolean online, String descricao) {
		this.online = online;
		this.descricao = descricao;
	}
}
