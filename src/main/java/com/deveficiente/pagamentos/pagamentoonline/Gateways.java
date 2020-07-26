package com.deveficiente.pagamentos.pagamentoonline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.StatusTransacao;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;
import com.deveficiente.pagamentos.pagamentoonline.gateways.Gateway;

//8
@Service
@Validated
public class Gateways {

	// 1
	private GatewaysOrdenadosPorCusto gatewaysOrdenados;

	public Gateways(@NotNull GatewaysOrdenadosPorCusto gatewaysOrdenados) {
		this.gatewaysOrdenados = gatewaysOrdenados;
	}

	/**
	 * 
	 * @param pagamento
	 * @return lista de transacoes geradas enquanto tentava pagar
	 */
	public List<Transacao> processa(@NotNull @Valid Pagamento pagamento) {
		// 1
		ArrayList<Transacao> transacoes = new ArrayList<>();
		// 1
		for (Gateway gateway : gatewaysOrdenados.ordena(pagamento)) {
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

		Assert.state(!transacoes.isEmpty(),
				"Pelo menos um gateway deve ter processado o pagamento");
		return transacoes;
	}

}
