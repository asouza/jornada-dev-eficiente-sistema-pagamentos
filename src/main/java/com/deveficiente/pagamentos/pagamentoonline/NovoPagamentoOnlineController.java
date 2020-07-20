package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;

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
import com.deveficiente.pagamentos.pagamentooffline.ObtemValorPedido;

@RestController
public class NovoPagamentoOnlineController {
	
	@Autowired
	private CombinacaoRestauranteUsuarioFormaPagamentoValidator combinacaoUsuarioRestauranteValidator;
	@Autowired
	//1
	private ObtemValorPedido obtemValorPedido;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(combinacaoUsuarioRestauranteValidator,new FormaPagamentoOnlineValidator());
	}

	@PostMapping(value = "/pagamento/online/{idPedido}")
	public void paga(@PathVariable("idPedido") Long idPedido ,@RequestBody @Valid NovoPagamentoOnlineRequest request) throws Exception {
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
		
		BigDecimal valor = obtemValorPedido.executa(idPedido,() -> {
			BindException bindException = new BindException("","");
			bindException.reject(null,"Olha, esse id de pedido não existe");
			return bindException;
		});
		
		
	}

}
