package com.deveficiente.pagamentos.listapagamentos;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Usuario;

public class RegraUsuarioEmailFraudulentoTest {

	private @NotNull @Valid Usuario usuario = new Usuario("a@a.com",
			FormaPagamento.elo);

	@Test
	@DisplayName("deveria aceitar todo pagamento offline")
	void teste1() {
		RegraUsuarioEmailFraudulento regra = new RegraUsuarioEmailFraudulento();
		boolean aceite = regra.aceita(FormaPagamento.dinheiro, usuario);
		Assertions.assertTrue(aceite);
	}

	@Test
	@DisplayName("deveria aceitar todo pagamento online para usuarios diferentes de shun")
	void teste2() {
		RegraUsuarioEmailFraudulento regra = new RegraUsuarioEmailFraudulento();
		boolean aceite = regra.aceita(FormaPagamento.elo, usuario);
		Assertions.assertTrue(aceite);
	}

	@Test
	@DisplayName("deveria bloquear pagamento online de shun")
	void teste3() {
		@NotNull
		@Valid
		Usuario shun = new Usuario("shun@deveficiente.com",
				FormaPagamento.elo);
		
		RegraUsuarioEmailFraudulento regra = new RegraUsuarioEmailFraudulento();
		boolean aceite = regra.aceita(FormaPagamento.elo, shun);
		Assertions.assertFalse(aceite);
	}

}
