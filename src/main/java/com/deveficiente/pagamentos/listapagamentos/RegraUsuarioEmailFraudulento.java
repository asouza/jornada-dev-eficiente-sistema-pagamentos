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
	
	private FraudeRepository fraudeRepository;
	
	
	public RegraUsuarioEmailFraudulento(FraudeRepository fraudeRepository) {
		super();
		this.fraudeRepository = fraudeRepository;
	}



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
		
		Optional<Fraude> possivelFraude = fraudeRepository.findByEmail(usuario.getEmail());
		
		return formaPagamento.online && possivelFraude.isEmpty();
	}

}
