package com.deveficiente.pagamentos.listapagamentos;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

public class UsuarioTest {

	Usuario seya = new Usuario("seya@deveficiente.com", FormaPagamento.visa,
			FormaPagamento.master, FormaPagamento.hipercard);

	Restaurante sagaSantuario = new Restaurante("saga santuario",
			FormaPagamento.visa, FormaPagamento.master, FormaPagamento.elo);

	Restaurante sagaPoseydon = new Restaurante("saga poseydon",
			FormaPagamento.elo, FormaPagamento.dinheiro,
			FormaPagamento.maquineta);

	@Test
	@DisplayName("deveria retornar os pagamentos aceitos")
	public void teste1() {
		Set<FormaPagamento> formas = seya.filtraFormasPagamento(sagaSantuario);
		Assertions.assertEquals(2, formas.size());
		Assertions.assertTrue(formas.contains(FormaPagamento.visa));
		Assertions.assertTrue(formas.contains(FormaPagamento.master));
	}

	@Test
	@DisplayName("deveria retornar uma lista vazia")
	public void teste2() {
		Set<FormaPagamento> formas = seya.filtraFormasPagamento(sagaPoseydon);
		Assertions.assertTrue(formas.isEmpty());
	}
}
