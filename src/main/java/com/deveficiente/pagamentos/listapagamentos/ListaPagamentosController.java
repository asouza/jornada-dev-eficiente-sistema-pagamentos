package com.deveficiente.pagamentos.listapagamentos;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deveficiente.pagamentos.modeladominio.CombinacaoUsuarioRestaurante;
import com.deveficiente.pagamentos.pagamentooffline.ExecutaTransacao;

@RestController
//10
public class ListaPagamentosController {

	@PersistenceContext
	private EntityManager manager;
	//1
	@Autowired
	private CacheListaFormasPagamento cacheListagem;
	@Autowired
	//1
	private ExecutaTransacao executaTransacao;
	@Autowired
	//1
	private SelecionaFormasPagamento selecionaFormasPagamento;	

	@GetMapping(value = "/pagamentos")
	// 1
	public ResponseEntity<Collection<DetalheFormaPagamento>> lista(
			// 1
			@Valid FiltraFormasPagamentoRequest request) {
		
		//1
		CombinacaoUsuarioRestaurante combinacao = request.toModel(manager);
		
		executaTransacao.commit(combinacao);
						
		return cacheListagem.executa(combinacao,(combinacaoUtilizada) -> {
			return selecionaFormasPagamento.executa(combinacaoUtilizada);			
		});
	}

}
