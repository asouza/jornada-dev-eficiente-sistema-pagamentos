package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.deveficiente.pagamentos.pagamentooffline.ExecutaTransacao;
import com.deveficiente.pagamentos.pagamentooffline.ObtemValorPedido;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;

/**
 * Esta classe é intimamente acoplada com o controller {@link NovoPagamentoOnlineController}.
 * Ela só surgiu para que pudesse ser quebrada a carga intrínseca de lá
 * de modo a facilitar o entendimento
 * @author albertoluizsouza
 *
 */
@Service
//5
public class IniciaPagamento {
	
	@Autowired
	// 1
	private ObtemValorPedido obtemValorPedido;
	@Autowired
	//1
	private ExecutaTransacao executaTransacao;
	@PersistenceContext
	private EntityManager manager;

	public Pagamento executa(Long idPedido,
			@Valid NovoPagamentoOnlineRequest request) throws BindException {
		ForceSiteCall.fromExactlyPoint(NovoPagamentoOnlineController.class);
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
		
		return novoPagamentoSalvo;
	}

}
