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
public class NovoPagamentoOnlineController {

	@Autowired
	private ExecutaTransacao executaTransacao;
	@Autowired
	private Gateways gateways;

	@Autowired
	private NovoPagamentoOnlineValidator novoPagamentoOnlineValidator;
	@Autowired
	private IniciaPagamento iniciaPagamento;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(novoPagamentoOnlineValidator);
	}

	@PostMapping(value = "/pagamento/online/{idPedido}")
	public void paga(@PathVariable("idPedido") Long idPedido,
			@RequestBody @Valid NovoPagamentoOnlineRequest request)
			throws Exception {
		Pagamento novoPagamentoSalvo = iniciaPagamento.executa(idPedido,
				request).get();

		List<Transacao> transacoesGeradas = gateways
				.processa(novoPagamentoSalvo);
		
		executaTransacao.executa(() -> {
			novoPagamentoSalvo.adicionaTransacao(transacoesGeradas);
			return null;
		});

	}

}
