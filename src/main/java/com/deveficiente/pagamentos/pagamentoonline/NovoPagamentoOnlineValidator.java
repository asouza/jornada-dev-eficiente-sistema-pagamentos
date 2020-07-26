package com.deveficiente.pagamentos.pagamentoonline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.deveficiente.pagamentos.pagamentooffline.CombinacaoRestauranteUsuarioFormaPagamentoValidator;
import com.deveficiente.pagamentos.pagamentooffline.PagamentoGeradoValidator;

@Component
public class NovoPagamentoOnlineValidator implements Validator{
	
	//1
	@Autowired
	private CombinacaoRestauranteUsuarioFormaPagamentoValidator combinacaoUsuarioRestauranteValidator;
	@Autowired
	//1
	private PagamentoGeradoValidator pagamentoGeradoValidtor;

	@Override
	public boolean supports(Class<?> clazz) {
		return NovoPagamentoOnlineRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors()) {
			return;
		}
		pagamentoGeradoValidtor.validate(target,errors);
		combinacaoUsuarioRestauranteValidator.validate(target,errors);
		new FormaPagamentoOnlineValidator().validate(target,errors);		
	}

}
