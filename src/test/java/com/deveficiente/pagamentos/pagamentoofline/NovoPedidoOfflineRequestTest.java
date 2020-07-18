package com.deveficiente.pagamentos.pagamentoofline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.pagamentooffline.NovoPedidoOfflineRequest;

public class NovoPedidoOfflineRequestTest {

	@Test
	@DisplayName("deveria verificar se um novo pedido é offline")
	void teste1() {
		NovoPedidoOfflineRequest request = new NovoPedidoOfflineRequest(
				FormaPagamento.dinheiro, 1l, 1l);
		Assertions.assertTrue(request.isOffline());
	}
	
	@Test
	@DisplayName("deveria verificar se um novo pedido não é offline")
	void teste2() {
		NovoPedidoOfflineRequest request = new NovoPedidoOfflineRequest(
				FormaPagamento.hipercard, 1l, 1l);
		Assertions.assertFalse(request.isOffline());
	}
}
