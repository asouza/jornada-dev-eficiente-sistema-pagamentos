package com.deveficiente.pagamentos.pagamentoonline;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentoonline.gateways.Gateway;

@Service
public class GatewaysOrdenadosPorCusto {

	private Set<Gateway> gateways;

	public GatewaysOrdenadosPorCusto(Set<Gateway> gateways) {
		super();
		this.gateways = gateways;
	}

	public List<Gateway> ordena(Pagamento pagamento) {
		return gateways.stream()
				// 1
				.filter(gateway -> gateway.aceita(pagamento))
				// 1
				.sorted((gateway1, gateway2) -> {
					return gateway1.custo(pagamento)
							.compareTo(gateway2.custo(pagamento));
				}).collect(Collectors.toList());

	}

}
