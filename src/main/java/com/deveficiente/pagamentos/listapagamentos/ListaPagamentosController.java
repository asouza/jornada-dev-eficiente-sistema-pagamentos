package com.deveficiente.pagamentos.listapagamentos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;
import com.deveficiente.pagamentos.pagamentooffline.ExecutaTransacao;

@RestController
public class ListaPagamentosController {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	// 1
	private Collection<RegraFraude> regrasFraude;
	@Autowired
	private ExecutaTransacao executaTransacao;
	@Value("${cache.usuario-seleciona-restaurante.quantidade}")
	private int nVezes;
	@Value("${cache.usuario-seleciona-restaurante.tempo-expiracao}")
	private int tempoExpiracao;

	@GetMapping(value = "/pagamentos")
	// 1
	public ResponseEntity<List<DetalheFormaPagamento>> lista(
			// 1
			@Valid FiltraFormasPagamentoRequest request) {

		// 1
		Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
		// 1
		Restaurante restaurante = manager.find(Restaurante.class,
				request.getIdRestaurante());

		// 1
		executaTransacao.executa(() -> {
			usuario.registraSelecao(restaurante);
			manager.merge(usuario);
			return null;
		});

		// 1
		Set<FormaPagamento> formasPagamento = usuario
				.filtraFormasPagamento(restaurante, regrasFraude);

		// 1
		List<DetalheFormaPagamento> detalhes = formasPagamento.stream()
				.map(DetalheFormaPagamento::new).collect(Collectors.toList());

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
