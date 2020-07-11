package com.deveficiente.pagamentos.listapagamentos;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.modeladominio.Restaurante;
import com.deveficiente.pagamentos.modeladominio.Usuario;

@RestController
public class ListaPagamentosController {

	@PersistenceContext
	private EntityManager manager;

	@GetMapping(value = "/pagamentos")
	public List<DetalheFormaPagamento> lista(
			@Valid FiltraFormasPagamentoRequest request) {

		Usuario usuario = manager.find(Usuario.class, request.getIdUsuario());
		Restaurante restaurante = manager.find(Restaurante.class,
				request.getIdRestaurante());

		Set<FormaPagamento> formasPagamento = usuario
				.filtraFormasPagamento(restaurante);

		return formasPagamento.stream().map(DetalheFormaPagamento::new)
				.collect(Collectors.toList());
	}

}
