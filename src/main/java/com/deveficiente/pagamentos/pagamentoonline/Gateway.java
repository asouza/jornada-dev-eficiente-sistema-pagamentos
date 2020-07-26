package com.deveficiente.pagamentos.pagamentoonline;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.deveficiente.pagamentos.pagamentooffline.Pagamento;
import com.deveficiente.pagamentos.pagamentooffline.Transacao;

/**
 * Represent um gateway de pagamento. 
 * A implementação sempre deveria sobreescrever o equals e hashcode
 * @author albertoluizsouza
 *
 */
public abstract class Gateway {

	public boolean aceita(@NotNull @Valid Pagamento pagamento) {
		return !pagamento.foiConcluido() && aceiteEspecifico(pagamento);
	}

	protected abstract boolean aceiteEspecifico(
			@NotNull @Valid Pagamento pagamento);

	public abstract Resultado<Exception,Transacao> processa(@NotNull @Valid Pagamento pagamento);
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();

	public abstract BigDecimal custo(@NotNull @Valid Pagamento pagamento);
	

}
