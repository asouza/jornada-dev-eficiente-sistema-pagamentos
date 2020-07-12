package com.deveficiente.pagamentos.listapagamentos;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Usuario;

@Service
public class RegraUsuarioEmailFraudulento implements RegraFraude {
	
	private Set<String> emailsBloqueados = Set.of("shun@deveficiente.com");

	/**
	 * 
	 * @param formaPagamento
	 * @param usuario
	 * @return true se o email do usuario nao estiver bloqueado para pagamentos online
	 */
	@Override
	public boolean aceita(
			@NotNull FormaPagamento formaPagamento,
			@NotNull @Valid Usuario usuario) {
		if(!formaPagamento.online) {
			return true;
		}
		
		return formaPagamento.online && !emailsBloqueados.contains(usuario.getEmail());
	}

}
