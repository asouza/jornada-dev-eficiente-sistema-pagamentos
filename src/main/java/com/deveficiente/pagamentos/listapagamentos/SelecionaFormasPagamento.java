package com.deveficiente.pagamentos.listapagamentos;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deveficiente.pagamentos.modeladominio.CombinacaoUsuarioRestaurante;
import com.deveficiente.pagamentos.modeladominio.FormaPagamento;

@Service
public class SelecionaFormasPagamento {

	@Autowired
	// 1
	private Collection<RegraFraude> regrasFraude;

	//1
	public Set<DetalheFormaPagamento> executa(
			//1
			CombinacaoUsuarioRestaurante combinacao) {
		//1
		Set<FormaPagamento> formas = combinacao
				.selecionaFormasPagamento(regrasFraude);
		//1
		return formas.stream().map(DetalheFormaPagamento::new)
				.collect(Collectors.toSet());
	}

}
