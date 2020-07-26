package com.deveficiente.pagamentos.modeladominio;

import java.util.stream.Stream;

public enum FormaPagamento {

	visa(true, "cartao visa"), master(true, "cartao master"),
	elo(true, "cartao elo"), hipercard(true, "cartao hiper"),
	maquineta(false, "maquina para passar cartao"),
	dinheiro(false, "dinheiro para pagar o pedidod");

	public final boolean online;
	private String descricao;

	FormaPagamento(boolean online, String descricao) {
		this.online = online;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean pertence(FormaPagamento... grupo) {
		return Stream.of(grupo).anyMatch(forma -> forma.equals(this));
	}
}
