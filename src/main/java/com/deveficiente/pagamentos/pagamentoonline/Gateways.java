package com.deveficiente.pagamentos.pagamentoonline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;
import com.deveficiente.pagamentos.pagamentoonline.gateways.Gateway;

//8
@Service
public class Gateways {

	@Autowired
	// 1
	private Set<Gateway> gateways;

	/**
	 * 
	 * @param pagamento
	 * @return lista de transacoes geradas enquanto tentava pagar
	 */
	public List<Transacao> processa(Pagamento pagamento) {
//		//approach deixando claro no retorno que as coisas podem dar erradas
		List<Gateway> gatewaysOrdenados = gateways.stream()
				// 1
				.filter(gateway -> gateway.aceita(pagamento))
				// 1
				.sorted((gateway1, gateway2) -> {
					return gateway1.custo(pagamento)
							.compareTo(gateway2.custo(pagamento));
				}).collect(Collectors.toList());

		// 1
		ArrayList<Transacao> transacoes = new ArrayList<>();
		// 1
		for (Gateway gateway : gatewaysOrdenados) {
			// 1 resultado
			Resultado<Exception, Transacao> possivelNovaTransacao = gateway
					.processa(pagamento);

			// 1
			if (possivelNovaTransacao.temErro()) {
				Transacao falhou = new Transacao(StatusTransacao.falha);
				falhou.setInfoAdicional(Map.of("gateway", gateway, "exception",
						possivelNovaTransacao.getStackTrace()));
				transacoes.add(falhou);
				// 1
			} else {
				transacoes.add(possivelNovaTransacao.get());
				break;
			}
		}

		Assert.isTrue(!transacoes.isEmpty(),
				"Pelo menos um gateway deve ter processado o pagamento");
		return transacoes;
	}

}
