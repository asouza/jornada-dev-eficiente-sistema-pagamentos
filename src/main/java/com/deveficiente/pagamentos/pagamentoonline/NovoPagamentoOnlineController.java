package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;

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

@RestController
public class NovoPagamentoOnlineController {
	
	@Autowired
	private CombinacaoRestauranteUsuarioFormaPagamentoValidator combinacaoUsuarioRestauranteValidator;
	@Autowired
	//1
	private ObtemValorPedido obtemValorPedido;
	@Autowired
	private EntityManager manager;
	@Autowired
	private ExecutaTransacao executaTransacao;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(combinacaoUsuarioRestauranteValidator,new FormaPagamentoOnlineValidator());
	}

	@PostMapping(value = "/pagamento/online/{idPedido}")
	public void paga(@PathVariable("idPedido") Long idPedido ,@RequestBody @Valid NovoPagamentoOnlineRequest request) throws Exception {
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
		 * cada precisa ficar salva no pagamento que estamos tentando gerar
		 * 
		 * salva o pagamento
		 */
		
		BigDecimal valor = obtemValorPedido.executa(idPedido,() -> {
			BindException bindException = new BindException("","");
			bindException.reject(null,"Olha, esse id de pedido não existe");
			return bindException;
		});
				
		Pagamento novoPagamentoSalvo = executaTransacao.executa(() -> {
			Pagamento novoPagamento = request.toPagamento(idPedido,valor,manager);
			manager.persist(novoPagamento);
			return novoPagamento;
		});
		
		//approach super me defendendo
//		gateways.stream()
//			.filter(gateway -> gateway.aceita(tentativaPagamento))
//			.order(gateway -> gateway.custo(tentativaPagamento))
//			.map(gateway -> {
//				try {
//				 pagamento.adicionaTransacao(gateway.paga(tentativaPagamento));
//				} catch(Exception e) {
//				 Transacao falhou = new Transacao(StatusTransacao.falha);
//				 falhou.setInfoAdicional(Map.of("gateway",gateway,"exception",Arrays.toString(e.getStackTrace())));
//				 pagamento.adicionaTransacao(falhou);	
//				}
//			});
//		
//				
//		//approach distribuindo a defesa pelos gateways
//		gateways.stream()
//		.filter(gateway -> gateway.aceita(tentativaPagamento))
//		.order(gateway -> gateway.custo(tentativaPagamento))
//		.map(gateway -> {
//			try {
//			 pagamento.adicionaTransacao(gateway.paga(tentativaPagamento));
//			} catch(Exception e) {
//				//o sistema aqui está num estado super inválido
//				throw new AssertionError(e);
//
//			}
//		});				
	}

}
