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
import com.deveficiente.pagamentos.pagamentoonline.TemCombinacaoUsuarioRestauranteFormaPagamento;

@Component
public class CombinacaoRestauranteUsuarioFormaPagamentoValidator
		implements Validator {

	private EntityManager manager;
	private Collection<RegraFraude> regrasFraude;

	public CombinacaoRestauranteUsuarioFormaPagamentoValidator(
			EntityManager manager, Collection<RegraFraude> regrasFraude) {
		super();
		this.manager = manager;
		this.regrasFraude = regrasFraude;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return TemCombinacaoUsuarioRestauranteFormaPagamento.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (errors.hasErrors()) {
			return;
		}

		TemCombinacaoUsuarioRestauranteFormaPagamento request = (TemCombinacaoUsuarioRestauranteFormaPagamento) target;
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
