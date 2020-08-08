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

	@Value("${cache.usuario-seleciona-restaurante.quantidade}")
	private int nVezes;
	@Value("${cache.usuario-seleciona-restaurante.tempo-expiracao}")
	private int tempoExpiracao;
	@Autowired
	//1
	private CombinacaoUsuarioRestauranteRepository combinacaoUsuarioRestauranteRepository;

	//1
	public ResponseEntity<Collection<DetalheFormaPagamento>> executa(
			//1
			CombinacaoUsuarioRestaurante combinacao,
			Function<CombinacaoUsuarioRestaurante, Collection<DetalheFormaPagamento>> filtraFormasPagamento) {

		Collection<DetalheFormaPagamento> detalhes = filtraFormasPagamento
				.apply(combinacao);
		// 1
		if (combinacaoUsuarioRestauranteRepository
				.contaSelecaoUsuarioRestaurante(combinacao.getUsuarioId(),
						combinacao.getRestauranteId()) >= nVezes) {
			return ResponseEntity.ok()
					.header("Expires",
							LocalDateTime.now().plusSeconds(tempoExpiracao)
									.format(DateTimeFormatter.ISO_DATE_TIME))
					.body(detalhes);
		}

		return ResponseEntity.ok(detalhes);
	}

}
