package com.deveficiente.pagamentos.pagamentoofline;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;

import com.deveficiente.pagamentos.listapagamentos.RegraFraude;
import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.CombinacaoRestauranteUsuarioFormaPagamentoValidator;
import com.deveficiente.pagamentos.pagamentooffline.NovoPedidoOfflineRequest;

public class CombinacaoRestauranteUsuarioFormaPagamentoValidatorTest {

	@Test
	@DisplayName("verifica se a combinacao entre usuario, restaurante e forma de pagamento é valida")
	public void teste1() {

		EntityManager manager = Mockito.mock(EntityManager.class);
		RegraFraude regra = (formaPagamento, usuario) -> {
			return true;
		};
		Collection<RegraFraude> regrasFraude = List.of(regra);

		CombinacaoRestauranteUsuarioFormaPagamentoValidator validator = new CombinacaoRestauranteUsuarioFormaPagamentoValidator(
				manager, regrasFraude);
		
		Usuario usuarioPagaComDinheiro = new Usuario("email@email.com",
				FormaPagamento.dinheiro);

		Restaurante restauranteAceitaDinheiro = new Restaurante("restaurante",
				FormaPagamento.dinheiro, FormaPagamento.elo);

		NovoPedidoOfflineRequest request = new NovoPedidoOfflineRequest(
				FormaPagamento.dinheiro, 1l, 1l);
		
		Mockito.when(manager.find(Usuario.class, 1l)).thenReturn(usuarioPagaComDinheiro);
		Mockito.when(manager.find(Restaurante.class, 1l)).thenReturn(restauranteAceitaDinheiro);
		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(errors.hasErrors()).thenReturn(false);
		
		validator.validate(request, errors);
		
		Mockito.verify(errors,Mockito.never()).reject(null,
				"A combinacao entre usuario, restaurante e forma de pagamento nao eh valida");
	}
	
	@Test
	@DisplayName("rejeita se a combinacao entre usuario, restaurante e forma de pagamento não é valida")
	public void teste2() {
		
		EntityManager manager = Mockito.mock(EntityManager.class);
		RegraFraude regra = (formaPagamento, usuario) -> {
			return true;
		};
		Collection<RegraFraude> regrasFraude = List.of(regra);
		
		CombinacaoRestauranteUsuarioFormaPagamentoValidator validator = new CombinacaoRestauranteUsuarioFormaPagamentoValidator(
				manager, regrasFraude);
		
		Usuario usuarioPagaComDinheiro = new Usuario("email@email.com",
				FormaPagamento.maquineta);
		
		Restaurante restauranteAceitaDinheiro = new Restaurante("restaurante",
				FormaPagamento.dinheiro, FormaPagamento.elo);
		
		NovoPedidoOfflineRequest request = new NovoPedidoOfflineRequest(
				FormaPagamento.dinheiro, 1l, 1l);
		
		Mockito.when(manager.find(Usuario.class, 1l)).thenReturn(usuarioPagaComDinheiro);
		Mockito.when(manager.find(Restaurante.class, 1l)).thenReturn(restauranteAceitaDinheiro);
		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(errors.hasErrors()).thenReturn(false);
		
		validator.validate(request, errors);
		
		Mockito.verify(errors).reject(null,
				"A combinacao entre usuario, restaurante e forma de pagamento nao eh valida");
	}
}
