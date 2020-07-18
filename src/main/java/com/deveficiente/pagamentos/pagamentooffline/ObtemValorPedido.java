package com.deveficiente.pagamentos.pagamentooffline;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Supplier;

import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ObtemValorPedido {

	private RestTemplate restTemplate;
	private String endpointValorPedido;

	public ObtemValorPedido(RestTemplate restTemplate,
			@Value("${enderecos-externos.valor-pedido}") @URL String endpointValorPedido) {
		super();
		this.restTemplate = restTemplate;
		this.endpointValorPedido = endpointValorPedido;
	}

	public BigDecimal executa(Long idPedido,
			Supplier<Exception> codigoEmCasoPedidoNaoExistente)
			throws Exception {
		// 1
		try {
			Map<String, Object> pedido = restTemplate
					.getForObject(endpointValorPedido, Map.class, idPedido);
			Number valorPedido = (Number) pedido.get("valor");
			
			return new BigDecimal(valorPedido.doubleValue());
		} catch (HttpClientErrorException e) {
			// 1
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				throw codigoEmCasoPedidoNaoExistente.get();
			}
			throw e;
		}
	}

}
