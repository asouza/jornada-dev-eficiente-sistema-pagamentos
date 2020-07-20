package com.deveficiente.pagamentos.pagamentoonline;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FormaPagamentoOnlineValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return NovoPagamentoOnlineRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors()) {
			return ;
		}
		
		NovoPagamentoOnlineRequest request = (NovoPagamentoOnlineRequest) target;
		if(!request.pagamentoOnline()) {
			errors.rejectValue("formaPagamento",null, "Apenas pagamento online");
		}
	}

}
