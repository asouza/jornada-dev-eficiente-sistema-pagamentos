package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.outrossistemas.DadosCartaoSeyaRequest;
import com.deveficiente.pagamentos.outrossistemas.DadosCompraSeyaRequest;
import com.deveficiente.pagamentos.pagamentooffline.DadosCartao;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;

@Service
public class GatewaySeya implements Gateway{
	
	@Autowired
	private RequestsGateways requestsGateways;

	@Override
	public int compareTo(Gateway o) {
		return this.custo().compareTo(o.custo());
	}

	@Override
	public boolean aceita(@NotNull @Valid Pagamento pagamento) {
		FormaPagamento formaPagamento = pagamento.getFormaPagamento();
		//aqui tem um perigo para o caso de novas formas
		return formaPagamento.online;
	}

	@Override
	public Resultado<Exception, Transacao> processa(
			@NotNull @Valid Pagamento pagamento) {
		DadosCartao dadosCartao = pagamento.getDadosCartao();
		int codigo = requestsGateways.seyaVerifica(new DadosCartaoSeyaRequest(dadosCartao));
		requestsGateways.seyaProcessa(codigo, new DadosCompraSeyaRequest(dadosCartao,pagamento.getValor()));
		
		return Resultado.sucesso(Transacao.concluida(this));
	}

	@Override
	public BigDecimal custo() {
		return new BigDecimal("6");
	}

}
