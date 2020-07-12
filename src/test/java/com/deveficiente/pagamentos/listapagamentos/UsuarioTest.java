package com.deveficiente.pagamentos.listapagamentos;

import java.util.Collection;
import java.util.List;
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
		RegraFraude regraAceita = (forma, usuario) -> {
			return true;
		};
		Collection<RegraFraude> regras = List.of(regraAceita);

		Set<FormaPagamento> formas = seya.filtraFormasPagamento(sagaSantuario,
				regras);

		Assertions.assertEquals(2, formas.size());
		Assertions.assertTrue(formas.contains(FormaPagamento.visa));
		Assertions.assertTrue(formas.contains(FormaPagamento.master));
	}

	@Test
	@DisplayName("deveria retornar vazio se alguma regra retornar falso")
	public void teste3() {
		RegraFraude regraNaoAceita = (forma, usuario) -> {
			return false;
		};
		Collection<RegraFraude> regras = List.of(regraNaoAceita);

		Set<FormaPagamento> formas = seya.filtraFormasPagamento(sagaSantuario,
				regras);

		Assertions.assertTrue(formas.isEmpty());
	}

	@Test
	@DisplayName("deveria retornar uma lista vazia para usuarios que nao tem pagamentos combinados com o restaurante e com regra que aceita")
	public void teste2() {
		RegraFraude regraAceita = (forma, usuario) -> {
			return true;
		};

		Collection<RegraFraude> regras = List.of(regraAceita);

		Set<FormaPagamento> formas = seya.filtraFormasPagamento(sagaPoseydon,
				regras);
		Assertions.assertTrue(formas.isEmpty());
	}
	
	@Test
	@DisplayName("deveria retornar uma lista vazia para usuarios que nao tem pagamentos combinados com o restaurante e com regra que nao aceita")
	public void teste4() {
		RegraFraude regraAceita = (forma, usuario) -> {
			return false;
		};
		
		Collection<RegraFraude> regras = List.of(regraAceita);
		
		Set<FormaPagamento> formas = seya.filtraFormasPagamento(sagaPoseydon,
				regras);
		Assertions.assertTrue(formas.isEmpty());
	}
}
