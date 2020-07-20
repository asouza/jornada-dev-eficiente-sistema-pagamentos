package com.deveficiente.pagamentos.pagamentoonline;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;

public interface TemCombinacaoUsuarioRestauranteFormaPagamento {

	public Long getIdRestaurante();
	
	public Long getIdUsuario();
	
	public FormaPagamento getFormaPagamento();
}
