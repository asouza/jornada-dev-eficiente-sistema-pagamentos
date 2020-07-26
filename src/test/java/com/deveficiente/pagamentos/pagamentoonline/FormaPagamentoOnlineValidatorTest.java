package com.deveficiente.pagamentos.pagamentoonline;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;

public class FormaPagamentoOnlineValidatorTest {

	@Test
	@DisplayName("deve aceitar apenas pagamentos online")
	void teste1() {
		FormaPagamentoOnlineValidator validador = new FormaPagamentoOnlineValidator();

		NovoPagamentoOnlineRequest request = new NovoPagamentoOnlineRequest(
				FormaPagamento.elo, 1l, 1l, "546546456547", 123);
		Errors errors = Mockito.mock(Errors.class);
		validador.validate(request, errors);

		/*
		 * Aqui eu deixo a checagem mais aberta, para garantir que o rejectValue
		 * nunca é chamado quando ta certo. Mesmo se mudar a mensagem de erro
		 * 
		 * E a documentação explica que uma vez que usou um matcher na lista de argumentos,
		 * precisa usar para todos argumentos. 
		 */
		Mockito.verify(errors, Mockito.never()).rejectValue(Mockito.eq("formaPagamento"),
				Mockito.any(), Mockito.anyString());
	}
	
	@Test
	@DisplayName("não deve aceitar pagamentos offline")
	void teste2() {
		FormaPagamentoOnlineValidator validador = new FormaPagamentoOnlineValidator();
		
		NovoPagamentoOnlineRequest request = new NovoPagamentoOnlineRequest(
				FormaPagamento.dinheiro, 1l, 1l, "546546456547", 123);
		Errors errors = Mockito.mock(Errors.class);
		validador.validate(request, errors);
		
		//aqui você fecha o escopo do teste, pq aí qualquer troca quebra aqui também.
		//para verificar a chamada seja específico
		//para verificar a não chamada seja mais abrangente
		Mockito.verify(errors).rejectValue("formaPagamento",
				null, "Apenas pagamento online");
	}
}
