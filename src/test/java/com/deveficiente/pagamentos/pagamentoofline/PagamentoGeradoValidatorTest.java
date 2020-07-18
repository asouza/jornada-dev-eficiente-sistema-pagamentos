package com.deveficiente.pagamentos.pagamentoofline;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.HandlerMapping;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.pagamentooffline.NovoPedidoOfflineRequest;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.PagamentoGeradoValidator;
import com.deveficiente.pagamentos.pagamentooffline.PagamentoRepository;

@TestInstance(Lifecycle.PER_METHOD)
public class PagamentoGeradoValidatorTest {

	private PagamentoRepository pagamentoRepository = (idPedido) -> {
		if (idPedido.equals(1l)) {
			return Optional.of(Mockito.mock(Pagamento.class));
		}
		return Optional.empty();
	};

	private HttpServletRequest servletRequest = Mockito
			.mock(HttpServletRequest.class);
	private PagamentoGeradoValidator validator = new PagamentoGeradoValidator(
			servletRequest, pagamentoRepository);
	private NovoPedidoOfflineRequest request = new NovoPedidoOfflineRequest(
			FormaPagamento.dinheiro, 1l, 1l);
	
	@Test
	@DisplayName("deveria gerar erro de validacao quando ja existe um pagamento com determinado id de pedido")
	void teste1() {

		Map<String, String> variaveisUrl = Map.of("idPedido", "1");
		Mockito.when(servletRequest
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
				.thenReturn(variaveisUrl);

		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(errors.hasErrors()).thenReturn(false);

		validator.validate(request, errors);

		Mockito.verify(errors).reject(null,
				"Já existe um pagamento iniciado para esse pedido");
	}
	
	@Test
	@DisplayName("deveria aceitar pedidos novos ")
	void teste2() {
		
		Map<String, String> variaveisUrl = Map.of("idPedido", "2");
		Mockito.when(servletRequest
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
		.thenReturn(variaveisUrl);
		
		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(errors.hasErrors()).thenReturn(false);
		
		validator.validate(request, errors);
		
		Mockito.verify(errors,Mockito.never()).reject(null,
				"Já existe um pagamento iniciado para esse pedido");
	}
}
