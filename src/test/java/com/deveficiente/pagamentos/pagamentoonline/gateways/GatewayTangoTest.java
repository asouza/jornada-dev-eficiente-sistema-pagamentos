package com.deveficiente.pagamentos.pagamentoonline.gateways;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;

public class GatewayTangoTest {

	private Usuario comprador = new Usuario("a@a.com.br", FormaPagamento.visa);
	private Restaurante restaurante = new Restaurante("a", FormaPagamento.visa);

	@DisplayName("deveria retornar o custo fixo para pagamentos menores que 100")
	@ParameterizedTest
	@ValueSource(strings = { "50,4", "100,4", "200,12" })
	void teste1(String par) throws Exception {
		GatewayTango tango = new GatewayTango();
		String[] valorECusto = par.split(",");
		Pagamento pagamento = Pagamento.cartao(1l,
				new BigDecimal(valorECusto[0]), FormaPagamento.visa,
				"545345344", 123, comprador, restaurante,
				StatusTransacao.esperando_confirmacao_pagamento);

		BigDecimal custo = tango.custo(pagamento).setScale(2,
				RoundingMode.HALF_EVEN);
		Assertions.assertEquals(new BigDecimal(valorECusto[1]).setScale(2,
				RoundingMode.HALF_EVEN), custo);

	}

}
