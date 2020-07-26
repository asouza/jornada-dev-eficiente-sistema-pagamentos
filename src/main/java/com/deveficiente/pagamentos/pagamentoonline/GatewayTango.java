package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deveficiente.pagamentos.outrossistemas.DadosCompraGenerico;
import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;

@Service
public class GatewayTango extends Gateway {

	@Autowired
	private RequestsGateways requestsGateways;
	private String id = "gateway-tango";

	@Override
	public boolean aceiteEspecifico(@NotNull @Valid Pagamento pagamento) {
		return pagamento.getFormaPagamento().online;
	}

	@Override
	public Resultado<Exception, Transacao> processa(
			@NotNull @Valid Pagamento pagamento) {
		requestsGateways.tangoProcessa(new DadosCompraGenerico(pagamento));
		return Resultado.sucesso(Transacao.concluida(this));
	}

	@Override
	public BigDecimal custo(@NotNull @Valid Pagamento pagamento) {
		// isso aqui precisa ser definido pelo negócio
		BigDecimal valor = pagamento.getValor().setScale(2,
				RoundingMode.HALF_EVEN);

		if (valor.compareTo(new BigDecimal("100.00")) <= 0) {
			return new BigDecimal("4");
		}
		return valor.multiply(new BigDecimal("0.06"));
	}

	@Override
	public String toString() {
		return "GatewayTango []";
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
		GatewayTango other = (GatewayTango) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
