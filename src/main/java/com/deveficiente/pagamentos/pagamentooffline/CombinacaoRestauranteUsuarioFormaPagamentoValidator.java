package com.deveficiente.pagamentos.pagamentooffline;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.deveficiente.pagamentos.listapagamentos.RegraFraude;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

@Component
public class CombinacaoRestauranteUsuarioFormaPagamentoValidator
		implements Validator {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private Collection<RegraFraude> regrasFraude;

	@Override
	public boolean supports(Class<?> clazz) {
		return NovoPedidoOfflineRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (errors.hasErrors()) {
			return;
		}

		NovoPedidoOfflineRequest request = (NovoPedidoOfflineRequest) target;
		Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
		Restaurante restaurante = manager.find(Restaurante.class,
				request.getIdRestaurante());

		boolean podePagar = usuario.podePagar(restaurante,
				request.getFormaPagamento(), regrasFraude);
		if (!podePagar) {
			errors.reject(null,
					"A combinacao entre usuario, restaurante e forma de pagamento nao eh valida");
		}
	}

}
