package com.deveficiente.pagamentos.pagamentoonline;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.deveficiente.pagamentos.pagamentooffline.ExecutaTransacao;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;

@RestController
//15
//11
//9
//7
public class NovoPagamentoOnlineController {

	@Autowired
	// 1
	private ExecutaTransacao executaTransacao;
	@Autowired
	// 1
	private Gateways gateways;

	@Autowired
	// 1
	private NovoPagamentoOnlineValidator novoPagamentoOnlineValidator;
	@Autowired
	//1
	private IniciaPagamento iniciaPagamento;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(novoPagamentoOnlineValidator);
	}

	@PostMapping(value = "/pagamento/online/{idPedido}")
	public void paga(@PathVariable("idPedido") Long idPedido,
			// 1
			@RequestBody @Valid NovoPagamentoOnlineRequest request)
			throws Exception {
		/**
		 * verifica as restricoes
		 * 
		 * gera um pagamento inicial
		 * 
		 * filtra os gateways que aceitam determinada forma de pagamento
		 * 
		 * ordena pelo custo da transacao
		 * 
		 * tenta pagar utilizando os gateways
		 * 
		 * cada transacao precisa ficar salva no pagamento que estamos tentando
		 * gerar
		 * 
		 * salva o pagamento
		 */

		//1
		Pagamento novoPagamentoSalvo = iniciaPagamento.executa(idPedido,
				request);
		// aqui para baixo tem 8 pontos

		/*
		 * versao 1 => diminuiria 8 pontos, considerando a referência ao gateway
		 * resultado = 7 pontos Transacao novaTransacao =
		 * gateways.paga(novoPagamentoSalvo); executaTransacao.executa(() ->
		 * pagamento.adicionaTransacao(novaTransacao));
		 */

		List<Transacao> transacoesGeradas = gateways
				.processa(novoPagamentoSalvo);
		// 1
		executaTransacao.executa(() -> {
			novoPagamentoSalvo.adicionaTransacao(transacoesGeradas);
			return null;
		});

	}

}
