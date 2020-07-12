package com.deveficiente.pagamentos.pagamentooffline;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FormaPagamentoOfflineValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return NovoPedidoOfflineRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NovoPedidoOfflineRequest request = (NovoPedidoOfflineRequest) target;
		if(!request.isOffline()) {
			errors.rejectValue("formaPagamento",null, "A forma de pagamento deve ser offline");
		}
	}

}
