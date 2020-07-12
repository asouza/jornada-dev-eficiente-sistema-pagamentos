package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class PagamentoOfflineController {

	@Autowired
	//1
	private CombinacaoRestauranteUsuarioFormaPagamentoValidator combinacaoRestauranteUsuarioFormaPagamentoValidator;
	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private ExecutaTransacao executaTransacao;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//1
		binder.addValidators(new FormaPagamentoOfflineValidator(),
				combinacaoRestauranteUsuarioFormaPagamentoValidator);
	}

	@PostMapping(value = "/pagamento/offline/{idPedido}")
	public String paga(@PathVariable Long idPedido,
			//1
			@RequestBody @Valid NovoPedidoOfflineRequest request) throws BindException {

		//1
		try {
			RestTemplate restTemplate = new RestTemplate();
			Map<String,Object> pedido = restTemplate.getForObject("http://localhost:8080/api/pedidos/{idPedido}", Map.class, idPedido);
			Number valorPedido =  (Number) pedido.get("valor");
			//1
			 
			Transacao txSalva = executaTransacao.executa(() -> {
				Transacao novaTransacaoOffline = request.toTransacao(idPedido,new BigDecimal(valorPedido.toString()),manager);
				manager.persist(novaTransacaoOffline);
				return novaTransacaoOffline;
			});
			
			return txSalva.getUuid();
		} 	
		//1
		catch (HttpClientErrorException e) {
			//1
			if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				BindException bindException = new BindException("","");
				bindException.reject(null,"Olha, esse id de pedido não existe");
				throw bindException;
			}
			throw e;
		}

	}

}
