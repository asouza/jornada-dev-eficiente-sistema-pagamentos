package com.deveficiente.pagamentos.listapagamentos;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import com.deveficiente.pagamentos.modeladominio.CombinacaoUsuarioRestaurante;
import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

public class CacheListaFormasPagamentoTest {
	
	private Usuario usuario = Mockito.spy(new Usuario("a@a.com.br", FormaPagamento.visa));
	private Restaurante restaurante = Mockito.spy(new Restaurante("a", FormaPagamento.visa));
	
	{
		Mockito.when(usuario.getId()).thenReturn(Optional.of(1l));
		Mockito.when(restaurante.getId()).thenReturn(Optional.of(1l));
	}

	@Test
	@DisplayName("deveria colocar no cache quando chegue no limite de selecao")
	void teste1() throws Exception {
		ContaSelecaoUsuarioRestaurante selecoes = (idUsario,idRestaurante) -> {
			return 2;
		};
		
		CacheListaFormasPagamento cache = new CacheListaFormasPagamento(2, 10, selecoes);
		CombinacaoUsuarioRestaurante combinacao = new CombinacaoUsuarioRestaurante(usuario, restaurante);
		
		
		
		ResponseEntity<Collection<DetalheFormaPagamento>> resposta = cache.executa(combinacao, combinacaoUtilizada -> {
			return List.of(new DetalheFormaPagamento(FormaPagamento.elo));
		});
		
		Assertions.assertTrue(resposta.getHeaders().containsKey("Expires"));
		
		
	}
	
	@Test
	@DisplayName("deveria colocar no cache caso passe do limite de selecao")
	void teste2() throws Exception {
		ContaSelecaoUsuarioRestaurante selecoes = (idUsario,idRestaurante) -> {
			return 3;
		};
		
		CacheListaFormasPagamento cache = new CacheListaFormasPagamento(2, 10, selecoes);
		CombinacaoUsuarioRestaurante combinacao = new CombinacaoUsuarioRestaurante(usuario, restaurante);
		
		
		
		ResponseEntity<Collection<DetalheFormaPagamento>> resposta = cache.executa(combinacao, combinacaoUtilizada -> {
			return List.of(new DetalheFormaPagamento(FormaPagamento.elo));
		});
		
		Assertions.assertTrue(resposta.getHeaders().containsKey("Expires"));
		
		
	}
	
	@Test
	@DisplayName("não deveria colocar no cache caso não passe do limite de uso")
	void teste3() throws Exception {
		ContaSelecaoUsuarioRestaurante selecoes = (idUsario,idRestaurante) -> {
			return 1;
		};
		
		CacheListaFormasPagamento cache = new CacheListaFormasPagamento(2, 10, selecoes);
		CombinacaoUsuarioRestaurante combinacao = new CombinacaoUsuarioRestaurante(usuario, restaurante);
		
		
		
		ResponseEntity<Collection<DetalheFormaPagamento>> resposta = cache.executa(combinacao, combinacaoUtilizada -> {
			return List.of(new DetalheFormaPagamento(FormaPagamento.elo));
		});
		
		Assertions.assertFalse(resposta.getHeaders().containsKey("Expires"));
		
		
	}
}
