package com.deveficiente.pagamentos.conclusaocompraoffline;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.PagamentoRepository;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;

public class ConcluiaCompraOfflineControllerTest {

	private PagamentoRepository pagamentoRepository = Mockito
			.mock(PagamentoRepository.class);
	private ConcluiaCompraOfflineController controller = new ConcluiaCompraOfflineController(
			pagamentoRepository);
	private Pagamento pagamento = new Pagamento(1l, BigDecimal.TEN,
			new Usuario("teste@teste.com.br", FormaPagamento.dinheiro),
			new Restaurante("teste", FormaPagamento.dinheiro),
			StatusTransacao.esperando_confirmacao_pagamento);

	@Test
	@DisplayName("deveria retornar 404 para um pagamento que nao existe")
	void teste1() throws Exception {
		Mockito.when(pagamentoRepository.findByCodigo("123456"))
				.thenReturn(Optional.empty());
		try {
			controller.conclui("123456");
			Assertions.fail();
		} catch (ResponseStatusException e) {
			Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		}
	}

	@Test
	@DisplayName("deveria retornar 400 para um pagamento que ja foi concluido")
	void teste2() throws Exception {

		pagamento.conclui();
		Mockito.when(pagamentoRepository.findByCodigo("1234567"))
				.thenReturn(Optional.of(pagamento));

		try {			
			controller.conclui("1234567");
			Assertions.fail();
		} catch (ResponseStatusException e) {
			Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		}
	}

	@Test
	@DisplayName("deveria concluir uma compra com sucesso")
	void teste3() throws Exception {
		Pagamento pagamentoObservavel = Mockito.spy(pagamento);
		Mockito.when(pagamentoRepository.findByCodigo("12345678"))
				.thenReturn(Optional.of(pagamentoObservavel));

		controller.conclui("12345678");
		
		Mockito.verify(pagamentoObservavel).conclui();
	}
}
