package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
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
public class NovoPagamentoOnlineController {

	@Autowired
	private CombinacaoRestauranteUsuarioFormaPagamentoValidator combinacaoUsuarioRestauranteValidator;
	@Autowired
	// 1
	private ObtemValorPedido obtemValorPedido;
	@Autowired
	private EntityManager manager;
	@Autowired
	private ExecutaTransacao executaTransacao;
	@Autowired
	private SortedSet<Gateway> gateways;
	@Autowired
	private PagamentoGeradoValidator pagamentoGeradoValidtor;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(pagamentoGeradoValidtor,
				combinacaoUsuarioRestauranteValidator,
				new FormaPagamentoOnlineValidator());
	}

	@PostMapping(value = "/pagamento/online/{idPedido}")
	public void paga(@PathVariable("idPedido") Long idPedido,
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

		BigDecimal valor = obtemValorPedido.executa(idPedido, () -> {
			BindException bindException = new BindException("", "");
			bindException.reject(null, "Olha, esse id de pedido não existe");
			return bindException;
		});

		Pagamento novoPagamentoSalvo = executaTransacao.executa(() -> {
			Pagamento novoPagamento = request.toPagamento(idPedido, valor,
					manager);
			manager.persist(novoPagamento);
			return novoPagamento;
		});

//		//approach deixando claro no retorno que as coisas podem dar erradas
		List<Gateway> gatewaysOrdenados = gateways.stream()
				.filter(gateway -> gateway.aceita(novoPagamentoSalvo))
				.collect(Collectors.toList());

		for (Gateway gateway : gatewaysOrdenados) {
			Resultado<Exception, Transacao> possivelNovaTransacao = gateway
					.processa(novoPagamentoSalvo);

			if (possivelNovaTransacao.temErro()) {
				Transacao falhou = new Transacao(StatusTransacao.falha);
				falhou.setInfoAdicional(Map.of("gateway", gateway, "exception",
						possivelNovaTransacao.getStackTrace()));
			} else {
				executaTransacao.executa(() -> {
					novoPagamentoSalvo
							.adicionaTransacao(possivelNovaTransacao.get());
					return null;
				});
				break;
			}
		}

	}

}
