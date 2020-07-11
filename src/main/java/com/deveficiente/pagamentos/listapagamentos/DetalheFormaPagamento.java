package com.deveficiente.pagamentos.listapagamentos;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;

public class DetalheFormaPagamento {

	private String descricao;
	private String id;

	public DetalheFormaPagamento(FormaPagamento formaPagamento) {
		this.id = formaPagamento.name();
		this.descricao = formaPagamento.getDescricao();
	}
	
	public String getId() {
		return id;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
