package com.deveficiente.pagamentos.pagamentoonline;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NovoPagamentoOnlineController {

	@PostMapping(value = "/pagamento/online/{idPedido}")
	public void paga(@RequestBody @Valid NovoPagamentoOnlineRequest request) {
		/**
		 * verifica as restricoes
		 * 
		 * filtra os gateways que aceitam determinada forma de pagamento
		 * 
		 * ordena pelo custo da transacao
		 * 
		 * tenta pagar utilizando os gateways
		 * 
		 * cada precisa ficar salva no pagamento que estamos tentando gerar
		 * 
		 * salva o pagamento
		 */
	}

}
