package com.deveficiente.pagamentos.listapagamentos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.ExecutaTransacao;
import com.deveficiente.pagamentos.pagamentoonline.ForceSiteCall;

@Component
public class CacheListagem {
	
	@Autowired
	//1
	private ExecutaTransacao executaTransacao;
	@Value("${cache.usuario-seleciona-restaurante.quantidade}")
	private int nVezes;
	@Value("${cache.usuario-seleciona-restaurante.tempo-expiracao}")
	private int tempoExpiracao;
	@PersistenceContext
	private EntityManager manager;	
	

	/**
	 * 
	 * @param detalhes lista para gerar a resposta
	 * @param usuario
	 * @param restaurante
	 * @return resposta para ser enviada para o cliente
	 */
	public ResponseEntity<List<DetalheFormaPagamento>> executa(
			List<DetalheFormaPagamento> detalhes, Usuario usuario,
			Restaurante restaurante) {
		
		ForceSiteCall.fromExactlyPoint(ListaPagamentosController.class);
		
		// 1
		executaTransacao.executa(() -> {
			usuario.registraSelecao(restaurante);
			manager.merge(usuario);
			return null;
		});
		
		
		//1
		if (usuario.selecionou(restaurante, nVezes)) {
			return ResponseEntity.ok()
					.header("Expires",
							LocalDateTime.now().plusSeconds(tempoExpiracao)
									.format(DateTimeFormatter.ISO_DATE_TIME))
					.body(detalhes);
		}
		
		
		return ResponseEntity.ok(detalhes);
	}

	
}
