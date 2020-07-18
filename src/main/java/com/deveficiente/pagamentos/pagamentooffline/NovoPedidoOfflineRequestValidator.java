package com.deveficiente.pagamentos.pagamentooffline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class NovoPedidoOfflineRequestValidator implements Validator {

	@Autowired
	// 1
	private CombinacaoRestauranteUsuarioFormaPagamentoValidator combinacaoRestauranteUsuarioFormaPagamentoValidator;
	@Autowired
	private PagamentoGeradoValidator pagamentoGeradoValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return NovoPedidoOfflineRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (errors.hasErrors()) {
			return;
		}

		combinacaoRestauranteUsuarioFormaPagamentoValidator.validate(target,
				errors);
		pagamentoGeradoValidator.validate(target, errors);
		new FormaPagamentoOfflineValidator().validate(target, errors);

	}

}
