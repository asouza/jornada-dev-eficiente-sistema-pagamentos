package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//9
//8
//7
public class PagamentoOfflineController {


	@PersistenceContext
	private EntityManager manager;
	@Autowired
	//1
	private ExecutaTransacao executaTransacao;
	@Autowired
	//1
	private ObtemValorPedido obtemValorPedido;
	@Autowired
	//1
	private NovoPedidoOfflineRequestValidator novoPedidoOfflineRequestValidator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(novoPedidoOfflineRequestValidator);
	}

	@PostMapping(value = "/pagamento/offline/{idPedido}")
	public String paga(@PathVariable Long idPedido,
			//1
			@RequestBody @Valid NovoPedidoOfflineRequest request) throws Exception {

		//1
		BigDecimal valor = obtemValorPedido.executa(idPedido,() -> {
			BindException bindException = new BindException("","");
			bindException.reject(null,"Olha, esse id de pedido não existe");
			return bindException;
		});
		
		//2
		return executaTransacao.executa(() -> {
			Pagamento novoPagamentoOffline = request.toPagamento(idPedido,valor,manager);
			manager.persist(novoPagamentoOffline);
			return novoPagamentoOffline.getCodigo();
		});

	}

}
