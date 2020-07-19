package com.deveficiente.pagamentos.conclusaocompraoffline;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.PagamentoRepository;

@RestController
public class ConcluiaCompraOfflineController {

	private PagamentoRepository pagamentoRepository;
	

	public ConcluiaCompraOfflineController(
			PagamentoRepository pagamentoRepository) {
		super();
		this.pagamentoRepository = pagamentoRepository;
	}




	@PostMapping(value = "/pagamento/offline/{codigoPagamento}/finaliza")
	@Transactional
	public void conclui(
			@PathVariable("codigoPagamento") String codigoPagamento) {
		Optional<Pagamento> possivelPagamento = pagamentoRepository
				.findByCodigo(codigoPagamento);
		
		if(possivelPagamento.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		Pagamento pagamento = possivelPagamento.get();
		if(pagamento.foiConcluido()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		pagamento.conclui();
	}

}
