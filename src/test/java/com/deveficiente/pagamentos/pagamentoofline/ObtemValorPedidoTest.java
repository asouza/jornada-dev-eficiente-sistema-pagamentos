package com.deveficiente.pagamentos.pagamentoofline;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.deveficiente.pagamentos.pagamentooffline.ObtemValorPedido;

public class ObtemValorPedidoTest {

	private RestTemplate restTemplate = new RestTemplate() {
		@Override
		public <T> T getForObject(String url, Class<T> responseType,
				Object... uriVariables) throws RestClientException {
			if (url.equals("http://pedidos.com/{idPedido}")
					&& uriVariables[0].equals(1l)) {
				return (T) Map.of("valor", 50.57);
			}

			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
	};

	@Test
	@DisplayName("se o id de pedido existe, retorna o valor")
	public void teste1() throws Exception {
		ObtemValorPedido obtemValorPedido = new ObtemValorPedido(restTemplate,
				"http://pedidos.com/{idPedido}");
		BigDecimal valor = obtemValorPedido.executa(1l, () -> {
			return new Exception();
		});

		Assertions.assertEquals(new BigDecimal("50.57"),
				valor.setScale(2, RoundingMode.HALF_EVEN));
	}

	@Test
	@DisplayName("se o id de pedido nao existe, executa a funcao")
	public void teste2() throws Exception {
		ObtemValorPedido obtemValorPedido = new ObtemValorPedido(restTemplate,
				"http://pedidos.com/{idPedido}");
		
		Assertions.assertThrows(BindException.class, () -> {
			obtemValorPedido.executa(2l, () -> {
				return new BindException(new Object(), "");
			});
		});
	}
}
