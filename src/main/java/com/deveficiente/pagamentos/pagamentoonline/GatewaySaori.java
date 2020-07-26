package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.outrossistemas.DadosCompraGenerico;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;

@Service
public class GatewaySaori implements Gateway {
	
	
	private static final Logger log = LoggerFactory
			.getLogger(GatewaySaori.class);

	
	@Autowired
	private RequestsGateways requestsGateways;

	@Override
	public boolean aceita(@NotNull @Valid Pagamento novoPagamentoSalvo) {
		FormaPagamento formaPagamento = novoPagamentoSalvo.getFormaPagamento();
		return formaPagamento.pertence(FormaPagamento.visa,FormaPagamento.master);
	}

	@Override
	public Resultado<Exception, Transacao> processa(
			@NotNull @Valid Pagamento pagamento) {
		
		log.debug("Processando pagamento por gateway Saori");
		DadosCompraGenerico request = new DadosCompraGenerico(pagamento);
		requestsGateways.saoriProcessa(request);
		return Resultado.sucesso(Transacao.concluida(this));
	}

	@Override
	public BigDecimal custo(Pagamento pagamento) {
		return pagamento.getValor().multiply(new BigDecimal("0.05"));
	}

	@Override
	public String toString() {
		return "GatewaySaori []";
	}
	
	

}
