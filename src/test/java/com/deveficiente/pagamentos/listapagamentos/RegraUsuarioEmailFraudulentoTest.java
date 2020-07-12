package com.deveficiente.pagamentos.listapagamentos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.framework;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Usuario;

public class RegraUsuarioEmailFraudulentoTest {

	private @NotNull @Valid Usuario usuario = new Usuario("a@a.com",
			FormaPagamento.elo);
	private FraudeRepository fraudeRepository = Mockito
			.mock(FraudeRepository.class);

	@Test
	@DisplayName("deveria aceitar todo pagamento offline")
	void teste1() {
		Mockito.when(fraudeRepository.findByEmail(usuario.getEmail()))
				.thenReturn(Optional.of(new Fraude(usuario.getEmail())));

		RegraUsuarioEmailFraudulento regra = new RegraUsuarioEmailFraudulento(
				fraudeRepository);
		boolean aceite = regra.aceita(FormaPagamento.dinheiro, usuario);
		Assertions.assertTrue(aceite);
	}

	@Test
	@DisplayName("deveria aceitar todo pagamento online para usuarios diferentes de shun")
	void teste2() {
		Mockito.when(fraudeRepository.findByEmail(usuario.getEmail()))
		.thenReturn(Optional.empty());		
		
		RegraUsuarioEmailFraudulento regra = new RegraUsuarioEmailFraudulento(
				fraudeRepository);
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
		Mockito.when(fraudeRepository.findByEmail(shun.getEmail()))
		.thenReturn(Optional.of(new Fraude(shun.getEmail())));
		
		RegraUsuarioEmailFraudulento regra = new RegraUsuarioEmailFraudulento(
				fraudeRepository);
		boolean aceite = regra.aceita(FormaPagamento.elo, shun);
		Assertions.assertFalse(aceite);
	}

}
