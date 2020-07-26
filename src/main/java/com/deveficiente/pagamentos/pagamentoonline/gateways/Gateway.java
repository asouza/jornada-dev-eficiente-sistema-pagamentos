package com.deveficiente.pagamentos.pagamentoonline.gateways;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;
import com.deveficiente.pagamentos.pagamentoonline.Resultado;

/**
 * Represent um gateway de pagamento de pagamento
 * A implementação sempre deveria sobreescrever o equals e hashcode
 * @author albertoluizsouza
 *
 */
public abstract class Gateway {

	public boolean aceita(@NotNull @Valid Pagamento pagamento) {
		Assert.isTrue(!pagamento.foiConcluido(),"Por algum motivo um motivo já concluído está tentando ser processado de novo");
		return aceiteEspecifico(pagamento);
	}

	protected abstract boolean aceiteEspecifico(
			@NotNull @Valid Pagamento pagamento);
	
	public Resultado<Exception,Transacao> processa(@NotNull @Valid Pagamento pagamento){
		this.aceita(pagamento);
		return processaEspecifico(pagamento);
	}

	protected abstract Resultado<Exception,Transacao> processaEspecifico(@NotNull @Valid Pagamento pagamento);
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();

	public BigDecimal custo(@NotNull @Valid Pagamento pagamento) {
		this.aceita(pagamento);
		return custoEspecifico(pagamento);
	}
	protected abstract BigDecimal custoEspecifico(@NotNull @Valid Pagamento pagamento);
	

}
