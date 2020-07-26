package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.deveficiente.pagamentos.pagamentooffline.CombinacaoRestauranteUsuarioFormaPagamentoValidator;
import com.deveficiente.pagamentos.pagamentooffline.ExecutaTransacao;
import com.deveficiente.pagamentos.pagamentooffline.ObtemValorPedido;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.PagamentoGeradoValidator;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;

@RestController
//15
public class NovoPagamentoOnlineController {

	//1
	@Autowired
	private CombinacaoRestauranteUsuarioFormaPagamentoValidator combinacaoUsuarioRestauranteValidator;
	@Autowired
	// 1
	private ObtemValorPedido obtemValorPedido;
	@Autowired
	private EntityManager manager;
	@Autowired
	//1
	private ExecutaTransacao executaTransacao;
	@Autowired
	//1
	private Gateways gateways;
	@Autowired
	//1
	private PagamentoGeradoValidator pagamentoGeradoValidtor;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(pagamentoGeradoValidtor,
				combinacaoUsuarioRestauranteValidator,
				new FormaPagamentoOnlineValidator());
	}

	@PostMapping(value = "/pagamento/online/{idPedido}")
	public void paga(@PathVariable("idPedido") Long idPedido,
			//1
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
		BigDecimal valor = obtemValorPedido.executa(idPedido, () -> {
			BindException bindException = new BindException("", "");
			bindException.reject(null, "Olha, esse id de pedido não existe");
			return bindException;
		});

		//2 pagamento + funcao como argumento
		Pagamento novoPagamentoSalvo = executaTransacao.executa(() -> {
			Pagamento novoPagamento = request.toPagamento(idPedido, valor,
					manager);
			manager.persist(novoPagamento);
			return novoPagamento;
		});
		
		// aqui para baixo tem 8 pontos
		
		/*
		 * versao 1 => diminuiria 8 pontos, considerando a referência ao gateway 
		 * resultado = 7 pontos
		 * Transacao novaTransacao = gateways.paga(novoPagamentoSalvo);
		 * executaTransacao.executa(() -> pagamento.adicionaTransacao(novaTransacao));
		 */
		
		List<Transacao> transacoesGeradas = gateways.processa(novoPagamentoSalvo);
		executaTransacao.executa(() -> {
			novoPagamentoSalvo.adicionaTransacao(transacoesGeradas);
			return null;
		});

	}

}
