package com.deveficiente.pagamentos.listapagamentos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.CombinacaoUsuarioRestaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.ExecutaTransacao;
import com.deveficiente.pagamentos.pagamentoonline.ForceSiteCall;

@Component
//4
public class CacheListaFormasPagamento {

	private int nVezes;
	private int tempoExpiracao;
	// 1
	private ContaSelecaoUsuarioRestaurante selecoes;

	/**
	 * 
	 * @param nVezes numero de vezes que a combinacao deve ser selecionada
	 * @param tempoExpiracao tempo em segundos
	 * @param selecoes alguma implementacao que possamos buscar selecoes passadas. Olhe para {@link CombinacaoUsuarioRestauranteRepository}
	 */
	public CacheListaFormasPagamento(
			@Value("${cache.usuario-seleciona-restaurante.quantidade}") int nVezes,
			@Value("${cache.usuario-seleciona-restaurante.tempo-expiracao}") int tempoExpiracao,
			ContaSelecaoUsuarioRestaurante selecoes) {
		super();
		this.nVezes = nVezes;
		this.tempoExpiracao = tempoExpiracao;
		this.selecoes = selecoes;
	}

	// 1
	public ResponseEntity<Collection<DetalheFormaPagamento>> executa(
			// 1
			CombinacaoUsuarioRestaurante combinacao,
			Function<CombinacaoUsuarioRestaurante, Collection<DetalheFormaPagamento>> filtraFormasPagamento) {

		Collection<DetalheFormaPagamento> detalhes = filtraFormasPagamento
				.apply(combinacao);
		// 1
		long numeroSelecao = combinacao.contaNumeroUsos(selecoes);
		if (numeroSelecao >= nVezes) {
			return ResponseEntity.ok()
					.header("Expires",
							LocalDateTime.now().plusSeconds(tempoExpiracao)
									.format(DateTimeFormatter.ISO_DATE_TIME))
					.body(detalhes);
		}

		return ResponseEntity.ok(detalhes);
	}

}
