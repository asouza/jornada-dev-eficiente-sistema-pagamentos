package com.deveficiente.pagamentos.pagamentoonline.gateways;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deveficiente.pagamentos.modeladominio.FormaPagamento;
import com.deveficiente.pagamentos.outrossistemas.DadosCartaoSeyaRequest;
import com.deveficiente.pagamentos.outrossistemas.DadosCompraSeyaRequest;
import com.deveficiente.pagamentos.pagamentooffline.DadosCartao;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;
import com.deveficiente.pagamentos.pagamentoonline.RequestsGateways;
import com.deveficiente.pagamentos.pagamentoonline.Resultado;

@Service
public class GatewaySeya extends Gateway{
	
	@Autowired
	private RequestsGateways requestsGateways;
	
	private String id =  "gateway-seya";
	
	private static final Logger log = LoggerFactory
			.getLogger(GatewaySeya.class);


	@Override
	public boolean aceiteEspecifico(@NotNull @Valid Pagamento pagamento) {
		FormaPagamento formaPagamento = pagamento.getFormaPagamento();
		//aqui tem um perigo para o caso de novas formas
		return formaPagamento.online;
	}

	@Override
	public Resultado<Exception, Transacao> processaEspecifico(
			@NotNull @Valid Pagamento pagamento) {
		log.debug("Processando pagamento por gateway Seya");
		DadosCartao dadosCartao = pagamento.getDadosCartao();
		int codigo = requestsGateways.seyaVerifica(new DadosCartaoSeyaRequest(dadosCartao));
		requestsGateways.seyaProcessa(codigo, new DadosCompraSeyaRequest(dadosCartao,pagamento.getValor()));
		
		return Resultado.sucesso(Transacao.concluida(this));
	}

	@Override
	public BigDecimal custoEspecifico(Pagamento pagamento) {
		return new BigDecimal("6");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GatewaySeya other = (GatewaySeya) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
