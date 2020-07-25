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
public interface Gateway extends Comparable<Gateway> {

	boolean aceita(@NotNull @Valid Pagamento novoPagamentoSalvo);

	Resultado<Exception,Transacao> processa(@NotNull @Valid Pagamento pagamento);
	
	@Override
	boolean equals(Object obj);
	
	@Override
	int hashCode();

	BigDecimal custo();
	

}
