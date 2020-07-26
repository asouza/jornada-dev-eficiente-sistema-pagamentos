package com.deveficiente.pagamentos.pagamentoofline;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;

public class PagamentoTest {

	private Pagamento pagamento = Pagamento.offline(1l, BigDecimal.TEN,
			FormaPagamento.dinheiro,
			new Usuario("teste@teste.com.br", FormaPagamento.dinheiro),
			new Restaurante("teste", FormaPagamento.dinheiro),
			StatusTransacao.esperando_confirmacao_pagamento);

	@Test
	@DisplayName("deveria verificar que um pagamento ainda nao foi concluido")
	void teste1() {
		Assertions.assertFalse(pagamento.foiConcluido());
	}

	@Test
	@DisplayName("deveria verificar que um pagamento foi concluido")
	void teste2() {
		pagamento.conclui();
		Assertions.assertTrue(pagamento.foiConcluido());
	}

	@Test
	@DisplayName("nao deveria deixar concluir uma compra mais de uma vez")
	void teste3() {
		pagamento.conclui();
		Assertions.assertThrows(IllegalStateException.class, () -> {
			pagamento.conclui();
		});
	}
}
