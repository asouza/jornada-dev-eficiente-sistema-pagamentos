package com.deveficiente.pagamentos.listapagamentos;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	//1
	private Collection<RegraFraude> regrasFraude;
	@Autowired
	private ExecutaTransacao executaTransacao;

	@GetMapping(value = "/pagamentos")
	//1
	public List<DetalheFormaPagamento> lista(
			//1
			@Valid FiltraFormasPagamentoRequest request) {

		//1
		Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
		//1
		Restaurante restaurante = manager.find(Restaurante.class,
				request.getIdRestaurante());
		
		//1
		executaTransacao.executa(() -> {
			usuario.registraSelecao(restaurante);
			return null;
		});

		//1
		Set<FormaPagamento> formasPagamento = usuario
				.filtraFormasPagamento(restaurante,regrasFraude);

		//1
		return formasPagamento.stream().map(DetalheFormaPagamento::new)
				.collect(Collectors.toList());
	}

}
