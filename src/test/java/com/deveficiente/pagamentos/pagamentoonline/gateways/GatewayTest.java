package com.deveficiente.pagamentos.pagamentoonline.gateways;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;
import com.deveficiente.pagamentos.pagamentoonline.RequestsGateways;

public class GatewayTest {

	private Usuario comprador = new Usuario("a@a.com.br", FormaPagamento.visa);
	private Restaurante restaurante = new Restaurante("a", FormaPagamento.visa);
	private RequestsGateways requestsGateways = Mockito
			.mock(RequestsGateways.class);
	private GatewaySaori gatewaySaori = new GatewaySaori(requestsGateways);

	@Test
	@DisplayName("nao deveria ter chamada para o aceita se o pagamento ja foi concluido")
	void teste1() throws Exception {
		Pagamento pagamento = Pagamento.cartao(1l, new BigDecimal("100"),
				FormaPagamento.visa, "545345344", 123, comprador, restaurante,
				StatusTransacao.esperando_confirmacao_pagamento);
		pagamento.conclui();

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gatewaySaori.aceita(pagamento);
		});
	}

	@Test
	@DisplayName("deveria chamar o aceita específico para pagametos nao concluidos")
	void teste2() throws Exception {
		Pagamento pagamento = Pagamento.cartao(1l, new BigDecimal("100"),
				FormaPagamento.visa, "545345344", 123, comprador, restaurante,
				StatusTransacao.esperando_confirmacao_pagamento);

		Assertions.assertTrue(gatewaySaori.aceita(pagamento));
	}
}
