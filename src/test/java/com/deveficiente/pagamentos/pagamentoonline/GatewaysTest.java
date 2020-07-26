package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.outrossistemas.DadosCartaoSeyaRequest;
import com.deveficiente.pagamentos.outrossistemas.DadosCompraGenerico;
import com.deveficiente.pagamentos.outrossistemas.DadosCompraSeyaRequest;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;
import com.deveficiente.pagamentos.pagamentoonline.gateways.Gateway;
import com.deveficiente.pagamentos.pagamentoonline.gateways.GatewaySaori;
import com.deveficiente.pagamentos.pagamentoonline.gateways.GatewaySeya;

public class GatewaysTest {
	private Usuario comprador = new Usuario("a@a.com.br", FormaPagamento.visa);
	private Restaurante restaurante = new Restaurante("a", FormaPagamento.visa);
	private Pagamento pagamento = Pagamento.cartao(1l, new BigDecimal("50"),
			FormaPagamento.visa, "545345344", 123, comprador, restaurante,
			StatusTransacao.esperando_confirmacao_pagamento);

	private GatewaysOrdenadosPorCusto gatewaysOrdenados = Mockito
			.mock(GatewaysOrdenadosPorCusto.class);
	final DadosCompraGenerico dadosCompra = new DadosCompraGenerico(pagamento);

	RequestsGateways requests = new RequestsGateways() {

		@Override
		public void tangoProcessa(DadosCompraGenerico request) {
			// TODO Auto-generated method stub

		}

		@Override
		public int seyaVerifica(DadosCartaoSeyaRequest request) {
			Assertions.fail();
			return 0;
		}

		@Override
		public void seyaProcessa(Integer codigo,
				DadosCompraSeyaRequest request) {
			Assertions.fail();
		}

		@Override
		public void saoriProcessa(DadosCompraGenerico request) {
			Assertions.assertEquals(dadosCompra, request);
		}
	};

	private Gateway gatewaySeyaQueNuncaDeveSerChamado = new GatewaySeya(requests);
	private Gateway gatewaySaori = new GatewaySaori(requests);
	private Gateway gatewaySaoriComErro = new GatewaySaori(requests) {
		public com.deveficiente.pagamentos.pagamentoonline.Resultado<Exception, Transacao> processaEspecifico(
				Pagamento pagamento) {
			return Resultado.erro(new Exception());
		};
	};

	@Test
	@DisplayName("deveria adicionar apenas uma transacao de sucesso")
	void teste1() {
		Mockito.when(gatewaysOrdenados.ordena(pagamento))
				.thenReturn(List.of(gatewaySaori));
		List<Transacao> transacoes = new Gateways(gatewaysOrdenados)
				.processa(pagamento);

		Assertions.assertEquals(1, transacoes.size());
		Assertions.assertTrue(
				transacoes.get(0).temStatus(StatusTransacao.concluida));

	}
	
	@Test
	@DisplayName("mesmo que dois gateways possam processar, deve parar no primeiro")
	void teste5() {
		Mockito.when(gatewaysOrdenados.ordena(pagamento))
		.thenReturn(List.of(gatewaySaori,gatewaySeyaQueNuncaDeveSerChamado));
		List<Transacao> transacoes = new Gateways(gatewaysOrdenados)
				.processa(pagamento);
		
		Assertions.assertEquals(1, transacoes.size());
		Assertions.assertTrue(
				transacoes.get(0).temStatus(StatusTransacao.concluida));
		
	}

	@Test
	@DisplayName("deveria gerar uma transacao que falhou em caso de falha do gateway")
	void teste2() {
		Mockito.when(gatewaysOrdenados.ordena(pagamento))
				.thenReturn(List.of(gatewaySaoriComErro));
		List<Transacao> transacoes = new Gateways(gatewaysOrdenados)
				.processa(pagamento);

		Assertions.assertEquals(1, transacoes.size());
		Assertions
				.assertTrue(transacoes.get(0).temStatus(StatusTransacao.falha));
		// neste momento ta rolando o ignore do setInformacao

	}
	
	@Test
	@DisplayName("deveria suportar uma falha de processamento")
	void teste3() {
		Mockito.when(gatewaysOrdenados.ordena(pagamento))
		.thenReturn(List.of(gatewaySaoriComErro,gatewaySaori));
		
		List<Transacao> transacoes = new Gateways(gatewaysOrdenados)
				.processa(pagamento);
		
		Assertions.assertEquals(2, transacoes.size());
		Assertions
		.assertTrue(transacoes.get(0).temStatus(StatusTransacao.falha));
		Assertions
		.assertTrue(transacoes.get(1).temStatus(StatusTransacao.concluida));
		
	}

	@Test
	@DisplayName("sempre tem que processar no mínimo uma transacao")
	void teste4() {
		Mockito.when(gatewaysOrdenados.ordena(pagamento)).thenReturn(List.of());
		Assertions.assertThrows(IllegalStateException.class, () -> {
			new Gateways(gatewaysOrdenados).processa(pagamento);
		});

	}
}
