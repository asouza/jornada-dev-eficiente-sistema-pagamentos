package com.deveficiente.pagamentos.pagamentoofline;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.pagamentooffline.FormaPagamentoOfflineValidator;
import com.deveficiente.pagamentos.pagamentooffline.NovoPedidoOfflineRequest;

public class FormaPagamentoOfflineValidatorTest {

	@Test
	@DisplayName("se o pedido não é offline então rejeita")
	public void teste1() {
		NovoPedidoOfflineRequest request = new NovoPedidoOfflineRequest(
				FormaPagamento.elo, 1l, 1l);
		Errors errors = Mockito.mock(Errors.class);

		FormaPagamentoOfflineValidator validator = new FormaPagamentoOfflineValidator();
		validator.validate(request, errors);

		Mockito.verify(errors).rejectValue("formaPagamento", null,
				"A forma de pagamento deve ser offline");
	}
	
	@Test
	@DisplayName("valida pedidos offline")
	public void teste2() {
		FormaPagamentoOfflineValidator validator = new FormaPagamentoOfflineValidator();
		NovoPedidoOfflineRequest request = new NovoPedidoOfflineRequest(
				FormaPagamento.dinheiro, 1l, 1l);
		Errors errors = Mockito.mock(Errors.class);
		
		validator.validate(request, errors);
		
		Mockito.verify(errors,Mockito.never()).rejectValue("formaPagamento", null,
				"A forma de pagamento deve ser offline");
	}
}
